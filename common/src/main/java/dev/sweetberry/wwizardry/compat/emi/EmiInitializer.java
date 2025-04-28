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
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.recipe.RecipeInitializer;
import dev.sweetberry.wwizardry.mixin.Accessor_PotionBrewing;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
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

		for (var recipe : manager.getAllRecipesFor(RecipeInitializer.ALTAR_TYPE.get()))
			registry.addRecipe(EmiAltarCatalyzationRecipe.of(recipe.id(), recipe.value()));

		for (
			var recipe :
			manager
				.getAllRecipesFor(RecipeType.CRAFTING)
				.stream()
				.filter(it -> it.value() instanceof ShapelessRecipe)
				.map(it -> (RecipeHolder<ShapelessRecipe>) (RecipeHolder<?>) it)
				.filter(it -> it.value().getIngredients().size() <= 4)
				.toList()
		)
			registry.addRecipe(EmiAltarShapelessRecipe.of(recipe.id(), recipe.value()));

		// TODO: Fix this code
		var brewing = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.potionBrewing() : PotionBrewing.EMPTY;
		var a_brewing = (Accessor_PotionBrewing)brewing;
		for (var ingredient : a_brewing.getContainers()) {
			for (var stack : ingredient.getItems()) {
				var basePath = getPrefixedPathedIdentifier(BuiltInRegistries.ITEM.getKey(stack.getItem()), "altar_brewing");
				for (PotionBrewing.Mix<Potion> recipe : a_brewing.getMixes()) {
					try {
						var recipeIngredient = recipe.ingredient();
						if (recipeIngredient.getItems().length > 0) {
							var ingredientPath = getPrefixedPathedIdentifier(BuiltInRegistries.ITEM.getKey(recipeIngredient.getItems()[0].getItem()), basePath);
							var inputPath = getPrefixedPathedIdentifier(BuiltInRegistries.POTION.getKey(recipe.from().value()), ingredientPath);
							var outputPath = getPrefixedPathedIdentifier(BuiltInRegistries.POTION.getKey(recipe.to().value()), inputPath);
							var id = WanderingWizardry.id(outputPath);

							final Item[] items = {
								Items.POTION,
								Items.SPLASH_POTION,
								Items.LINGERING_POTION,
								Items.TIPPED_ARROW
							};
							final String[] strings = {
								"/potion/",
								"/splash_potion/",
								"/lingering_potion/",
								"/tipped_arrow/"
							};

							final ItemStack[] inputStacks = new ItemStack[4];
							final ItemStack[] outputStacks = new ItemStack[4];

							for (int i = 0; i < 4; i++) {
								var inputStack = items[i].getDefaultInstance();
								inputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(recipe.from()));
								var outputStack = items[i].getDefaultInstance();
								outputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(recipe.to()));

								registry.addRecipe(new EmiAltarBrewingRecipe(
									EmiStack.of(inputStack),
									EmiIngredient.of(recipeIngredient),
									EmiStack.of(outputStack),
									id.withPrefix(strings[i])
								));
							}
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
