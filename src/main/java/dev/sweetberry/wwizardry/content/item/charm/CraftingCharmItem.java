package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import java.util.stream.Stream;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class CraftingCharmItem extends AltarCharmItem {
	public CraftingCharmItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		var recipes = getRecipes(world.getRecipeManager());
		for (var it = recipes.iterator(); it.hasNext();) {
			var recipe = it.next().value();

			if (view.ingredientsMatch(recipe.getIngredients())) {
				view.setRecipeResult(recipe.getResultItem(world.registryAccess()));
				view.setAllAsRemainders();
				view.setBloom(5);
				return true;
			}
		}

		return false;
	}

	private Stream<RecipeHolder<ShapelessRecipe>> getRecipes(RecipeManager manager) {
		return manager.getAllRecipesFor(RecipeType.CRAFTING)
			.stream()
			.filter(it -> it.value() instanceof ShapelessRecipe)
			.map(it -> (RecipeHolder<ShapelessRecipe>)(RecipeHolder<?>)it);
	}
}
