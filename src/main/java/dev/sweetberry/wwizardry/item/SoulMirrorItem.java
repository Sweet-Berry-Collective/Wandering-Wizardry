package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.item.material.CrystallineSculkToolMaterial;
import dev.sweetberry.wwizardry.mixin.Accessor_ServerPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class SoulMirrorItem extends ToolItem implements Vanishable {
	public static final SoulMirrorItem INSTANCE = new SoulMirrorItem(
		new QuiltItemSettings()
			.maxCount(1)
	);

	public SoulMirrorItem(Settings settings) {
		super(CrystallineSculkToolMaterial.INSTANCE, settings);
	}

	public boolean isFullyUsed(ItemStack stack) {
		return stack.getDamage() >= stack.getMaxDamage() - 1;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return isFullyUsed(stack) ? UseAction.NONE : UseAction.CROSSBOW;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (world.isClient || !(user instanceof ServerPlayerEntity player)) {


			return stack;
		}

		var server = world.getServer();
		if (server == null)
			return stack;

		if (!player.isCreative())
			stack.damage(1, user, a -> {});

		server.getWorld(player.getSpawnPointDimension()).syncWorldEvent(
			WorldEvents.TRAVEL_THROUGH_PORTAL,
			moveToSpawnPoint(server, player),
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

	public static BlockPos moveToSpawnPoint(MinecraftServer server, ServerPlayerEntity player) {
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
		return BlockPos.create(respawnPos.x, respawnPos.y, respawnPos.z);
	}

	private static BlockPos moveToWorldSpawn(MinecraftServer server, ServerPlayerEntity player) {
		var world = server.getOverworld();
		var access = (Accessor_ServerPlayerEntity)player;
		BlockPos blockPos = world.getSpawnPos();
		if (world.getDimension().hasSkyLight() && world.getServer().getSaveProperties().getGameMode() != GameMode.ADVENTURE) {
			int i = Math.max(0, server.getSpawnRadius(world));
			int j = MathHelper.floor(world.getWorldBorder().getDistanceInsideBorder((double)blockPos.getX(), (double)blockPos.getZ()));
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
					var center = blockPos2.ofCenter();
					player.teleport(world, center.x, center.y, center.z, 0, 0);
					if (world.isSpaceEmpty(player)) {
						return blockPos2;
					}
				}
			}
		} else {
			var center = blockPos.ofCenter();
			center = new Vec3d(center.x, Math.floor(center.y), center.z);
			var addY = 0;
			do {
				player.teleport(world, center.x, center.y + addY, center.z, 0, 0);
			} while (!world.isSpaceEmpty(player) && center.y + addY < world.getTopY() - 1);
			return BlockPos.create(center.x, center.y + addY, center.z);
		}
		return blockPos;
	}
}
