package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface EmiAltarRecipe extends EmiRecipe {
	List<EmiIngredient> getOuterIngredients();
	EmiIngredient getInnerIngredient();
	EmiStack getOutput();
	boolean shouldKeepCatalyst();

	@Override
	default List<EmiIngredient> getInputs() {
		var inputs = new ArrayList<>(getOuterIngredients());
		inputs.add(getInnerIngredient());
		return inputs;
	}

	@Override
	default List<EmiStack> getOutputs() {
		return List.of(getOutput());
	}

	@Override
	default List<EmiIngredient> getCatalysts() {
		if (shouldKeepCatalyst())
			return List.of(getInnerIngredient());
		return List.of();
	}

	@Override
	default int getDisplayWidth() {
		return 119;
	}

	@Override
	default int getDisplayHeight() {
		return 80;
	}

	@Override
	default void addWidgets(WidgetHolder widgets) {
		var inputs = getOuterIngredients();
		widgets.addSlot(inputs.get(0), 1, 1);
		widgets.addSlot(inputs.get(1), 1, 21);
		widgets.addSlot(inputs.get(2), 1, 41);
		widgets.addSlot(inputs.get(3), 1, 61);

		widgets.addSlot(getInnerIngredient(), 47, 31).catalyst(shouldKeepCatalyst()).appendTooltip(Text.translatable("wwizardry.catalyst"));

		widgets.addSlot(getOutput(), 92, 28).large(true);

		widgets.addFillingArrow(21, 31, 1);

		widgets.addFillingArrow(67, 31, 1);
	}
}
