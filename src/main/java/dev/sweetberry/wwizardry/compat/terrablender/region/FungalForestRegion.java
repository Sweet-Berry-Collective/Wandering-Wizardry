package dev.sweetberry.wwizardry.compat.terrablender.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class FungalForestRegion extends Region {
	public static final FungalForestRegion INSTANCE = new FungalForestRegion();

	public FungalForestRegion() {
		super(Mod.id("fungal_forest"), RegionType.OVERWORLD, 1);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
		addModifiedVanillaOverworldBiomes(mapper, builder -> {
			builder.replaceBiome(Biomes.DESERT, WorldgenInitializer.FUNGAL_FOREST);
		});
	}
}