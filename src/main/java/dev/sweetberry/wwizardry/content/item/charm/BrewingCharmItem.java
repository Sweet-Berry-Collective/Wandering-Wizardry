package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView.AltarDirection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;

public class BrewingCharmItem extends AltarCharmItem {
	public BrewingCharmItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		AltarRecipeView.AltarDirection ingredientSlot = null;
		ItemStack ingredientStack = null;
		for (var dir : AltarRecipeView.AltarDirection.cardinals()) {
			var stack = view.getItemInPedestal(dir);
			if (stack != null && PotionBrewing.isIngredient(stack)) {
				ingredientSlot = dir;
				ingredientStack = stack;
				break;
			}
		}
		if (ingredientSlot == null)
			return false;
		boolean used = false;
		view.setResultInPedestal(ingredientSlot, ItemStack.EMPTY);
		for (var dir : AltarRecipeView.AltarDirection.cardinalWithout(ingredientSlot)) {
			var stack = view.getItemInPedestal(dir);
			if (stack == null)
				continue;
			if (PotionBrewing.hasMix(stack, ingredientStack)) {
				used = true;
				view.setResultInPedestal(dir, PotionBrewing.mix(ingredientStack, stack));
			} else {
				view.setResultInPedestal(dir, stack);
			}
		}
		view.keepCenter();
		if (used)
			view.setBloom(5);
		return used;
	}
}
