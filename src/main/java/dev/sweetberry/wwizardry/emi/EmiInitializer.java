package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.item.WanderingItems;
import dev.sweetberry.wwizardry.recipe.AltarCatalyzationRecipe;

public class EmiInitializer implements EmiPlugin {
	public static final EmiStack icon = EmiStack.of(WanderingItems.CRYSTALLINE_SCULK_SHARD);
	public static final EmiRecipeCategory category = new EmiRecipeCategory(
		WanderingMod.id("altar_catalyzation"),
		icon
	);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(category);

		var manager = registry.getRecipeManager();

		for (var recipe : manager.listAllOfType(AltarCatalyzationRecipe.TYPE)) {
			registry.addRecipe(EmiAltarRecipe.of(recipe));
		}
	}
}
