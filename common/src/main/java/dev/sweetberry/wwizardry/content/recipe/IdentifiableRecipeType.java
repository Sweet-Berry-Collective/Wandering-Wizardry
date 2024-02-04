package dev.sweetberry.wwizardry.content.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public record IdentifiableRecipeType<T extends Recipe<?>>(ResourceLocation id) implements RecipeType<T> {
	@Override
	public String toString() {
		return id.toString();
	}
}
