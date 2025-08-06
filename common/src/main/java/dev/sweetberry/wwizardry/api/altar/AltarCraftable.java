package dev.sweetberry.wwizardry.api.altar;

import dev.sweetberry.wwizardry.api.event.Event;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * An event called when the altar tries to craft something
 * */
@FunctionalInterface
public interface AltarCraftable {
	Event<AltarCraftable> EVENT = new Event<>(events -> (view, world) -> {
		for (var event : events) {
			view.reset();
			if (event.tryCraft(view, world))
				return true;
		}
		return false;
	});

	boolean tryCraft(AltarRecipeView view, Level world);

	static boolean ensureAllFilled(AltarRecipeView view) {
		for (var i : AltarRecipeView.AltarDirection.cardinals()) {
			var item = view.getItemInPedestal(i);

			if (item == null)
				return false;
		}

		return true;
	}

	static Optional<AltarRecipeView.AltarDirection> ensureOneOfType(AltarRecipeView view, Predicate<ItemStack> predicate) {
		Optional<AltarRecipeView.AltarDirection> match = Optional.empty();

		for (var i : AltarRecipeView.AltarDirection.cardinals()) {
			var item = view.getItemInPedestal(i);

			if (item == null)
				return Optional.empty();

			var matches = predicate.test(item);

			if (matches && match.isPresent())
				return Optional.empty();

			if (matches)
				match = Optional.of(i);
		}

		return match;
	}

	static AltarRecipeView.AltarDirection[] getAllOfType(AltarRecipeView view, Predicate<ItemStack> predicate) {
		var values = new ArrayList<AltarRecipeView.AltarDirection>();

		for (var i : AltarRecipeView.AltarDirection.cardinals()) {
			var item = view.getItemInPedestal(i);

			if (item == null)
				return null;

			if (predicate.test(item))
				values.add(i);
		}

		return values.toArray(AltarRecipeView.AltarDirection[]::new);
	}
}
