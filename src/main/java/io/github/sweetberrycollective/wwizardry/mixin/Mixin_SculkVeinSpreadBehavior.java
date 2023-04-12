package io.github.sweetberrycollective.wwizardry.mixin;

import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/block/SculkVeinBlock$SculkVeinSpreadBehavior"})
public class Mixin_SculkVeinSpreadBehavior {
	@Inject(method = "canPlace", at = @At("RETURN"), cancellable = true)
	private void checkAltar(BlockView view, BlockPos posA, BlockPos posB, Direction dir, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (view.getBlockState(posB.offset(dir)).contains(WanderingBlocks.SCULK_INFESTED)) {
			cir.setReturnValue(true);
		}
	}
}
