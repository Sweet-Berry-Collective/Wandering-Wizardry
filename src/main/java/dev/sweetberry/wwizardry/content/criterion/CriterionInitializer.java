package dev.sweetberry.wwizardry.content.criterion;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.advancement.criterion.AbstractCriterionTrigger;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

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
	<T extends AbstractCriterionTrigger.Conditions, C extends AbstractCriterionTrigger<T>>
	C register(String id, C criterion) {
		Registry.register(Registries.TRIGGER_TYPE, Mod.id(id), criterion);
		return criterion;
	}
}
