package dev.sweetberry.wwizardry.content.world.sapling;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class BeeHoldingSaplingGenerator extends SaplingGenerator {
	public final @Nullable RegistryKey<ConfiguredFeature<?, ?>> BEES;
	public final RegistryKey<ConfiguredFeature<?, ?>> NO_BEES;

	public BeeHoldingSaplingGenerator(RegistryKey<ConfiguredFeature<?, ?>> noBees, @Nullable RegistryKey<ConfiguredFeature<?, ?>> bees) {
		BEES = bees;
		NO_BEES = noBees;
	}

	public BeeHoldingSaplingGenerator(String noBees, @Nullable String bees) {
		this(getId(noBees), bees == null ? null : getId(bees));
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> getId(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Mod.id("tree/"+id));
	}

	@Nullable
	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(RandomGenerator random, boolean bees) {
		return bees && BEES != null ? BEES : NO_BEES;
	}
}
