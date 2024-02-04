package dev.sweetberry.wwizardry.api.altar;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A view of the altars state as it tries to craft an item
 * */
public interface AltarRecipeView extends Container {
	/**
	 * Gets an item in the referenced pedestal
	 * Returns null if there's no pedestal
	 * */
	@Nullable
	ItemStack getItemInPedestal(AltarDirection direction);

	/**
	 * Sets the crafting result in the referenced pedestal
	 * */
	void setResultInPedestal(AltarDirection direction, ItemStack stack);

	/**
	 * Gets the currently stored result in the referenced pedestal
	 * */
	ItemStack getResultInPedestal(AltarDirection direction);
	/**
	 * Sets the item crafted, this isn't placed in any pedestal
	 * */
	void setRecipeResult(ItemStack stack);
	/**
	 * Gets the item crafted
	 * */
	ItemStack getRecipeResult();
	/**
	 * Sets the bloom for the catalyzer.
	 * This affects how much sculk is generated
	 * Set to 0 to disable
	 * */
	void setBloom(int bloom);
	/**
	 * Gets the bloom for the catalyzer
	 * */
	int getBloom();
	/**
	 * Resets the state to what was initially captured
	 * */
	void reset();

	/**
	 * Check if a list of ingredients matches the outer pedestals
	 * */
	boolean ingredientsMatch(List<Ingredient> inputs);

	/**
	 * */
	default List<ItemStack> getOuterItems() {
		var out = new ArrayList<ItemStack>();
		for (var dir : AltarDirection.cardinals())
			out.add(getItemInPedestal(dir));
		return out;
	}

	/**
	 * Set the cardinal pedestals as the remainder of their held items
	 * */
	default void setCardinalsAsRemainders() {
		for (var dir : AltarDirection.cardinals()) {
			var stack = getItemInPedestal(dir);
			if (stack != null) {
				var item = stack.getItem();
				setResultInPedestal(
					dir,
					item.hasCraftingRemainingItem()
						? item.getCraftingRemainingItem().getDefaultInstance()
						: ItemStack.EMPTY
				);
			}
		}
	}

	/**
	 * Marks the center as the remainder of it's held item
	 * */
	default void keepCenter() {
		var stack = getItemInPedestal(AltarDirection.CENTER);
		if (stack != null) {
			var item = stack.getItem();
			setResultInPedestal(
				AltarDirection.CENTER,
				item.hasCraftingRemainingItem()
					? item.getCraftingRemainingItem().getDefaultInstance()
					: ItemStack.EMPTY
			);
		}
	}

	/**
	 * Set all pedestals as the remainder of their held item
	 * */
	default void setAllAsRemainders() {
		setCardinalsAsRemainders();
		keepCenter();
	}

	/**
	 * References a different pedestal
	 * */
	enum AltarDirection {
		/**
		 * The catalyzer
		 * */
		CENTER,
		NORTH,
		SOUTH,
		EAST,
		WEST;

		@Nullable
		public static AltarDirection fromDirection(Direction direction) {
			return switch (direction) {
				case NORTH -> NORTH;
				case SOUTH -> SOUTH;
				case EAST -> EAST;
				case WEST -> WEST;
				default -> null;
			};
		}

		public static AltarDirection[] cardinals() {
			return new AltarDirection[] {
				NORTH,
				SOUTH,
				EAST,
				WEST
			};
		}

		public static AltarDirection[] cardinalWithout(AltarDirection dir) {
			return Arrays.stream(cardinals()).filter(it -> it != dir).toArray(AltarDirection[]::new);
		}
	}
}
