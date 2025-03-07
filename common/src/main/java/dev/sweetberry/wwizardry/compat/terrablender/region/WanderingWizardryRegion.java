package dev.sweetberry.wwizardry.compat.terrablender.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.world.level.biome.Biomes;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public class WanderingWizardryRegion extends Region {
	public static final WanderingWizardryRegion INSTANCE = new WanderingWizardryRegion();

	public WanderingWizardryRegion() {
		super(WanderingWizardry.id("biomes"), RegionType.OVERWORLD, 2);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addModifiedVanillaOverworldBiomes(mapper, builder -> {
			builder.replaceBiome(Biomes.DESERT, WorldgenInitializer.FUNGAL_FOREST);
			builder.replaceBiome(Biomes.DRIPSTONE_CAVES, WorldgenInitializer.CRYSTAL_COVE);
			builder.replaceBiome(Biomes.PLAINS, WorldgenInitializer.FORGOTTEN_FIELDS);
		});
	}
}
