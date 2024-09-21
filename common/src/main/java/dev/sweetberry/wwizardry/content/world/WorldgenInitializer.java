package dev.sweetberry.wwizardry.content.world;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.world.feature.CrystalShardFeature;
import dev.sweetberry.wwizardry.content.world.processor.WaterLoggingFixProcessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class WorldgenInitializer {
	public static final RegistryContext<StructureProcessorType<?>> STRUCTURE_PROCESSORS = new RegistryContext<>(BuiltInRegistries.STRUCTURE_PROCESSOR);
	public static final RegistryContext<Feature<?>> FEATURES = new RegistryContext<>(BuiltInRegistries.FEATURE);

	public static final ResourceKey<Biome> FORGOTTEN_FIELDS = key("forgotten_fields");

	public static final ResourceKey<Biome> FUNGAL_FOREST = key("fungal_forest");

	public static final ResourceKey<Biome> CRYSTAL_COVE = key("crystal_cove");

	public static final ResourceKey<PlacedFeature> ROSE_QUARTZ = ResourceKey.create(Registries.PLACED_FEATURE, WanderingWizardry.id("ore/rose_quartz"));

	public static final Map<GenerationStep.Decoration, Set<ResourceKey<PlacedFeature>>> OVERWORLD_MODIFICATIONS = new HashMap<>();

	public static final Lazy<StructureProcessorType<WaterLoggingFixProcessor>> WATER_LOGGING_FIX = registerStructureProcessor(
		"water_logging_fix",
		() -> () -> WaterLoggingFixProcessor.CODEC
	);

	public static final Lazy<CrystalShardFeature> CRYSTAL_SHARD = registerFeature("crystal_shard",
		() -> new CrystalShardFeature(
			CrystalShardFeature.Config.CODEC
		)
	);

	public static ResourceKey<Biome> key(String path) {
		return ResourceKey.create(Registries.BIOME, WanderingWizardry.id(path));
	}
	public static void addModification(Map<GenerationStep.Decoration, Set<ResourceKey<PlacedFeature>>> modifications, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
		if (!modifications.containsKey(step))
			modifications.put(step, new HashSet<>());
		modifications.get(step).add(feature);
	}

	public static void addOverworldModification(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
		addModification(OVERWORLD_MODIFICATIONS, step, feature);
	}

	public static void init() {
		addOverworldModification(GenerationStep.Decoration.UNDERGROUND_ORES, ROSE_QUARTZ);
	}

	@SuppressWarnings("unchecked")
	private static <T extends StructureProcessor> Lazy<StructureProcessorType<T>> registerStructureProcessor(String id, Supplier<StructureProcessorType<T>> type) {
		return (Lazy<StructureProcessorType<T>>)(Object) STRUCTURE_PROCESSORS.register(WanderingWizardry.id(id), (Supplier<StructureProcessorType<?>>)(Object) type);
	}

	@SuppressWarnings("unchecked")
	private static <F extends Feature<FC>, FC extends FeatureConfiguration> Lazy<F> registerFeature(String id, Supplier<F> feature) {
		return (Lazy<F>) FEATURES.register(WanderingWizardry.id(id), (Supplier<Feature<?>>) feature);
	}
}
