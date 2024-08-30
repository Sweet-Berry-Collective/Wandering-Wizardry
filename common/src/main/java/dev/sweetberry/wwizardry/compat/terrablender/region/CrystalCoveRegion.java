package dev.sweetberry.wwizardry.compat.terrablender.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class CrystalCoveRegion extends Region {
	public static final CrystalCoveRegion INSTANCE = new CrystalCoveRegion();

	public CrystalCoveRegion() {
		super(WanderingWizardry.id("crystal_coves"), RegionType.OVERWORLD, 3);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addModifiedVanillaOverworldBiomes(mapper, builder -> {
			builder.replaceBiome(Biomes.DRIPSTONE_CAVES, WorldgenInitializer.CRYSTAL_COVE);
		});
	}
}
