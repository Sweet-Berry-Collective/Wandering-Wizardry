package io.github.sweetberrycollective.wwizardry.recipe;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class WanderingRecipes {
	public static void init() {
		registerRecipe(AltarCatalyzationRecipe.TYPE);
		registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer.INSTANCE);
	}

	public static <T extends Recipe<?>> void registerRecipe(WanderingRecipeType<T> recipe) {
		Registry.register(Registries.RECIPE_TYPE, recipe.id(), recipe);
	}

	public static <T extends Recipe<?>> void registerSerializer(String id, QuiltRecipeSerializer<T> recipe) {
		Registry.register(Registries.RECIPE_SERIALIZER, WanderingMod.id(id), recipe);
	}
}
