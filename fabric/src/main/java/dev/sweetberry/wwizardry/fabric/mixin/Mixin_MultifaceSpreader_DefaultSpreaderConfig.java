package dev.sweetberry.wwizardry.fabric.mixin;

import dev.sweetberry.wwizardry.content.block.Sculkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.state.BlockState;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultifaceSpreader.DefaultSpreaderConfig.class)
public class Mixin_MultifaceSpreader_DefaultSpreaderConfig {
	@Shadow
	protected MultifaceBlock block;

	@Inject(at = @At("HEAD"), method = "stateCanBeReplaced", cancellable = true)
	private void wwizardry$checkAltarPlace(BlockGetter view, BlockPos posA, BlockPos posB, Direction dir, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		if (!(state.getBlock() instanceof Sculkable))
			return;
		cir.setReturnValue(true);
	}

	@Inject(at = @At("HEAD"), method = "canSpreadInto", cancellable = true)
	private void wwizardry$checkAlterSpread(BlockGetter view, BlockPos pos, MultifaceSpreader.SpreadPos placement, CallbackInfoReturnable<Boolean> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		var state = view.getBlockState(placement.pos());
		if (!(state.getBlock() instanceof Sculkable))
			return;
		cir.setReturnValue(!state.getValue(BlockInitializer.SCULK_INFESTED));
	}

	@Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
	private void wwizardry$getAltarPlacement(BlockState state, BlockGetter view, BlockPos pos, Direction dir, CallbackInfoReturnable<BlockState> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		var trueState = view.getBlockState(pos);
		if (!(trueState.getBlock() instanceof Sculkable))
			return;
		cir.setReturnValue(trueState.setValue(BlockInitializer.SCULK_INFESTED, true));
	}
}
