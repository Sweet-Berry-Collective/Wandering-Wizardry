package dev.sweetberry.wwizardry.content.world.sapling;

import dev.sweetberry.wwizardry.WanderingWizardry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class BeeHoldingSaplingGenerator {
	public static TreeGrower create(
		String name,
		ResourceKey<ConfiguredFeature<?, ?>> noBees,
		@Nullable ResourceKey<ConfiguredFeature<?, ?>> bees
	) {
		return new TreeGrower(
			name,
			Optional.empty(),
			Optional.of(noBees),
			Optional.ofNullable(bees)
		);
	}

	public static TreeGrower create(String name, String bees, String noBees) {
		return create(name, getId(bees), getId(noBees));
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> getId(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, WanderingWizardry.id("tree/"+id));
	}
}
