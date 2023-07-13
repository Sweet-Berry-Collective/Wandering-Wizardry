package dev.sweetberry.wwizardry.world;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;
import org.quiltmc.qsl.worldgen.biome.api.ModificationPhase;
import terrablender.api.ModifiedVanillaOverworldBuilder;

public class WanderingWorldgen {
	private static final MultiNoiseUtil.ParameterRange FULL_RANGE = MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f);
	public static final RegistryKey<Biome> FORGOTTEN_FIELDS = key("forgotten_fields");
	public static final RegistryKey<PlacedFeature> ROSE_QUARTZ = RegistryKey.of(RegistryKeys.PLACED_FEATURE, WanderingMod.id("ore/rose_quartz"));

	public static RegistryKey<Biome> key(String path) {
		return RegistryKey.of(RegistryKeys.BIOME, WanderingMod.id(path));
	}

	public static void init() {
		var modification = BiomeModifications.create(WanderingMod.id("modifications"));

		modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), (ctx, modifications) -> {
			modifications.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ROSE_QUARTZ);
		});
	}
}
