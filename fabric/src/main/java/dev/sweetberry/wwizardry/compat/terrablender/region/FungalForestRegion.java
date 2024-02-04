package dev.sweetberry.wwizardry.compat.terrablender.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

public class FungalForestRegion extends Region {
	public static final FungalForestRegion INSTANCE = new FungalForestRegion();

	public FungalForestRegion() {
		super(WanderingWizardry.id("fungal_forest"), RegionType.OVERWORLD, 1);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addModifiedVanillaOverworldBiomes(mapper, builder -> {
			builder.replaceBiome(Biomes.DESERT, WorldgenInitializer.FUNGAL_FOREST);
		});
	}
}
