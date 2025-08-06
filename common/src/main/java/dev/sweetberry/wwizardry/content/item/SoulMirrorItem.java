package dev.sweetberry.wwizardry.content.item;

import com.google.common.collect.ImmutableList;
import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.content.criterion.CriterionInitializer;
import dev.sweetberry.wwizardry.content.item.tier.CrystallineSculkTier;
import dev.sweetberry.wwizardry.mixin.Accessor_PlayerRespawnLogic;
import dev.sweetberry.wwizardry.mixin.Accessor_ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.LodestoneTracker;
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

public class SoulMirrorItem extends TieredItem implements AltarCraftable {
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
		return stack.has(DataComponents.LODESTONE_TRACKER);
	}

	@Nullable
	public static GlobalPos getLodestonePosition(@Nullable LodestoneTracker tracker) {
		return tracker != null
			? tracker.target().orElse(null)
			: null;
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

		var tracker = stack.get(DataComponents.LODESTONE_TRACKER);
		var pos = getLodestonePosition(tracker);

		if (pos != null)
			return;

		stack.remove(DataComponents.LODESTONE_TRACKER);
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
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 30;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (world.isClientSide || !(user instanceof ServerPlayer player))
			return stack;

		var server = world.getServer();
		if (server == null)
			return stack;

		player.getCooldowns().addCooldown(this, 20);

		var pos = getLodestonePosition(stack.get(DataComponents.LODESTONE_TRACKER));
		if (pos != null) {
			var respawnWorld = server.getLevel(pos.dimension());
			if (respawnWorld == null) {
				return stack;
			}
			var respawnPos = findRespawnPosition(EntityType.PLAYER, respawnWorld, pos.pos()).orElse(null);

			if (respawnPos == null)
				return stack;

			if (!player.isCreative())
				stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);

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
			stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);

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
			writeLodestone(world.dimension(), blockPos, itemStack);
			return InteractionResult.sidedSuccess(world.isClientSide);
		}

		var itemStack2 = getDefaultInstance();

		if (!playerEntity.getAbilities().instabuild)
			itemStack.shrink(1);

		writeLodestone(world.dimension(), blockPos, itemStack2);
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
		var _respawnPos = Accessor_ServerPlayer.invokeFindRespawnAndUseSpawnBlock(world, pos, player.getRespawnAngle(), player.isRespawnForced(), true);
		if (_respawnPos.isEmpty())
			return moveToWorldSpawn(server, player);
		var respawnPos = _respawnPos.get();
		player.teleportTo(world, respawnPos.position().x, respawnPos.position().y, respawnPos.position().z, respawnPos.yaw(), 0);
		return new PosAndWorld(BlockPos.containing(respawnPos.position().x, respawnPos.position().y, respawnPos.position().z), world);
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
			player.teleportTo(center.x, blockPos.getY() + addY, center.z);
			return new PosAndWorld(BlockPos.containing(center.x, blockPos.getY() + addY, center.z), world);
		}
		return new PosAndWorld(blockPos, null);
	}

	private void writeLodestone(ResourceKey<Level> worldKey, BlockPos pos, ItemStack stack) {
		var tracker = new LodestoneTracker(Optional.of(GlobalPos.of(worldKey, pos)), true);
		stack.set(DataComponents.LODESTONE_TRACKER, tracker);
	}

	private void copyLodestone(ItemStack from, ItemStack to) {
		to.set(DataComponents.LODESTONE_TRACKER, from.get(DataComponents.LODESTONE_TRACKER));
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		final var soulMirrorItem = ItemInitializer.SOUL_MIRROR.get();
		view.keepCenter();

		var center = view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER);

		assert center != null;

		if (!center.has(DataComponents.LODESTONE_TRACKER))
			return false;

		for (var i : AltarRecipeView.AltarDirection.cardinals()) {
			var item = view.getItemInPedestal(i);
			if (item == null)
				return false;

			if (item.is(soulMirrorItem))
				copyLodestone(center, item);

			view.setResultInPedestal(i, item);
		}

		return true;
	}

	public record PosAndWorld(BlockPos pos, @Nullable ServerLevel world) {}
}
