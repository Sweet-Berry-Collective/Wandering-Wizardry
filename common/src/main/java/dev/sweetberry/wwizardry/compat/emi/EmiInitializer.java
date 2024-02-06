package dev.sweetberry.wwizardry.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.compat.emi.recipe.EmiAltarBrewingRecipe;
import dev.sweetberry.wwizardry.compat.emi.recipe.EmiAltarCatalyzationRecipe;
import dev.sweetberry.wwizardry.compat.emi.recipe.EmiAltarShapelessRecipe;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.recipe.AltarCatalyzationRecipe;
import dev.sweetberry.wwizardry.content.recipe.RecipeInitializer;
import dev.sweetberry.wwizardry.mixin.Accessor_PotionBrewing;
import dev.sweetberry.wwizardry.mixin.Accessor_PotionBrewing_Mix;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

@EmiEntrypoint
public class EmiInitializer implements EmiPlugin {
	public static final EmiStack BASE_ICON = EmiStack.of(ItemInitializer.CRYSTALLINE_SCULK_SHARD.get());
	public static final EmiRecipeCategory BASE = new EmiRecipeCategory(
		WanderingWizardry.id("altar_catalyzation"),
		BASE_ICON
	);

	public static final EmiStack SHAPELESS_ICON = EmiStack.of(ItemInitializer.CRAFTING_CHARM.get());
	public static final EmiRecipeCategory SHAPELESS = new EmiRecipeCategory(
		WanderingWizardry.id("altar_shapeless"),
		SHAPELESS_ICON
	);

	public static final EmiStack BREWING_ICON = EmiStack.of(ItemInitializer.BREWING_CHARM.get());
	public static final EmiRecipeCategory BREWING = new EmiRecipeCategory(
		WanderingWizardry.id("altar_brewing"),
		BREWING_ICON
	);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(BASE);
		registry.addCategory(SHAPELESS);
		registry.addCategory(BREWING);

		registry.addWorkstation(BASE, EmiStack.of(BlockInitializer.ALTAR_CATALYZER.get()));
		registry.addWorkstation(BASE, EmiStack.of(BlockInitializer.ALTAR_PEDESTAL.get()));

		registry.addWorkstation(SHAPELESS, EmiStack.of(BlockInitializer.ALTAR_CATALYZER.get()));
		registry.addWorkstation(SHAPELESS, EmiStack.of(BlockInitializer.ALTAR_PEDESTAL.get()));
		registry.addWorkstation(SHAPELESS, EmiStack.of(ItemInitializer.CRAFTING_CHARM.get()));

		registry.addWorkstation(BREWING, EmiStack.of(BlockInitializer.ALTAR_CATALYZER.get()));
		registry.addWorkstation(BREWING, EmiStack.of(BlockInitializer.ALTAR_PEDESTAL.get()));
		registry.addWorkstation(BREWING, EmiStack.of(ItemInitializer.BREWING_CHARM.get()));

		var manager = registry.getRecipeManager();

		for (var recipe : manager.getAllRecipesFor(RecipeInitializer.ALTAR_TYPE.get())) {
			registry.addRecipe(EmiAltarCatalyzationRecipe.of(recipe.id(), recipe.value()));
		}

		for (
			var recipe :
			manager
				.getAllRecipesFor(RecipeType.CRAFTING)
				.stream()
				.filter(it -> it.value() instanceof ShapelessRecipe)
				.map(it -> (RecipeHolder<ShapelessRecipe>) (RecipeHolder<?>) it)
				.filter(it -> it.value().getIngredients().size() <= 4)
				.toList()
		) {
			registry.addRecipe(EmiAltarShapelessRecipe.of(recipe.id(), recipe.value()));
		}

		for (var ingredient : Accessor_PotionBrewing.getAllowedContainers()) {
			for (var stack : ingredient.getItems()) {
				var basePath = getPrefixedPathedIdentifier(BuiltInRegistries.ITEM.getKey(stack.getItem()), "altar_brewing");
				for (PotionBrewing.Mix<Potion> recipe : Accessor_PotionBrewing.getMixes()) {
					try {
						var accessor = (Accessor_PotionBrewing_Mix<Potion>)recipe;
						var recipeIngredient = accessor.getIngredient();
						if (recipeIngredient.getItems().length > 0) {
							var ingredientPath = getPrefixedPathedIdentifier(BuiltInRegistries.ITEM.getKey(recipeIngredient.getItems()[0].getItem()), basePath);
							var inputPath = getPrefixedPathedIdentifier(BuiltInRegistries.POTION.getKey(accessor.getFrom()), ingredientPath);
							var outputPath = getPrefixedPathedIdentifier(BuiltInRegistries.POTION.getKey(accessor.getTo()), inputPath);
							var id = WanderingWizardry.id(outputPath);

							registry.addRecipe(new EmiAltarBrewingRecipe(
							    EmiStack.of(PotionUtils.setPotion(stack.copy(), accessor.getFrom())),
								EmiIngredient.of(recipeIngredient),
								EmiStack.of(PotionUtils.setPotion(stack.copy(), accessor.getTo())),
								id
							));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static String getPathedIdentifier(ResourceLocation id) {
		return id.getNamespace() + "/" + id.getPath();
	}

	public static String getPrefixedPathedIdentifier(ResourceLocation id, String prefix) {
		return prefix + "/" + getPathedIdentifier(id);
	}
}
