package dev.sweetberry.wwizardry.content.criterion;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class CriterionInitializer {
	public static final SimpleTriggerCriterion LODESTONE_MIRROR = simple(
		"use_mirror_on_lodestone"
	);

	public static final SimpleTriggerCriterion COMPLETE_ALTAR = simple(
		"complete_altar"
	);

	public static final SimpleTriggerCriterion ALTAR_END_CRYSTAL = simple(
		"place_end_crystal_in_altar"
	);

	public static void init() {}

	public static SimpleTriggerCriterion simple(String id) {
		return register(id, new SimpleTriggerCriterion());
	}

	public static
	<T extends SimpleCriterionTrigger.SimpleInstance, C extends SimpleCriterionTrigger<T>>
	C register(String id, C criterion) {
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, Mod.id(id), criterion);
		return criterion;
	}
}
