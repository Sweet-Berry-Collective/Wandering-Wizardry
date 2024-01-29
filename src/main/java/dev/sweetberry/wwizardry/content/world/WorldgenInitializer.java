package dev.sweetberry.wwizardry.content.world;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.processors.WaterLoggingFixProcessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;
import org.quiltmc.qsl.worldgen.biome.api.ModificationPhase;

public class WorldgenInitializer {
	public static final RegistryKey<Biome> FORGOTTEN_FIELDS = key("forgotten_fields");

	public static final RegistryKey<Biome> FUNGAL_FOREST = key("fungal_forest");

	public static final RegistryKey<PlacedFeature> ROSE_QUARTZ = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Mod.id("ore/rose_quartz"));

	public static RegistryKey<Biome> key(String path) {
		return RegistryKey.of(RegistryKeys.BIOME, Mod.id(path));
	}

	public static void init() {
		registerStructureProcessor(WaterLoggingFixProcessor.INSTANCE, "water_logging_fix");

		var modification = BiomeModifications.create(Mod.id("modifications"));

		modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), (ctx, modifications) -> {
			modifications.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, ROSE_QUARTZ);
		});
	}

	private static <T extends StructureProcessor> StructureProcessorType<T> registerStructureProcessor(StructureProcessorType<T> type, String id) {
		return Registry.register(Registries.STRUCTURE_PROCESSOR_TYPE, Mod.id(id), type);
	}
}
