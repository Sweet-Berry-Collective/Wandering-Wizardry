package dev.sweetberry.wwizardry.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.compat.emi.EmiInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EmiAltarBrewingRecipe(
	EmiIngredient inputPotion,
	EmiIngredient inputIngredient,
	EmiStack outputPotion,
	Identifier id
) implements EmiAltarRecipe {
	@Override
	public List<EmiIngredient> getOuterIngredients() {
		var empty = EmiStack.of(ItemInitializer.SLOT_CHARM);
		return List.of(inputIngredient, inputPotion, empty, empty);
	}

	@Override
	public EmiIngredient getInnerIngredient() {
		return EmiStack.of(ItemInitializer.BREWING_CHARM);
	}

	@Override
	public EmiStack getOutput() {
		return outputPotion;
	}

	@Override
	public boolean shouldKeepCatalyst() {
		return true;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EmiInitializer.BREWING;
	}

	@Override
	public @Nullable Identifier getId() {
		return id;
	}
}
