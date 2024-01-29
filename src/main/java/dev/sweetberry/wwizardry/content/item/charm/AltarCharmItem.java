package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import net.minecraft.item.Item;

public abstract class AltarCharmItem extends Item implements AltarCraftable {
	public AltarCharmItem(Settings settings) {
		super(settings);
	}
}
