package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.sweetberry.wwizardry.item.WanderingItems;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EmiAltarShapelessRecipe(Identifier id, List<EmiIngredient> input, EmiStack output) implements EmiRecipe {
	public static EmiAltarShapelessRecipe of(ShapelessRecipe recipe, DefaultedList<Ingredient> ingredients, ItemStack output) {
		var inputs = DefaultedList.ofSize(4, (EmiIngredient)EmiStack.of(WanderingItems.SLOT_CHARM));
		for (var i = 0; i < ingredients.size(); i++) {
			inputs.set(i, EmiIngredient.of(ingredients.get(i)));
		}

		return new EmiAltarShapelessRecipe(recipe.getId(), inputs, EmiStack.of(output.copy()));
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
	public List<EmiIngredient> getInputs() {
		return input;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return List.of(output);
	}

	@Override
	public int getDisplayWidth() {
		return 119;
	}

	@Override
	public int getDisplayHeight() {
		return 80;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(input.get(0), 1, 1);
		widgets.addSlot(input.get(1), 1, 21);
		widgets.addSlot(input.get(2), 1, 41);
		widgets.addSlot(input.get(3), 1, 61);

		widgets.addSlot(EmiStack.of(WanderingItems.CRAFTING_CHARM), 47, 31).catalyst(true).appendTooltip(Text.translatable("wwizardry.catalyst"));

		widgets.addSlot(output, 92, 28).large(true);

		widgets.addFillingArrow(21, 31, 1);

		widgets.addFillingArrow(67, 31, 1);
	}
}
