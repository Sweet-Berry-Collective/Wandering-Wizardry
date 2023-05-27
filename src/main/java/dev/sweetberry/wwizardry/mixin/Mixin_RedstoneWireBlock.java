package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.block.LogicGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public class Mixin_RedstoneWireBlock {
	@Inject(at = @At("HEAD"), method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
	private static void wwizardry$connectsTo(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
		if (!(state.getBlock() instanceof LogicGateBlock logicGateBlock)) return;
		if (logicGateBlock.inputType == LogicGateBlock.SideInput.ALL) return;
		Direction direction = state.get(LogicGateBlock.FACING);
		cir.setReturnValue(direction == dir || direction.getOpposite() == dir);
	}
}
