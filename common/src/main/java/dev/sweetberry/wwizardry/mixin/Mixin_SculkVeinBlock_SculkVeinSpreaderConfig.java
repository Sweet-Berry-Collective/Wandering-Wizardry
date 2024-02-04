package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.block.Sculkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/world/level/block/SculkVeinBlock$SculkVeinSpreaderConfig"})
public class Mixin_SculkVeinBlock_SculkVeinSpreaderConfig {
	@Inject(method = "stateCanBeReplaced", at = @At("RETURN"), cancellable = true)
	private void checkAltar(BlockGetter view, BlockPos posA, BlockPos posB, Direction dir, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (view.getBlockState(posB.relative(dir)).hasProperty(Sculkable.SCULK_INFESTED)) {
			cir.setReturnValue(true);
		}
	}
}
