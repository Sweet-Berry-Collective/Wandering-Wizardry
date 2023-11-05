package dev.sweetberry.wwizardry.item;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import net.minecraft.item.Item;

public abstract class AltarCharmItem extends Item implements AltarCraftable {
	public AltarCharmItem(Settings settings) {
		super(settings);
	}
}
