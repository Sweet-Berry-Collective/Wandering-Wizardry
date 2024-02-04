package dev.sweetberry.wwizardry.content.recipe;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeInitializer {
	public static final RegistryContext<RecipeType<?>> RECIPES = new RegistryContext<>(BuiltInRegistries.RECIPE_TYPE);
	public static final RegistryContext<RecipeSerializer<?>> RECIPE_SERIALIZERS = new RegistryContext<>(BuiltInRegistries.RECIPE_SERIALIZER);

	public static final IdentifiableRecipeType<?> ALTAR_TYPE = registerRecipe(AltarCatalyzationRecipe.TYPE);
	public static final RecipeSerializer<?> ALTAR_SERIALIZER = registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer.INSTANCE);

	public static <T extends Recipe<?>> IdentifiableRecipeType<T> registerRecipe(IdentifiableRecipeType<T> recipe) {
		return (IdentifiableRecipeType<T>) RECIPES.register(recipe.id(), recipe);
	}

	public static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String id, RecipeSerializer<T> recipe) {
		return (RecipeSerializer<T>) RECIPE_SERIALIZERS.register(WanderingWizardry.id(id), recipe);
	}
}
