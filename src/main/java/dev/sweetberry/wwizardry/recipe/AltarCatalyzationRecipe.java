package dev.sweetberry.wwizardry.recipe;

import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.api.AltarCraftable;
import dev.sweetberry.wwizardry.api.AltarRecipeView;
import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.item.WanderingItems;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Arrays;

public record AltarCatalyzationRecipe(
		Identifier id,
		Ingredient catalyst,
		Ingredient[] inputs,
		ItemStack result,
		boolean keepCatalyst,
		int bloom
) implements Recipe<AltarCatalyzerBlockEntity>, AltarCraftable {
	public static final WanderingRecipeType<AltarCatalyzationRecipe> TYPE = new WanderingRecipeType<>(WanderingMod.id("altar_catalyzation"));

	@Override
	public boolean matches(AltarCatalyzerBlockEntity inventory, World world) {
		if (!catalyst.test(inventory.heldItem)) return false;
		var met = new boolean[]{false, false, false, false};
		var neighbors = inventory.getNeighbors();
		for (var neighbor : neighbors) {
			for (var j = 0; j < 4; j++) {
				var isSlotCharm = neighbor.heldItem.getItem() == WanderingItems.SLOT_CHARM;
				if (!met[j]) {
					met[j] = inputs.length > j
						? inputs[j].test(neighbor.heldItem) || (isSlotCharm && inputs[j].test(ItemStack.EMPTY))
						: isSlotCharm;
					if (met[j]) j = 5;
				}
			}
		}
		for (var b : met) {
			if (!b)
				return false;
		}
		return true;
	}

	@Override
	public ItemStack craft(AltarCatalyzerBlockEntity inventory, DynamicRegistryManager registryManager) {
		return result.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return width == 1 && height == 1;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> list = DefaultedList.of();
		list.addAll(Arrays.asList(inputs));
		return list;
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager registryManager) {
		return result;
	}

	@Override
	public Identifier getId() {
		return id();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AltarCatalyzationRecipeSerializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, World world) {
		view.setBloom(bloom);
		view.setRecipeResult(result.copy());
		if (keepCatalyst)
			view.setResultInPedestal(
				AltarRecipeView.AltarDirection.CENTER,
				view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER)
			);
		else
			view.setResultInPedestal(
				AltarRecipeView.AltarDirection.CENTER,
				view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER).getRecipeRemainder()
			);
		for (var dir : AltarRecipeView.AltarDirection.cardinals()) {
			view.setResultInPedestal(
				dir,
				view.getItemInPedestal(dir).getRecipeRemainder()
			);
		}
		return true;
	}
}
