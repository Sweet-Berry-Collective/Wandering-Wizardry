package io.github.sweetberrycollective.wwizardry.recipe;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Arrays;

public record AltarRecipe(Identifier id, Ingredient catalyst, Ingredient[] inputs, ItemStack result,
						  boolean keepCatalyst) implements Recipe<AltarCatalyzerBlockEntity> {
	public static final WanderingRecipeType<AltarRecipe> TYPE = new WanderingRecipeType<>(WanderingMod.id("altar"));

	@Override
	public boolean matches(AltarCatalyzerBlockEntity inventory, World world) {
		if (!catalyst.test(inventory.heldItem)) return false;
		var met = new boolean[]{false, false, false, false};
		var neighbors = inventory.getNeighbors();
		for (var neighbor : neighbors) {
			for (var j = 0; j < 4; j++) {
				if (!met[j]) {
					met[j] = inputs[j].test(neighbor.heldItem);
					j = 5;
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
	public ItemStack craft(AltarCatalyzerBlockEntity inventory) {
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
	public ItemStack getOutput() {
		return result;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AltarRecipeSerializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}
}
