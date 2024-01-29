package dev.sweetberry.wwizardry.content.recipe;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeInitializer {
	public static void init() {
		registerRecipe(AltarCatalyzationRecipe.TYPE);
		registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer.INSTANCE);
	}

	public static <T extends Recipe<?>> void registerRecipe(IdentifiableRecipeType<T> recipe) {
		Registry.register(Registries.RECIPE_TYPE, recipe.id(), recipe);
	}

	public static <T extends Recipe<?>> void registerSerializer(String id, RecipeSerializer<T> recipe) {
		Registry.register(Registries.RECIPE_SERIALIZER, Mod.id(id), recipe);
	}
}
