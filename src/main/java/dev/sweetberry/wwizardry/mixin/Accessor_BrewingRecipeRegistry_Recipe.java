package dev.sweetberry.wwizardry.mixin;

import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrewingRecipeRegistry.Recipe.class)
public interface Accessor_BrewingRecipeRegistry_Recipe<T> {
	@Accessor
	Ingredient getIngredient();

	@Accessor
	T getInput();

	@Accessor
	T getOutput();
}
