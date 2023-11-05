package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.world.World;

public class BrewingCharmItem extends AltarCharmItem {
	public BrewingCharmItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, World world) {
		AltarRecipeView.AltarDirection ingredientSlot = null;
		ItemStack ingredientStack = null;
		for (var dir : AltarRecipeView.AltarDirection.cardinals()) {
			var stack = view.getItemInPedestal(dir);
			if (stack != null && BrewingRecipeRegistry.isValidIngredient(stack)) {
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
			if (BrewingRecipeRegistry.hasRecipe(stack, ingredientStack)) {
				used = true;
				view.setResultInPedestal(dir, BrewingRecipeRegistry.craft(ingredientStack, stack));
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
