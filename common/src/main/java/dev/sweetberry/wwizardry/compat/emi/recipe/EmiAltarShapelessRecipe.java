package dev.sweetberry.wwizardry.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.compat.emi.EmiInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public record EmiAltarShapelessRecipe(ResourceLocation id, List<EmiIngredient> input, EmiStack output) implements EmiAltarRecipe {
	public static EmiAltarShapelessRecipe of(ResourceLocation id, ShapelessRecipe recipe) {
		var ingredients = recipe.getIngredients();
		var output = recipe.getResultItem(null);

		var inputs = NonNullList.withSize(4, (EmiIngredient)EmiStack.of(ItemInitializer.SLOT_CHARM.get()));
		for (var i = 0; i < ingredients.size(); i++) {
			inputs.set(i, EmiIngredient.of(ingredients.get(i)));
		}

		return new EmiAltarShapelessRecipe(WanderingWizardry.id(EmiInitializer.getPrefixedPathedIdentifier(id, "altar_crafting_shapeless")), inputs, EmiStack.of(output.copy()));
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EmiInitializer.SHAPELESS;
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
		return EmiStack.of(ItemInitializer.CRAFTING_CHARM.get());
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
