package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.recipe.RecipeHolder;
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
			var recipe = it.next().value();

			if (view.ingredientsMatch(recipe.getIngredients())) {
				view.setRecipeResult(recipe.getResult(world.getRegistryManager()));
				view.setAllAsRemainders();
				view.setBloom(5);
				return true;
			}
		}

		return false;
	}

	private Stream<RecipeHolder<ShapelessRecipe>> getRecipes(RecipeManager manager) {
		return manager.listAllOfType(RecipeType.CRAFTING)
			.stream()
			.filter(it -> it.value() instanceof ShapelessRecipe)
			.map(it -> (RecipeHolder<ShapelessRecipe>)(RecipeHolder<?>)it);
	}
}
