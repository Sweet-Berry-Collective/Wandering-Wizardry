package dev.sweetberry.wwizardry.content.recipe;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeInitializer {
	public static void init() {
		registerRecipe(AltarCatalyzationRecipe.TYPE);
		registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer.INSTANCE);
	}

	public static <T extends Recipe<?>> void registerRecipe(IdentifiableRecipeType<T> recipe) {
		Registry.register(BuiltInRegistries.RECIPE_TYPE, recipe.id(), recipe);
	}

	public static <T extends Recipe<?>> void registerSerializer(String id, RecipeSerializer<T> recipe) {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Mod.id(id), recipe);
	}
}
