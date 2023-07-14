package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.sweetberry.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record EmiAltarRecipe(Identifier id, List<EmiIngredient> input, EmiIngredient catalyst, boolean keepCatalyst, int bloom, EmiStack output) implements EmiRecipe {
	public static EmiAltarRecipe of(AltarCatalyzationRecipe recipe) {
		var inputs = new ArrayList<EmiIngredient>();
		for (var input : recipe.inputs()) {
			inputs.add(EmiIngredient.of(input));
		}

		return new EmiAltarRecipe(recipe.id(), inputs, EmiIngredient.of(recipe.catalyst()), recipe.keepCatalyst(), recipe.bloom(), EmiStack.of(recipe.result()));
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EmiInitializer.category;
	}

	@Override
	public @Nullable Identifier getId() {
		return id();
	}

	@Override
	public List<EmiIngredient> getInputs() {
		var inputs = new ArrayList<>(input);
		inputs.add(catalyst);
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		var outputs = new ArrayList<EmiStack>();
		outputs.add(output);
		if (keepCatalyst)
			outputs.addAll(catalyst.getEmiStacks());
		return outputs;
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

		widgets.addSlot(catalyst, 47, 31).catalyst(keepCatalyst).appendTooltip(Text.translatable("wwizardry.catalyst"));

		widgets.addSlot(output, 92, 28).large(true);

		widgets.addFillingArrow(21, 31, 1);

		widgets.addFillingArrow(67, 31, 1);
	}
}
