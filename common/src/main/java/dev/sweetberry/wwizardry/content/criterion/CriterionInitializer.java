package dev.sweetberry.wwizardry.content.criterion;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class CriterionInitializer {
	public static final RegistryContext<CriterionTrigger<?>> CRITERION = new RegistryContext<>(BuiltInRegistries.TRIGGER_TYPES);

	public static final Lazy<SimpleTriggerCriterion> LODESTONE_MIRROR = simple(
		"use_mirror_on_lodestone"
	);

	public static final Lazy<SimpleTriggerCriterion> COMPLETE_ALTAR = simple(
		"complete_altar"
	);

	public static final Lazy<SimpleTriggerCriterion> ALTAR_END_CRYSTAL = simple(
		"place_end_crystal_in_altar"
	);

	public static Lazy<SimpleTriggerCriterion> simple(String id) {
		return register(id, SimpleTriggerCriterion::new);
	}

	public static
	<T extends SimpleCriterionTrigger.SimpleInstance, C extends SimpleCriterionTrigger<T>>
	Lazy<C> register(String id, Supplier<C> criterion) {
		return (Lazy<C>)(Object) CRITERION.register(WanderingWizardry.id(id), (Supplier<CriterionTrigger<?>>)(Object) criterion);
	}
}
