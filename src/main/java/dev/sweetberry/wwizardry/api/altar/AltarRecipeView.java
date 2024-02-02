package dev.sweetberry.wwizardry.api.altar;

import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * A view of the altars state as it tries to craft an item
 * */
public interface AltarRecipeView {
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
	default boolean ingredientsMatch(List<Ingredient> inputs) {
		var met = new boolean[] {false, false, false, false};
		// Iterate through the cardinal directions
		for (var dir : AltarDirection.cardinals()) {
			// Iterate through the ingredients
			for (var j = 0; j < 4; j++) {
				var heldItem = getItemInPedestal(dir);
				var isSlotCharm = heldItem.getItem() == ItemInitializer.SLOT_CHARM;
				// If this ingredient was already met, skil
				if (!met[j]) {
					// Check if the ingredient is met in this slot
					met[j] = inputs.size() > j
						? inputs.get(j).test(heldItem) || (isSlotCharm && inputs.get(j).test(ItemStack.EMPTY))
						: isSlotCharm;
					// If it was met, break out of the inner loop
					if (met[j]) j = 5;
				}
			}
		}
		// Check if any of them haven't been met
		for (var b : met)
			if (!b)
				return false;
		return true;
	}

	/**
	 * Set the cardinal pedestals as the remainder of their held items
	 * */
	default void setCardinalsAsRemainders() {
		for (var dir : AltarDirection.cardinals()) {
			var item = getItemInPedestal(dir);
			if (item != null)
				setResultInPedestal(
					dir,
					item.getRecipeRemainder()
				);
		}
	}

	/**
	 * Marks the center as the remainder of it's held item
	 * */
	default void keepCenter() {
		var item = getItemInPedestal(AltarDirection.CENTER);
		if (item != null)
			setResultInPedestal(
				AltarDirection.CENTER,
				item.getRecipeRemainder()
			);
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
