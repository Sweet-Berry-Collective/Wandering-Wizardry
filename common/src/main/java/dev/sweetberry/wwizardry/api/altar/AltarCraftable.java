package dev.sweetberry.wwizardry.api.altar;

import dev.sweetberry.wwizardry.api.event.Event;
import net.minecraft.world.level.Level;

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
}
