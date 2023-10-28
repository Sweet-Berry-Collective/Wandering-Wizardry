package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.api.AltarRecipeView;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class CraftingCharmItem extends AltarCharmItem {
	public CraftingCharmItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, World world) {
		var recipes = getRecipes(world.getRecipeManager());
		for (var it = recipes.iterator(); it.hasNext();) {
			var recipe = it.next();

			if (view.ingredientsMatch(recipe.getIngredients())) {
				view.setRecipeResult(recipe.getResult(world.getRegistryManager()));
				view.setAllAsRemainders();
				return true;
			}
		}

		return false;
	}

	private Stream<ShapelessRecipe> getRecipes(RecipeManager manager) {
		return manager.listAllOfType(RecipeType.CRAFTING)
			.stream()
			.filter(it -> it instanceof ShapelessRecipe)
			.map(it -> (ShapelessRecipe)it);
	}
}