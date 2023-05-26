package dev.sweetberry.wwizardry.world.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.world.WanderingBiomes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class OverworldRegion extends Region {
	public static final OverworldRegion INSTANCE = new OverworldRegion();

	public OverworldRegion() {
		super(WanderingMod.id("overworld"), RegionType.OVERWORLD, 3);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
		addModifiedVanillaOverworldBiomes(mapper, WanderingBiomes::overworld);
	}
}