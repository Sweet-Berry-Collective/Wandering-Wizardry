package dev.sweetberry.wwizardry.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.block.AltarPedestalBlock;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.item.WanderingItems;
import dev.sweetberry.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.RecipeHolder;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EmiInitializer implements EmiPlugin {
	public static final EmiStack BASE_ICON = EmiStack.of(WanderingItems.CRYSTALLINE_SCULK_SHARD);
	public static final EmiRecipeCategory BASE = new EmiRecipeCategory(
		WanderingMod.id("altar_catalyzation"),
		BASE_ICON
	);

	public static final EmiStack SHAPELESS_ICON = EmiStack.of(WanderingItems.CRAFTING_CHARM);
	public static final EmiRecipeCategory SHAPELESS = new EmiRecipeCategory(
		WanderingMod.id("altar_shapeless"),
		SHAPELESS_ICON
	);

	public static final EmiStack BREWING_ICON = EmiStack.of(WanderingItems.BREWING_CHARM);
	public static final EmiRecipeCategory BREWING = new EmiRecipeCategory(
		WanderingMod.id("altar_brewing"),
		BREWING_ICON
	);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(BASE);
		registry.addCategory(SHAPELESS);
		registry.addCategory(BREWING);

		registry.addWorkstation(BASE, EmiStack.of(AltarCatalyzerBlock.INSTANCE));
		registry.addWorkstation(BASE, EmiStack.of(AltarPedestalBlock.INSTANCE));

		registry.addWorkstation(SHAPELESS, EmiStack.of(AltarCatalyzerBlock.INSTANCE));
		registry.addWorkstation(SHAPELESS, EmiStack.of(AltarPedestalBlock.INSTANCE));
		registry.addWorkstation(SHAPELESS, EmiStack.of(WanderingItems.CRAFTING_CHARM));

		registry.addWorkstation(BREWING, EmiStack.of(AltarCatalyzerBlock.INSTANCE));
		registry.addWorkstation(BREWING, EmiStack.of(AltarPedestalBlock.INSTANCE));
		registry.addWorkstation(BREWING, EmiStack.of(WanderingItems.BREWING_CHARM));

		var manager = registry.getRecipeManager();

		for (var recipe : manager.listAllOfType(AltarCatalyzationRecipe.TYPE)) {
			registry.addRecipe(EmiAltarCatalyzationRecipe.of(recipe.id(), recipe.value()));
		}

		for (
			var recipe :
			manager
				.listAllOfType(RecipeType.CRAFTING)
				.stream()
				.filter(it -> it.value() instanceof ShapelessRecipe)
				.map(it -> (RecipeHolder<ShapelessRecipe>) (RecipeHolder<?>) it)
				.filter(it -> it.value().getIngredients().size() <= 4)
				.toList()
		) {
			registry.addRecipe(EmiAltarShapelessRecipe.of(recipe.id(), recipe.value()));
		}

		for (var ingredient : BrewingRecipeRegistry.POTION_TYPES) {
			for (var stack : ingredient.getMatchingStacks()) {
				var basePath = getPrefixedPathedIdentifier(Registries.ITEM.getId(stack.getItem()), "altar_brewing");
				for (BrewingRecipeRegistry.Recipe<Potion> recipe : BrewingRecipeRegistry.POTION_RECIPES) {
					try {
						var recipeIngredient = recipe.ingredient;
						if (recipeIngredient.getMatchingStacks().length > 0) {
							var ingredientPath = getPrefixedPathedIdentifier(Registries.ITEM.getId(recipeIngredient.getMatchingStacks()[0].getItem()), basePath);
							var inputPath = getPrefixedPathedIdentifier(Registries.POTION.getId(recipe.input), ingredientPath);
							var outputPath = getPrefixedPathedIdentifier(Registries.POTION.getId(recipe.output), inputPath);
							var id = WanderingMod.id(outputPath);

							registry.addRecipe(new EmiAltarBrewingRecipe(
							    EmiStack.of(PotionUtil.setPotion(stack.copy(), recipe.input)),
								EmiIngredient.of(recipeIngredient),
								EmiStack.of(PotionUtil.setPotion(stack.copy(), recipe.output)),
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

	public static String getPathedIdentifier(Identifier id) {
		return id.getNamespace() + "/" + id.getPath();
	}

	public static String getPrefixedPathedIdentifier(Identifier id, String prefix) {
		return prefix + "/" + getPathedIdentifier(id);
	}
}
