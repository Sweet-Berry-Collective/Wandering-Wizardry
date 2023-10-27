package dev.sweetberry.wwizardry.world;

import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.world.region.OverworldRegion;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModification;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.surface_rule.api.SurfaceRuleEvents;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class TerraBlenderInitializer implements TerraBlenderApi {
	@Override
	public void onTerraBlenderInitialized() {
		Regions.register(OverworldRegion.INSTANCE);

		var aboveWater = SurfaceRules.water(-1, 0);

		SurfaceRuleManager.addSurfaceRules(
			SurfaceRuleManager.RuleCategory.OVERWORLD,
			WanderingMod.MODID,
			SurfaceRules.condition(
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
						SurfaceRules.stoneDepth(-2,true, VerticalSurfaceType.FLOOR),
						SurfaceRules.block(Blocks.SANDSTONE.getDefaultState())
					),
					SurfaceRules.condition(
						SurfaceRules.UNDER_FLOOR,
						SurfaceRules.block(Blocks.SAND.getDefaultState())
					)
				)
			)
		);
	}
}
