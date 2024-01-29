package dev.sweetberry.wwizardry.recipe;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class WanderingRecipes {
	public static void init() {
		registerRecipe(AltarCatalyzationRecipe.TYPE);
		registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer.INSTANCE);
	}

	public static <T extends Recipe<?>> void registerRecipe(WanderingRecipeType<T> recipe) {
		Registry.register(Registries.RECIPE_TYPE, recipe.id(), recipe);
	}

	public static <T extends Recipe<?>> void registerSerializer(String id, RecipeSerializer<T> recipe) {
		Registry.register(Registries.RECIPE_SERIALIZER, WanderingMod.id(id), recipe);
	}
}
