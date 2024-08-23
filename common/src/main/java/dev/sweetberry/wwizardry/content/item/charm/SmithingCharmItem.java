package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class SmithingCharmItem extends AltarCharmItem{
	public SmithingCharmItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		var recipes = getRecipes(world.getRecipeManager());

		AltarRecipeView.AltarDirection templateDirection = null;
		ItemStack templateStack = null;

		for (var dir : AltarRecipeView.AltarDirection.cardinals()) {
			var stack = view.getItemInPedestal(dir);
			if (stack == null)
				continue;
			if (stack.getItem() instanceof SmithingTemplateItem smithingTemplate) {
				templateStack = stack;
				templateDirection = dir;
				break;
			}
		}
		if (templateStack == null)
			return false;
		var finalTemplateStack = templateStack;
		var matchingRecipes = recipes.filter(
			it -> it.value().isTemplateIngredient(finalTemplateStack)
		).toList();

		ItemStack additionStack = null;
		ItemStack baseStack = null;
		AltarRecipeView.AltarDirection baseDirection = null;

		for (var dir : AltarRecipeView.AltarDirection.cardinalWithout(templateDirection)) {
			var stack = view.getItemInPedestal(dir);
			if (stack == null)
				continue;
			if (matchingRecipes.stream().anyMatch(it -> it.value().isAdditionIngredient(stack)) && additionStack == null) {
				additionStack = stack;
			} else if (matchingRecipes.stream().anyMatch(it -> it.value().isBaseIngredient(stack)) && baseStack == null) {
				baseStack = stack;
				baseDirection = dir;
			} else {
				view.setResultInPedestal(dir, stack);
			}
		}
		if (additionStack == null || baseStack == null || baseDirection == null)
			return false;
		var finalAdditionStack = additionStack;
		var finalBaseStack = baseStack;
		var maybeRecipe = matchingRecipes.stream()
			.filter(
				it ->
					it.value().isAdditionIngredient(finalAdditionStack)
					&& it.value().isBaseIngredient(finalBaseStack)
			).findFirst();
		if (maybeRecipe.isEmpty())
			return false;
		var recipe = maybeRecipe.get().value();
		var input = new SmithingRecipeInput(templateStack, baseStack, additionStack);
		view.keepCenter();
		view.setResultInPedestal(baseDirection, recipe.assemble(input, world.registryAccess()));

		return true;
	}

	private Stream<RecipeHolder<SmithingRecipe>> getRecipes(RecipeManager manager) {
		return manager.getAllRecipesFor(RecipeType.SMITHING)
			.stream();
	}
}
