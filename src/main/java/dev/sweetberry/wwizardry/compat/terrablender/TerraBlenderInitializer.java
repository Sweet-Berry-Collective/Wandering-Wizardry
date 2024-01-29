package dev.sweetberry.wwizardry.compat.terrablender;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import dev.sweetberry.wwizardry.compat.terrablender.region.ForgottenFieldsRegion;
import dev.sweetberry.wwizardry.compat.terrablender.region.FungalForestRegion;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
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

	public static SurfaceRules.MaterialRule getMaterialRule() {
		var aboveWater = SurfaceRules.water(-1, 0);

		return SurfaceRules.condition(
			SurfaceRules.biome(WorldgenInitializer.FUNGAL_FOREST),
			SurfaceRules.sequence(
				SurfaceRules.condition(
					aboveWater,
					SurfaceRules.condition(
						SurfaceRules.ON_FLOOR,
						SurfaceRules.block(BlockInitializer.MYCELIAL_SAND.getDefaultState())
					)
				),
				SurfaceRules.condition(
					SurfaceRules.ON_FLOOR,
					SurfaceRules.block(Blocks.SAND.getDefaultState())
				),
				SurfaceRules.condition(
					SurfaceRules.UNDER_FLOOR,
					SurfaceRules.block(Blocks.SAND.getDefaultState())
				),
				SurfaceRules.condition(
					SurfaceRules.DEEP_UNDER_FLOOR,
					SurfaceRules.block(Blocks.SANDSTONE.getDefaultState())
				)
			)
		);
	}
}
