package dev.sweetberry.wwizardry.world;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.ModifiedVanillaOverworldBuilder;
import terrablender.api.ParameterUtils;

public class WanderingBiomes {
	private static final MultiNoiseUtil.ParameterRange FULL_RANGE = MultiNoiseUtil.ParameterRange.of(-1.0f, 1.0f);
	public static final RegistryKey<Biome> FORGOTTEN_FIELDS = key("forgotten_fields");

	public static RegistryKey<Biome> key(String path) {
		return RegistryKey.of(RegistryKeys.BIOME, WanderingMod.id(path));
	}

	public static void overworld(ModifiedVanillaOverworldBuilder map) {
		map.replaceBiome(Biomes.MEADOW, FORGOTTEN_FIELDS);
	}
}
