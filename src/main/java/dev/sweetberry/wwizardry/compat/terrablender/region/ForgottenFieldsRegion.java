package dev.sweetberry.wwizardry.compat.terrablender.region;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public class ForgottenFieldsRegion extends Region {
	public static final ForgottenFieldsRegion INSTANCE = new ForgottenFieldsRegion();

	public ForgottenFieldsRegion() {
		super(Mod.id("forgotten_fields"), RegionType.OVERWORLD, 1);
	}

	@Override
	public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addBiome(
			mapper,
			Climate.parameters(
				Climate.Parameter.span(0.55f, 0.75f),
				Climate.Parameter.span(0.55f, 0.75f),
				Climate.Parameter.span(0.55f, 0.75f),
				Climate.Parameter.span(0.75f, 1.0f),
				Climate.Parameter.span(0.55f, 1.0f),
				Climate.Parameter.span(0.75f, 1.0f),
				1f
			),
			WorldgenInitializer.FORGOTTEN_FIELDS
		);
	}
}
