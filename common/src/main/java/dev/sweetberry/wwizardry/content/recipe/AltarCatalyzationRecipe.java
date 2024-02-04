package dev.sweetberry.wwizardry.content.recipe;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

// TODO: Switch to taking in an AltarCraftable.
public record AltarCatalyzationRecipe(
		Ingredient catalyst,
		List<Ingredient> inputs,
		ItemStack result,
		boolean keepCatalyst,
		int bloom
) implements Recipe<AltarRecipeView>, AltarCraftable {
	public static final TagKey<Item> ALTAR_AIR_MODIFIER = TagKey.create(Registries.ITEM, WanderingWizardry.id("altar_air_modifier"));

	public static final IdentifiableRecipeType<AltarCatalyzationRecipe> TYPE = new IdentifiableRecipeType<>(WanderingWizardry.id("altar_catalyzation"));

	@Override
	public boolean matches(AltarRecipeView inventory, Level world) {
		if (!catalyst.test(inventory.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER))) return false;
		var met = new boolean[]{false, false, false, false};
		var neighbors = inventory.getOuterItems();
		for (var neighbor : neighbors) {
			for (var j = 0; j < 4; j++) {
				var isSlotCharm = neighbor.is(ALTAR_AIR_MODIFIER);
				if (!met[j]) {
					met[j] = inputs.size() > j
						? inputs.get(j).test(neighbor) || (isSlotCharm && inputs.get(j).test(ItemStack.EMPTY))
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
	public ItemStack assemble(AltarRecipeView container, RegistryAccess registryAccess) {
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width == 1 && height == 1;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.addAll(inputs);
		return list;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AltarCatalyzationRecipeSerializer.INSTANCE;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryManager) {
		return result;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		view.setBloom(bloom);
		view.setRecipeResult(result.copy());
		if (keepCatalyst)
			view.setResultInPedestal(
				AltarRecipeView.AltarDirection.CENTER,
				view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER)
			);
		else
			view.keepCenter();
		view.setCardinalsAsRemainders();
		return true;
	}
}
