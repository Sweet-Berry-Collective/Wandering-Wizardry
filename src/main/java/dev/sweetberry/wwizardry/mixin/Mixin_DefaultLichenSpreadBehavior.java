package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.block.AltarBlock;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import net.minecraft.block.AbstractLichenBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenSpreadBehavior;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LichenSpreadBehavior.DefaultLichenSpreadBehavior.class)
public class Mixin_DefaultLichenSpreadBehavior {
	@Shadow
	protected AbstractLichenBlock block;

	@Inject(at = @At("HEAD"), method = "canPlace", cancellable = true)
	private void wwizardry$checkAltarPlace(BlockView view, BlockPos posA, BlockPos posB, Direction dir, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		if (!(state.getBlock() instanceof AltarBlock<?>))
			return;
		cir.setReturnValue(true);
	}

	@Inject(at = @At("HEAD"), method = "canSpreadTo", cancellable = true)
	private void wwizardry$checkAlterSpread(BlockView view, BlockPos pos, LichenSpreadBehavior.Placement placement, CallbackInfoReturnable<Boolean> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		var state = view.getBlockState(placement.pos());
		if (!(state.getBlock() instanceof AltarBlock<?>))
			return;
		cir.setReturnValue(!state.get(WanderingBlocks.SCULK_INFESTED));
	}

	@Inject(at = @At("HEAD"), method = "getPlacementState", cancellable = true)
	private void wwizardry$getAltarPlacement(BlockState state, BlockView view, BlockPos pos, Direction dir, CallbackInfoReturnable<BlockState> cir) {
		if (!(block instanceof SculkVeinBlock))
			return;
		var trueState = view.getBlockState(pos);
		if (!(trueState.getBlock() instanceof AltarBlock<?>))
			return;
		cir.setReturnValue(trueState.with(WanderingBlocks.SCULK_INFESTED, true));
	}
}
