package dev.sweetberry.wwizardry.content.item;

import com.google.common.collect.ImmutableList;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.criterion.CriterionInitializer;
import dev.sweetberry.wwizardry.content.item.tier.CrystallineSculkTier;
import dev.sweetberry.wwizardry.mixin.Accessor_PlayerRespawnLogic;
import dev.sweetberry.wwizardry.mixin.Accessor_ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoulMirrorItem extends TieredItem implements Vanishable {
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
		.addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::below).iterator())
		.addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::above).iterator())
		.add(new Vec3i(0, 1, 0))
		.build();

	public SoulMirrorItem(Properties settings) {
		super(CrystallineSculkTier.INSTANCE, settings);
	}

	public static boolean hasLodestone(ItemStack stack) {
		CompoundTag nbtCompound = stack.getTag();
		return nbtCompound != null && (nbtCompound.contains("LodestoneDimension") || nbtCompound.contains("LodestonePos"));
	}

	@Nullable
	private static ResourceKey<Level> getLodestoneDimension(CompoundTag nbt) {
		return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("LodestoneDimension")).result().orElse(null);
	}

	@Nullable
	public static GlobalPos getLodestonePosition(@Nullable CompoundTag nbt) {
		if (nbt == null)
			return null;

		boolean hasPos = nbt.contains("LodestonePos");
		boolean hasDim = nbt.contains("LodestoneDimension");

		if (!hasPos || !hasDim)
			return null;

		var dim = getLodestoneDimension(nbt);
		if (dim == null)
			return null;

		BlockPos blockPos = NbtUtils.readBlockPos(nbt.getCompound("LodestonePos"));
		return GlobalPos.of(dim, blockPos);
	}

	public static Optional<Vec3> findRespawnPosition(EntityType<?> entity, CollisionGetter world, BlockPos pos) {
		Optional<Vec3> optional = findRespawnPosition(entity, world, pos, true);
		return optional.isPresent() ? optional : findRespawnPosition(entity, world, pos, false);
	}

	private static Optional<Vec3> findRespawnPosition(EntityType<?> entity, CollisionGetter world, BlockPos pos, boolean ignoreInvalidPos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for(Vec3i vec3i : VALID_SPAWN_OFFSETS) {
			mutable.set(pos).move(vec3i);
			Vec3 vec3d = DismountHelper.findSafeDismountLocation(entity, world, mutable, ignoreInvalidPos);
			if (vec3d != null) {
				return Optional.of(vec3d);
			}
		}

		return Optional.empty();
	}

	public boolean isFullyUsed(ItemStack stack) {
		return stack.getDamageValue() >= stack.getMaxDamage() - 1;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (world.isClientSide)
			return;

		if (!hasLodestone(stack))
			return;

		CompoundTag nbtCompound = stack.getOrCreateTag();
		if (nbtCompound.contains("LodestoneTracked") && !nbtCompound.getBoolean("LodestoneTracked"))
			return;

		var dim = getLodestoneDimension(nbtCompound);
		if (dim == null || !nbtCompound.contains("LodestonePos"))
			return;

		var dimWorld = ((ServerLevel)world).getServer().getLevel(dim);
		if (dimWorld == null) {
			nbtCompound.remove("LodestonePos");
			return;
		}

		BlockPos blockPos = NbtUtils.readBlockPos(nbtCompound.getCompound("LodestonePos"));
		if (dimWorld.isInWorldBounds(blockPos) && dimWorld.getPoiManager().existsAtPosition(PoiTypes.LODESTONE, blockPos))
			return;

		nbtCompound.remove("LodestonePos");
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return hasLodestone(stack) || super.isFoil(stack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return isFullyUsed(stack) ? UseAnim.NONE : UseAnim.CROSSBOW;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (world.isClientSide || !(user instanceof ServerPlayer player))
			return stack;

		var server = world.getServer();
		if (server == null)
			return stack;

		player.getCooldowns().addCooldown(this, 20);

		var pos = getLodestonePosition(stack.getTag());
		if (pos != null) {
			var respawnWorld = server.getLevel(pos.dimension());
			if (respawnWorld == null) {
				return stack;
			}
			var respawnPos = findRespawnPosition(EntityType.PLAYER, respawnWorld, pos.pos()).orElse(null);

			if (respawnPos == null)
				return stack;

			if (!player.isCreative())
				stack.hurtAndBreak(1, user, a -> {});

			player.teleportTo(respawnWorld, respawnPos.x, respawnPos.y, respawnPos.z, player.getRespawnAngle(), 0);
			var block = BlockPos.containing(respawnPos.x, respawnPos.y, respawnPos.z);

			if (respawnWorld.dimension() != world.dimension())
				respawnWorld.levelEvent(
					LevelEvent.SOUND_PORTAL_TRAVEL,
					block,
					0
				);
			else
				world.playSeededSound(
					null,
					respawnPos.x(),
					respawnPos.y(),
					respawnPos.z(),
					SoundEvents.ENDERMAN_TELEPORT,
					SoundSource.PLAYERS,
					1, 1,
					0
				);

			return stack;
		}

		if (!player.isCreative())
			stack.hurtAndBreak(1, user, a -> {});

		var posAndWorld = moveToSpawnPoint(server, player);
		var respawnWorld = posAndWorld.world == null ? world : posAndWorld.world;
		var respawnPos = posAndWorld.pos;

		if (respawnWorld.dimension() != world.dimension())
			respawnWorld.levelEvent(
				LevelEvent.SOUND_PORTAL_TRAVEL,
				respawnPos,
				0
			);
		else
			world.playSeededSound(
				null,
				respawnPos.getX(),
				respawnPos.getY(),
				respawnPos.getZ(),
				SoundEvents.ENDERMAN_TELEPORT,
				SoundSource.PLAYERS,
				1, 1,
				0
			);

		return stack;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var stack = user.getItemInHand(hand);
		if (isFullyUsed(stack))
			return InteractionResultHolder.fail(stack);

		user.startUsingItem(hand);

		return InteractionResultHolder.consume(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 30;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos blockPos = context.getClickedPos();
		Level world = context.getLevel();
		if (!world.getBlockState(blockPos).is(Blocks.LODESTONE))
			return super.useOn(context);

		world.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
		Player playerEntity = context.getPlayer();

		if (playerEntity instanceof ServerPlayer serverPlayerEntity)
			CriterionInitializer.LODESTONE_MIRROR.get().trigger(serverPlayerEntity);

		ItemStack itemStack = context.getItemInHand();
		var shouldKeepItem = !playerEntity.getAbilities().instabuild && itemStack.getCount() == 1;
		if (shouldKeepItem) {
			writeNbt(world.dimension(), blockPos, itemStack.getOrCreateTag());
			return InteractionResult.sidedSuccess(world.isClientSide);
		}

		var itemStack2 = getDefaultInstance();
		CompoundTag nbtCompound = itemStack.hasTag() ? itemStack.getTag().copy() : new CompoundTag();
		itemStack2.setTag(nbtCompound);
		if (!playerEntity.getAbilities().instabuild)
			itemStack.shrink(1);

		writeNbt(world.dimension(), blockPos, nbtCompound);
		if (!playerEntity.getInventory().add(itemStack2))
			playerEntity.drop(itemStack2, false);

		return InteractionResult.sidedSuccess(world.isClientSide);
	}

	public static PosAndWorld moveToSpawnPoint(MinecraftServer server, ServerPlayer player) {
		var pos = player.getRespawnPosition();
		if (pos == null)
			return moveToWorldSpawn(server, player);
		var world = server.getLevel(player.getRespawnDimension());
		if (world == null)
			return moveToWorldSpawn(server, player);
		var _respawnPos = Player.findRespawnPositionAndUseSpawnBlock(world, pos, player.getRespawnAngle(), player.isRespawnForced(), true);
		if (_respawnPos.isEmpty())
			return moveToWorldSpawn(server, player);
		var respawnPos = _respawnPos.get();
		player.teleportTo(world, respawnPos.x, respawnPos.y, respawnPos.z, player.getRespawnAngle(), 0);
		return new PosAndWorld(BlockPos.containing(respawnPos.x, respawnPos.y, respawnPos.z), world);
	}

	private static PosAndWorld moveToWorldSpawn(MinecraftServer server, ServerPlayer player) {
		var world = server.overworld();
		var access = (Accessor_ServerPlayer)player;
		BlockPos blockPos = world.getSharedSpawnPos();
		if (world.dimensionType().hasSkyLight() && world.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
			int i = Math.max(0, server.getSpawnRadius(world));
			int j = Mth.floor(world.getWorldBorder().getDistanceToBorder(blockPos.getX(), blockPos.getZ()));
			if (j < i) {
				i = j;
			}

			if (j <= 1) {
				i = 1;
			}

			long l = i * 2L + 1;
			long m = l * l;
			int k = m > 2147483647L ? Integer.MAX_VALUE : (int)m;
			int n = access.invokeGetCoprime(k);
			int o = RandomSource.create().nextInt(k);

			for(int p = 0; p < k; ++p) {
				int q = (o + n * p) % k;
				int r = q % (i * 2 + 1);
				int s = q / (i * 2 + 1);
				BlockPos blockPos2 = Accessor_PlayerRespawnLogic.invokeGetOverworldRespawnPos(world, blockPos.getX() + r - i, blockPos.getZ() + s - i);
				if (blockPos2 != null) {
					var box = new AABB(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), blockPos2.getX()+1, blockPos2.getY()+2, blockPos2.getZ()+1);
					if (world.noCollision(box)) {
						var center = blockPos2.getCenter();
						player.teleportTo(world, center.x, blockPos2.getY(), center.z, 0, 0);
						return new PosAndWorld(blockPos2, world);
					}
				}
			}
		} else {
			var addY = 0;
			do {
				addY++;
				var box = new AABB(blockPos.getX(), blockPos.getY() + addY, blockPos.getZ(), blockPos.getX()+1, blockPos.getY()+2 + addY, blockPos.getZ()+1);
				if (world.noCollision(box))
					break;
			} while (blockPos.getY() + addY < world.getMaxBuildHeight() - 1);
			var center = blockPos.getCenter();
			player.teleportToWithTicket(center.x, blockPos.getY() + addY, center.z);
			return new PosAndWorld(BlockPos.containing(center.x, blockPos.getY() + addY, center.z), world);
		}
		return new PosAndWorld(blockPos, null);
	}

	private void writeNbt(ResourceKey<Level> worldKey, BlockPos pos, CompoundTag nbt) {
		nbt.put("LodestonePos", NbtUtils.writeBlockPos(pos));
		Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, worldKey).resultOrPartial(WanderingWizardry.LOGGER::error).ifPresent(element -> nbt.put("LodestoneDimension", element));
		nbt.putBoolean("LodestoneTracked", true);
	}

	public record PosAndWorld(BlockPos pos, @Nullable ServerLevel world) {}
}
