package dev.sweetberry.wwizardry.api.altar;

import net.minecraft.world.World;
import org.quiltmc.qsl.base.api.event.Event;

@FunctionalInterface
public interface AltarCraftable {
	Event<AltarCraftable> EVENT = Event.create(AltarCraftable.class, events -> (view, world) -> {
		for (var event : events) {
			view.reset();
			if (event.tryCraft(view, world))
				return true;
		}
		return false;
	});

	boolean tryCraft(AltarRecipeView view, World world);
}
