package dev.sweetberry.wwizardry.content.world;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.processors.WaterLoggingFixProcessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class WorldgenInitializer {
	public static final ResourceKey<Biome> FORGOTTEN_FIELDS = key("forgotten_fields");

	public static final ResourceKey<Biome> FUNGAL_FOREST = key("fungal_forest");

	public static final ResourceKey<PlacedFeature> ROSE_QUARTZ = ResourceKey.create(Registries.PLACED_FEATURE, Mod.id("ore/rose_quartz"));

	public static ResourceKey<Biome> key(String path) {
		return ResourceKey.create(Registries.BIOME, Mod.id(path));
	}

	public static void init() {
		registerStructureProcessor(WaterLoggingFixProcessor.INSTANCE, "water_logging_fix");

		var modification = BiomeModifications.create(Mod.id("modifications"));

		modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), (ctx, modifications) -> {
			modifications.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ROSE_QUARTZ);
		});
	}

	private static <T extends StructureProcessor> StructureProcessorType<T> registerStructureProcessor(StructureProcessorType<T> type, String id) {
		return Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, Mod.id(id), type);
	}
}
