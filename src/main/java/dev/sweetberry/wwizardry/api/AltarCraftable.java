package dev.sweetberry.wwizardry.api;

import net.minecraft.world.World;

@FunctionalInterface
public interface AltarCraftable {
	boolean tryCraft(AltarRecipeView view, World world);
}
