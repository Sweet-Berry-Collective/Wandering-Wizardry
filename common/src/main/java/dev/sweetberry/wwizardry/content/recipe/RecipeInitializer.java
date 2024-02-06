package dev.sweetberry.wwizardry.content.recipe;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class RecipeInitializer {
	public static final RegistryContext<RecipeType<?>> RECIPES = new RegistryContext<>(BuiltInRegistries.RECIPE_TYPE);
	public static final RegistryContext<RecipeSerializer<?>> RECIPE_SERIALIZERS = new RegistryContext<>(BuiltInRegistries.RECIPE_SERIALIZER);

	public static final Lazy<IdentifiableRecipeType<AltarCatalyzationRecipe>> ALTAR_TYPE = registerRecipe("altar_catalyzation");
	public static final Lazy<RecipeSerializer<AltarCatalyzationRecipe>> ALTAR_SERIALIZER = registerSerializer("altar_catalyzation", AltarCatalyzationRecipeSerializer::new);

	public static <T extends Recipe<?>> Lazy<IdentifiableRecipeType<T>> registerRecipe(String id) {
		var _id = WanderingWizardry.id(id);
		return (Lazy<IdentifiableRecipeType<T>>)(Object) RECIPES.register(_id, () -> new IdentifiableRecipeType<T>(_id));
	}

	public static <T extends Recipe<?>> Lazy<RecipeSerializer<T>> registerSerializer(String id, Supplier<RecipeSerializer<T>> recipe) {
		return (Lazy<RecipeSerializer<T>>)(Object) RECIPE_SERIALIZERS.register(WanderingWizardry.id(id), (Supplier<RecipeSerializer<?>>)(Object) recipe);
	}
}
