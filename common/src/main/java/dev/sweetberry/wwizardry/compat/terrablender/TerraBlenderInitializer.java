package dev.sweetberry.wwizardry.compat.terrablender;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import dev.sweetberry.wwizardry.compat.terrablender.region.ForgottenFieldsRegion;
import dev.sweetberry.wwizardry.compat.terrablender.region.FungalForestRegion;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class TerraBlenderInitializer {
	public static void init() {
		Regions.register(ForgottenFieldsRegion.INSTANCE);
		Regions.register(FungalForestRegion.INSTANCE);

		SurfaceRuleManager.addSurfaceRules(
			SurfaceRuleManager.RuleCategory.OVERWORLD,
			WanderingWizardry.MODID,
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
						SurfaceRules.state(BlockInitializer.MYCELIAL_SAND.get().defaultBlockState())
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
