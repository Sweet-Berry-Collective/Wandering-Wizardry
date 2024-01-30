package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.content.item.SelfRemainderingItem;
import net.minecraft.item.Item;

public abstract class AltarCharmItem extends SelfRemainderingItem implements AltarCraftable {
	public AltarCharmItem(Settings settings) {
		super(settings);
	}
}
