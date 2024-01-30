package dev.sweetberry.wwizardry.content.world.sapling;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.block.WoodTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BeeHoldingSaplingGenerator {
	public static WoodTypes create(
		String name,
		RegistryKey<ConfiguredFeature<?, ?>> noBees,
		@Nullable RegistryKey<ConfiguredFeature<?, ?>> bees
	) {
		return new WoodTypes(
			name,
			Optional.empty(),
			Optional.of(noBees),
			Optional.ofNullable(bees)
		);
	}

	public static WoodTypes create(String name, String bees, String noBees) {
		return create(name, getId(bees), getId(noBees));
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> getId(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Mod.id("tree/"+id));
	}
}
