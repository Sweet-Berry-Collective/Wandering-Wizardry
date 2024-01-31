package dev.sweetberry.wwizardry.compat.terrablender;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import dev.sweetberry.wwizardry.compat.terrablender.region.ForgottenFieldsRegion;
import dev.sweetberry.wwizardry.compat.terrablender.region.FungalForestRegion;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class TerraBlenderInitializer implements TerraBlenderApi {
	@Override
	public void onTerraBlenderInitialized() {
		Regions.register(ForgottenFieldsRegion.INSTANCE);
		Regions.register(FungalForestRegion.INSTANCE);

		SurfaceRuleManager.addSurfaceRules(
			SurfaceRuleManager.RuleCategory.OVERWORLD,
			Mod.MODID,
			getMaterialRule()
		);
	}

	public static SurfaceRules.RuleSource getMaterialRule() {
		var aboveWater = SurfaceRules.waterBlockCheck(-1, 0);

		return SurfaceRules.ifTrue(
			SurfaceRules.isBiome(WorldgenInitializer.FUNGAL_FOREST),
			SurfaceRules.sequence(
				SurfaceRules.ifTrue(
					aboveWater,
					SurfaceRules.ifTrue(
						SurfaceRules.ON_FLOOR,
						SurfaceRules.state(BlockInitializer.MYCELIAL_SAND.defaultBlockState())
					)
				),
				SurfaceRules.ifTrue(
					SurfaceRules.ON_FLOOR,
					SurfaceRules.state(Blocks.SAND.defaultBlockState())
				),
				SurfaceRules.ifTrue(
					SurfaceRules.UNDER_FLOOR,
					SurfaceRules.state(Blocks.SAND.defaultBlockState())
				),
				SurfaceRules.ifTrue(
					SurfaceRules.DEEP_UNDER_FLOOR,
					SurfaceRules.state(Blocks.SANDSTONE.defaultBlockState())
				)
			)
		);
	}
}
