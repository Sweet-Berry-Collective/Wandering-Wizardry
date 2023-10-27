package dev.sweetberry.wwizardry.world.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.world.WanderingWorldgen;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.OverworldBiomeParameters;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

public class OverworldRegion extends Region {
	public static final OverworldRegion INSTANCE = new OverworldRegion();

	public OverworldRegion() {
		super(WanderingMod.id("overworld"), RegionType.OVERWORLD, 3);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
		addBiome(
			mapper,
			MultiNoiseUtil.createNoiseHypercube(
				MultiNoiseUtil.ParameterRange.of(0.55f, 0.75f),
				MultiNoiseUtil.ParameterRange.of(0.55f, 0.75f),
				MultiNoiseUtil.ParameterRange.of(0.55f, 0.75f),
				MultiNoiseUtil.ParameterRange.of(0.75f, 1.0f),
				MultiNoiseUtil.ParameterRange.of(0.55f, 1.0f),
				MultiNoiseUtil.ParameterRange.of(0.75f, 1.0f),
				1f
			),
			WanderingWorldgen.FORGOTTEN_FIELDS
		);

		addModifiedVanillaOverworldBiomes(mapper, builder -> {
			builder.replaceBiome(Biomes.DESERT, WanderingWorldgen.FUNGAL_FOREST);
		});
	}
}
