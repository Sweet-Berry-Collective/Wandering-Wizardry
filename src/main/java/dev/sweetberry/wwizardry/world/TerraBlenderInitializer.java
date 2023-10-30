package dev.sweetberry.wwizardry.world;

import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.world.region.ForgottenFieldsRegion;
import dev.sweetberry.wwizardry.world.region.FungalForestRegion;
import net.minecraft.block.Blocks;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
import org.quiltmc.qsl.worldgen.biome.api.*;
import org.quiltmc.qsl.worldgen.surface_rule.api.SurfaceRuleEvents;
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
			WanderingMod.MODID,
			getMaterialRule()
		);
	}

	public static SurfaceRules.MaterialRule getMaterialRule() {
		var aboveWater = SurfaceRules.water(-1, 0);

		return SurfaceRules.condition(
			SurfaceRules.biome(WanderingWorldgen.FUNGAL_FOREST),
			SurfaceRules.sequence(
				SurfaceRules.condition(
					aboveWater,
					SurfaceRules.condition(
						SurfaceRules.ON_FLOOR,
						SurfaceRules.block(WanderingBlocks.MYCELIAL_SAND.getDefaultState())
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
