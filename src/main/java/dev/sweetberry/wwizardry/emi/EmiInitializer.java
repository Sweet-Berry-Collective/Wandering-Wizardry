package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.item.WanderingItems;
import dev.sweetberry.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;

public class EmiInitializer implements EmiPlugin {
	public static final EmiStack BASE_ICON = EmiStack.of(WanderingItems.CRYSTALLINE_SCULK_SHARD);
	public static final EmiRecipeCategory BASE = new EmiRecipeCategory(
		WanderingMod.id("altar_catalyzation"),
		BASE_ICON
	);

	public static final EmiStack SHAPELESS_ICON = EmiStack.of(WanderingItems.CRAFTING_CHARM);
	public static final EmiRecipeCategory SHAPELESS = new EmiRecipeCategory(
		WanderingMod.id("altar_shapeless"),
		SHAPELESS_ICON
	);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(BASE);
		registry.addCategory(SHAPELESS);

		var manager = registry.getRecipeManager();

		for (var recipe : manager.listAllOfType(AltarCatalyzationRecipe.TYPE)) {
			registry.addRecipe(EmiAltarRecipe.of(recipe));
		}

		for (
			var recipe :
			manager
				.listAllOfType(RecipeType.CRAFTING)
				.stream()
				.filter(it -> it instanceof ShapelessRecipe)
				.map(it -> (ShapelessRecipe) it)
				.filter(it -> it.getIngredients().size() <= 4)
				.toList()
		) {
			registry.addRecipe(
				EmiAltarShapelessRecipe.of(
					recipe,
					recipe.getIngredients(),
					recipe.getResult(null)
				)
			);
		}
	}
}
