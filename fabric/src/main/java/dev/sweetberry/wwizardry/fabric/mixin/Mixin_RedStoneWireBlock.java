package dev.sweetberry.wwizardry.fabric.mixin;

import dev.sweetberry.wwizardry.content.block.redstone.LogicGateBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class Mixin_RedStoneWireBlock {
	@Inject(at = @At("HEAD"), method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", cancellable = true)
	private static void wwizardry$connectsTo(BlockState state, Direction dir, CallbackInfoReturnable<Boolean> cir) {
		if (!(state.getBlock() instanceof LogicGateBlock logicGateBlock)) return;
		if (logicGateBlock.inputType == LogicGateBlock.SideInput.ALL) return;
		Direction direction = state.getValue(LogicGateBlock.FACING);
		cir.setReturnValue(direction == dir || direction.getOpposite() == dir);
	}
}
