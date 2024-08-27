package dev.sweetberry.wwizardry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.block.redstone.CopperLensBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconBlockEntity.class)
public class Mixin_BeaconBlockEntity {
	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getLightBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)I"
		)
	)
	private static int checkCopperLens(BlockState instance, BlockGetter blockGetter, BlockPos pos, Operation<Integer> original) {
		if (instance.getBlock() instanceof CopperLensBlock copperLens) {
			WanderingWizardry.LOGGER.info("waaaa");
			var shouldBlock = copperLens.shouldBlockBeacon(instance);
			WanderingWizardry.LOGGER.info("{}. {}, {}", instance.getValue(CopperLensBlock.AXIS), instance.getValue(CopperLensBlock.FOCUS), shouldBlock);

			return shouldBlock ? 16 : original.call(instance, blockGetter, pos);
		}
		return original.call(instance, blockGetter, pos);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
		)
	)
	private static boolean checkCopperLens(BlockState instance, Block block, Operation<Boolean> original) {
		if (instance.getBlock() instanceof CopperLensBlock copperLens) {
			WanderingWizardry.LOGGER.info("{}. {}", instance.getValue(CopperLensBlock.AXIS), instance.getValue(CopperLensBlock.FOCUS));
			return !copperLens.shouldBlockBeacon(instance);
		}
		return original.call(instance, block);
	}

	@WrapOperation(
		method = "applyEffects",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"
		)
	)
	private static AABB focusArea(AABB instance, double amount, Operation<AABB> original, Level level, BlockPos pos) {
		var up = level.getBlockState(pos.above()).getBlock() instanceof CopperLensBlock;
		if (up)
			amount *= 0.5;
		return original.call(instance, amount);
	}

	@WrapOperation(
		method = "applyEffects",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
		)
	)
	private static boolean focusEffect(Player instance, MobEffectInstance mobEffectInstance, Operation<Boolean> original, Level level, BlockPos pos) {
		var up = level.getBlockState(pos.above()).getBlock() instanceof CopperLensBlock;
		if (up)
			mobEffectInstance = new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration(), mobEffectInstance.getAmplifier() + 1);
		return original.call(instance, mobEffectInstance);
	}
}
