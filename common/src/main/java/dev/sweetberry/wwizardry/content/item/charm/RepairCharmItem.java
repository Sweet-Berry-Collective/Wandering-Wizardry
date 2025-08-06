package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class RepairCharmItem extends AltarCharmItem {
	public RepairCharmItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		view.keepCenter();

		if (!AltarCraftable.ensureAllFilled(view))
			return false;

		// Damageable items are *probably* repairable.
		var toolSlot = AltarCraftable.ensureOneOfType(view, ItemStack::isDamageableItem);

		if (toolSlot.isEmpty())
			return false;

		var tool = Objects.requireNonNull(view.getItemInPedestal(toolSlot.get())).copy();

		var repairableSlots = AltarCraftable.getAllOfType(view, it -> tool.getItem().isValidRepairItem(tool, it));

		assert repairableSlots != null;

		for (var i : AltarRecipeView.AltarDirection.cardinals())
			if (i != toolSlot.get() && !WanderingWizardry.arrayContains(repairableSlots, i))
				view.setResultInPedestal(i, view.getItemInPedestal(i));

		for (var i : repairableSlots) {
			if (tool.getDamageValue() == 0) {
				view.setResultInPedestal(i, view.getItemInPedestal(i));
				continue;
			}

			var minDamage = Math.min(tool.getMaxDamage() / 4, tool.getDamageValue());

			var newDamage = tool.getDamageValue() - minDamage;

			if (newDamage < 0)
				newDamage = 0;

			tool.setDamageValue(newDamage);
		}

		view.setResultInPedestal(toolSlot.get(), tool);

		return true;
	}
}
