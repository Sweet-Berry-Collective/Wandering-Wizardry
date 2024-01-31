package dev.sweetberry.wwizardry.api.altar;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface AltarCraftable {
	Event<AltarCraftable> EVENT = EventFactory.createArrayBacked(AltarCraftable.class, events -> (view, world) -> {
		for (var event : events) {
			view.reset();
			if (event.tryCraft(view, world))
				return true;
		}
		return false;
	});

	boolean tryCraft(AltarRecipeView view, Level world);
}
