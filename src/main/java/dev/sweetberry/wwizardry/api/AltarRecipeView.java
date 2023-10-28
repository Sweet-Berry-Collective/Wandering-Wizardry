package dev.sweetberry.wwizardry.api;

import dev.sweetberry.wwizardry.item.WanderingItems;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface AltarRecipeView {
	@Nullable
	ItemStack getItemInPedestal(AltarDirection direction);
	void setResultInPedestal(AltarDirection direction, ItemStack stack);
	ItemStack getResultInPedestal(AltarDirection direction);
	void setRecipeResult(ItemStack stack);
	ItemStack getRecipeResult();
	void setBloom(int bloom);
	int getBloom();
	void reset();

	default boolean ingredientsMatch(List<Ingredient> inputs) {
		var met = new boolean[]{false, false, false, false};
		for (var dir : AltarDirection.cardinals()) {
			for (var j = 0; j < 4; j++) {
				var heldItem = getItemInPedestal(dir);
				var isSlotCharm = heldItem.getItem() == WanderingItems.SLOT_CHARM;
				if (!met[j]) {
					met[j] = inputs.size() > j
						? inputs.get(j).test(heldItem) || (isSlotCharm && inputs.get(j).test(ItemStack.EMPTY))
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

	default void keepCenter() {
		var item = getItemInPedestal(AltarDirection.CENTER);
		if (item != null)
			setResultInPedestal(
				AltarDirection.CENTER,
				item.getRecipeRemainder()
			);
	}

	default void setAllAsRemainders() {
		setCardinalsAsRemainders();
		keepCenter();
	}

	enum AltarDirection {
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
