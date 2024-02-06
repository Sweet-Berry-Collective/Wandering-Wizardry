package dev.sweetberry.wwizardry.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.compat.emi.EmiInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.recipe.AltarCatalyzationRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

public record EmiAltarCatalyzationRecipe(ResourceLocation id, List<EmiIngredient> input, EmiIngredient catalyst, boolean keepCatalyst, int bloom, EmiStack output) implements EmiAltarRecipe {
	public static EmiAltarCatalyzationRecipe of(ResourceLocation id, AltarCatalyzationRecipe recipe) {
		var inputs = NonNullList.withSize(4, (EmiIngredient)EmiStack.of(ItemInitializer.SLOT_CHARM.get()));

		for (var i = 0; i < recipe.inputs().size(); i++) {
			inputs.set(i, EmiIngredient.of(recipe.inputs().get(i)));
		}

		return new EmiAltarCatalyzationRecipe(id, inputs, EmiIngredient.of(recipe.catalyst()), recipe.keepCatalyst(), recipe.bloom(), EmiStack.of(recipe.result()));
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EmiInitializer.BASE;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return id();
	}

	@Override
	public List<EmiIngredient> getOuterIngredients() {
		return input;
	}

	@Override
	public EmiIngredient getInnerIngredient() {
		return catalyst;
	}

	@Override
	public EmiStack getOutput() {
		return output;
	}

	@Override
	public boolean shouldKeepCatalyst() {
		return keepCatalyst;
	}
}
