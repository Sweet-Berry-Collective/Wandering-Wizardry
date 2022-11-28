package io.github.sweetberrycollective.wwizardry.mixin;

import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import net.minecraft.block.AbstractLichenBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenSpreadBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/block/SculkVeinBlock$SculkVeinSpreadBehavior"})
public class Mixin_SculkVeinSpreadBehavior extends LichenSpreadBehavior.DefaultLichenSpreadBehavior {
	public Mixin_SculkVeinSpreadBehavior(AbstractLichenBlock block) {
		super(block);
		throw new UnsupportedOperationException("Someone tried to instantiate a mixin!");
	}

	@Inject(method = "canPlace", at = @At("RETURN"), cancellable = true)
	private void checkAltar(BlockView view, BlockPos posA, BlockPos posB, Direction dir, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (view.getBlockState(posB.offset(dir)).contains(WanderingBlocks.SCULK_INFESTED)) {
			cir.setReturnValue(false);
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(BlockState state, BlockView view, BlockPos pos, Direction dir) {
		if (state.contains(WanderingBlocks.SCULK_INFESTED) && !state.get(WanderingBlocks.SCULK_INFESTED)) {
			return state.with(WanderingBlocks.SCULK_INFESTED, true).with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(view, pos.down()));
		}
		return super.getPlacementState(state, view, pos, dir);
	}

	@Override
	public boolean canSpreadTo(BlockView view, BlockPos pos, LichenSpreadBehavior.Placement placement) {
		var state = view.getBlockState(pos);
		return super.canSpreadTo(view, pos, placement) || (state.contains(WanderingBlocks.SCULK_INFESTED) && !state.get(WanderingBlocks.SCULK_INFESTED));
	}
}
