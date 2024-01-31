package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.world.processors.WaterLoggingFixProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplate.class)
public class Mixin_StructureTemplate {
	@Inject(
		method = "placeInWorld",
		at = @At("HEAD")
	)
	private void wwizardry$fixWaterLogging(ServerLevelAccessor world, BlockPos pos, BlockPos pivot, StructurePlaceSettings placementData, RandomSource random, int flags, CallbackInfoReturnable<Boolean> cir) {
		if (placementData.getProcessors().stream().allMatch(
			it -> ((Accessor_StructureProcessor)it).callGetType() == WaterLoggingFixProcessor.INSTANCE
		))
			placementData.setKeepLiquids(false);
	}
}
