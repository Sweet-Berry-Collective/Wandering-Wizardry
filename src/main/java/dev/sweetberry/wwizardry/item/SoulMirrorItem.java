package dev.sweetberry.wwizardry.item;

import com.google.common.collect.ImmutableList;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.item.material.CrystallineSculkToolMaterial;
import dev.sweetberry.wwizardry.mixin.Accessor_ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.*;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.CollisionView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Optional;

public class SoulMirrorItem extends ToolItem implements Vanishable {
	public static final SoulMirrorItem INSTANCE = new SoulMirrorItem(
		new QuiltItemSettings()
			.maxCount(1)
	);

	public static final String LODESTONE_POS_KEY = "LodestonePos";
	public static final String LODESTONE_DIMENSION_KEY = "LodestoneDimension";
	public static final String LODESTONE_TRACKED_KEY = "LodestoneTracked";

	private static final ImmutableList<Vec3i> VALID_HORIZONTAL_SPAWN_OFFSETS = ImmutableList.of(
		new Vec3i(0, 0, -1),
		new Vec3i(-1, 0, 0),
		new Vec3i(0, 0, 1),
		new Vec3i(1, 0, 0),
		new Vec3i(-1, 0, -1),
		new Vec3i(1, 0, -1),
		new Vec3i(-1, 0, 1),
		new Vec3i(1, 0, 1)
	);
	private static final ImmutableList<Vec3i> VALID_SPAWN_OFFSETS = new ImmutableList.Builder<Vec3i>()
		.addAll(VALID_HORIZONTAL_SPAWN_OFFSETS)
		.addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::down).iterator())
		.addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::up).iterator())
		.add(new Vec3i(0, 1, 0))
		.build();

	public SoulMirrorItem(Settings settings) {
		super(CrystallineSculkToolMaterial.INSTANCE, settings);
	}

	public static boolean hasLodestone(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		return nbtCompound != null && (nbtCompound.contains("LodestoneDimension") || nbtCompound.contains("LodestonePos"));
	}

	@Nullable
	private static RegistryKey<World> getLodestoneDimension(NbtCompound nbt) {
		return World.CODEC.parse(NbtOps.INSTANCE, nbt.get("LodestoneDimension")).result().orElse(null);
	}

	@Nullable
	public static GlobalPos getLodestonePosition(@Nullable NbtCompound nbt) {
		if (nbt == null)
			return null;

		boolean hasPos = nbt.contains("LodestonePos");
		boolean hasDim = nbt.contains("LodestoneDimension");

		if (!hasPos || !hasDim)
			return null;

		var dim = getLodestoneDimension(nbt);
		if (dim == null)
			return null;

		BlockPos blockPos = NbtHelper.toBlockPos(nbt.getCompound("LodestonePos"));
		return GlobalPos.create(dim, blockPos);
	}

	public static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos) {
		Optional<Vec3d> optional = findRespawnPosition(entity, world, pos, true);
		return optional.isPresent() ? optional : findRespawnPosition(entity, world, pos, false);
	}

	private static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos, boolean ignoreInvalidPos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(Vec3i vec3i : VALID_SPAWN_OFFSETS) {
			mutable.set(pos).move(vec3i);
			Vec3d vec3d = Dismounting.findRespawnPos(entity, world, mutable, ignoreInvalidPos);
			if (vec3d != null) {
				return Optional.of(vec3d);
			}
		}

		return Optional.empty();
	}

	public boolean isFullyUsed(ItemStack stack) {
		return stack.getDamage() >= stack.getMaxDamage() - 1;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient)
			return;

		if (!hasLodestone(stack))
			return;

		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (nbtCompound.contains("LodestoneTracked") && !nbtCompound.getBoolean("LodestoneTracked"))
			return;

		var dim = getLodestoneDimension(nbtCompound);
		if (dim == null || !nbtCompound.contains("LodestonePos"))
			return;

		var dimWorld = ((ServerWorld)world).getServer().getWorld(dim);
		if (dimWorld == null) {
			nbtCompound.remove("LodestonePos");
			return;
		}

		BlockPos blockPos = NbtHelper.toBlockPos(nbtCompound.getCompound("LodestonePos"));
		if (dimWorld.isInBuildLimit(blockPos) && dimWorld.getPointOfInterestStorage().hasTypeAt(PointOfInterestTypes.LODESTONE, blockPos))
			return;

		nbtCompound.remove("LodestonePos");
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return hasLodestone(stack) || super.hasGlint(stack);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return isFullyUsed(stack) ? UseAction.NONE : UseAction.CROSSBOW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (world.isClient || !(user instanceof ServerPlayerEntity player))
			return stack;

		var server = world.getServer();
		if (server == null)
			return stack;

		player.getItemCooldownManager().set(INSTANCE, 20);

		var pos = getLodestonePosition(stack.getNbt());
		if (pos != null) {
			var respawnWorld = server.getWorld(pos.getDimension());
			if (respawnWorld == null) {
				return stack;
			}
			var respawnPos = findRespawnPosition(EntityType.PLAYER, respawnWorld, pos.getPos()).orElse(null);

			if (respawnPos == null)
				return stack;

			if (!player.isCreative())
				stack.damage(1, user, a -> {});

			player.teleport(respawnWorld, respawnPos.x, respawnPos.y, respawnPos.z, player.getSpawnAngle(), 0);
			var block = BlockPos.create(respawnPos.x, respawnPos.y, respawnPos.z);

			if (respawnWorld.getRegistryKey() != world.getRegistryKey())
				respawnWorld.syncWorldEvent(
					WorldEvents.TRAVEL_THROUGH_PORTAL,
					block,
					0
				);
			else
				world.playSound(
					null,
					respawnPos.getX(),
					respawnPos.getY(),
					respawnPos.getZ(),
					SoundEvents.ENTITY_ENDERMAN_TELEPORT,
					SoundCategory.PLAYERS,
					1, 1,
					0
				);

			return stack;
		}

		if (!player.isCreative())
			stack.damage(1, user, a -> {});

		var posAndWorld = moveToSpawnPoint(server, player);
		var respawnWorld = posAndWorld.world == null ? world : posAndWorld.world;
		var respawnPos = posAndWorld.pos;

		if (respawnWorld.getRegistryKey() != world.getRegistryKey())
			respawnWorld.syncWorldEvent(
				WorldEvents.TRAVEL_THROUGH_PORTAL,
				respawnPos,
				0
			);
		else
			world.playSound(
				null,
				respawnPos.getX(),
				respawnPos.getY(),
				respawnPos.getZ(),
				SoundEvents.ENTITY_ENDERMAN_TELEPORT,
				SoundCategory.PLAYERS,
				1, 1,
				0
			);

		return stack;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		if (isFullyUsed(stack))
			return TypedActionResult.fail(stack);

		user.setCurrentHand(hand);

		return TypedActionResult.consume(stack);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 30;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos blockPos = context.getBlockPos();
		World world = context.getWorld();
		if (!world.getBlockState(blockPos).isOf(Blocks.LODESTONE)) {
			return super.useOnBlock(context);
		} else {
			world.playSound(null, blockPos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0F, 1.0F);
			PlayerEntity playerEntity = context.getPlayer();
			ItemStack itemStack = context.getStack();
			var shouldKeepItem = !playerEntity.getAbilities().creativeMode && itemStack.getCount() == 1;
			if (shouldKeepItem) {
				writeNbt(world.getRegistryKey(), blockPos, itemStack.getOrCreateNbt());
			} else {
				ItemStack itemStack2 = new ItemStack(INSTANCE, 1);
				NbtCompound nbtCompound = itemStack.hasNbt() ? itemStack.getNbt().copy() : new NbtCompound();
				itemStack2.setNbt(nbtCompound);
				if (!playerEntity.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}

				writeNbt(world.getRegistryKey(), blockPos, nbtCompound);
				if (!playerEntity.getInventory().insertStack(itemStack2)) {
					playerEntity.dropItem(itemStack2, false);
				}
			}

			return ActionResult.success(world.isClient);
		}
	}

	public static PosAndWorld moveToSpawnPoint(MinecraftServer server, ServerPlayerEntity player) {
		var pos = player.getSpawnPointPosition();
		if (pos == null)
			return moveToWorldSpawn(server, player);
		var world = server.getWorld(player.getSpawnPointDimension());
		if (world == null)
			return moveToWorldSpawn(server, player);
		var _respawnPos = PlayerEntity.findRespawnPosition(world, pos, player.getSpawnAngle(), player.isSpawnPointSet(), true);
		if (_respawnPos.isEmpty())
			return moveToWorldSpawn(server, player);
		var respawnPos = _respawnPos.get();
		player.teleport(world, respawnPos.x, respawnPos.y, respawnPos.z, player.getSpawnAngle(), 0);
		return new PosAndWorld(BlockPos.create(respawnPos.x, respawnPos.y, respawnPos.z), world);
	}

	private static PosAndWorld moveToWorldSpawn(MinecraftServer server, ServerPlayerEntity player) {
		var world = server.getOverworld();
		var access = (Accessor_ServerPlayerEntity)player;
		BlockPos blockPos = world.getSpawnPos();
		if (world.getDimension().hasSkyLight() && world.getServer().getSaveProperties().getGameMode() != GameMode.ADVENTURE) {
			int i = Math.max(0, server.getSpawnRadius(world));
			int j = MathHelper.floor(world.getWorldBorder().getDistanceInsideBorder(blockPos.getX(), blockPos.getZ()));
			if (j < i) {
				i = j;
			}

			if (j <= 1) {
				i = 1;
			}

			long l = i * 2L + 1;
			long m = l * l;
			int k = m > 2147483647L ? Integer.MAX_VALUE : (int)m;
			int n = access.invokeCalculateSpawnOffsetMultiplier(k);
			int o = RandomGenerator.createLegacy().nextInt(k);

			for(int p = 0; p < k; ++p) {
				int q = (o + n * p) % k;
				int r = q % (i * 2 + 1);
				int s = q / (i * 2 + 1);
				BlockPos blockPos2 = SpawnLocating.findOverworldSpawn(world, blockPos.getX() + r - i, blockPos.getZ() + s - i);
				if (blockPos2 != null) {
					var box = new Box(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), blockPos2.getX()+1, blockPos2.getY()+2, blockPos2.getZ()+1);
					if (world.isSpaceEmpty(box)) {
						var center = blockPos2.ofCenter();
						player.teleport(world, center.x, blockPos2.getY(), center.z, 0, 0);
						return new PosAndWorld(blockPos2, world);
					}
				}
			}
		} else {
			var addY = 0;
			do {
				addY++;
				var box = new Box(blockPos.getX(), blockPos.getY() + addY, blockPos.getZ(), blockPos.getX()+1, blockPos.getY()+2 + addY, blockPos.getZ()+1);
				if (world.isSpaceEmpty(box))
					break;
			} while (blockPos.getY() + addY < world.getTopY() - 1);
			var center = blockPos.ofCenter();
			player.teleport(center.x, blockPos.getY() + addY, center.z);
			return new PosAndWorld(BlockPos.create(center.x, blockPos.getY() + addY, center.z), world);
		}
		return new PosAndWorld(blockPos, null);
	}

	private void writeNbt(RegistryKey<World> worldKey, BlockPos pos, NbtCompound nbt) {
		nbt.put("LodestonePos", NbtHelper.fromBlockPos(pos));
		World.CODEC.encodeStart(NbtOps.INSTANCE, worldKey).resultOrPartial(WanderingMod.LOGGER::error).ifPresent(element -> nbt.put("LodestoneDimension", element));
		nbt.putBoolean("LodestoneTracked", true);
	}

	public record PosAndWorld(BlockPos pos, @Nullable ServerWorld world) {}
}
