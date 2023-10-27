package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.world.processors.WaterLoggingFixProcessor;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Structure.class)
public class Mixin_Structure {
	@Inject(
		method = "place",
		at = @At("HEAD")
	)
	private void wwizardry$fixWaterLogging(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, RandomGenerator random, int flags, CallbackInfoReturnable<Boolean> cir) {
		if (placementData.getProcessors().stream().allMatch(
			it -> ((Accessor_StructureProcessor)it).callGetType() == WaterLoggingFixProcessor.INSTANCE
		))
			placementData.setPlaceFluids(false);
	}
}
