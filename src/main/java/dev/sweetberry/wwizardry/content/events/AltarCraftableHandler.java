package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import net.minecraft.world.World;

public class AltarCraftableHandler {
	public static boolean tryCraft(AltarRecipeView view, World world) {
		var stack = view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER);
		if (stack == null)
			return false;
		if (stack.getItem() instanceof AltarCraftable craftable)
			return craftable.tryCraft(view, world);
		return false;
	}
}
