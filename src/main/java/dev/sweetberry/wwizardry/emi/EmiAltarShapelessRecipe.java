package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.item.WanderingItems;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EmiAltarShapelessRecipe(Identifier id, List<EmiIngredient> input, EmiStack output) implements EmiAltarRecipe {
	public static EmiAltarShapelessRecipe of(Identifier id, ShapelessRecipe recipe) {
		var ingredients = recipe.getIngredients();
		var output = recipe.getResult(null);

		var inputs = DefaultedList.ofSize(4, (EmiIngredient)EmiStack.of(WanderingItems.SLOT_CHARM));
		for (var i = 0; i < ingredients.size(); i++) {
			inputs.set(i, EmiIngredient.of(ingredients.get(i)));
		}

		return new EmiAltarShapelessRecipe(WanderingMod.id(EmiInitializer.getPrefixedPathedIdentifier(id, "altar_crafting_shapeless")), inputs, EmiStack.of(output.copy()));
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EmiInitializer.SHAPELESS;
	}

	@Override
	public @Nullable Identifier getId() {
		return id();
	}

	@Override
	public List<EmiIngredient> getOuterIngredients() {
		return input;
	}

	@Override
	public EmiIngredient getInnerIngredient() {
		return EmiStack.of(WanderingItems.CRAFTING_CHARM);
	}

	@Override
	public EmiStack getOutput() {
		return output;
	}

	@Override
	public boolean shouldKeepCatalyst() {
		return true;
	}
}
