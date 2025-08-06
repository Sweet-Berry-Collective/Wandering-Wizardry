package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.content.item.SelfRemainderingItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AltarCharmItem extends SelfRemainderingItem implements AltarCraftable {
	public AltarCharmItem(Properties settings) {
		super(settings);
	}
}
