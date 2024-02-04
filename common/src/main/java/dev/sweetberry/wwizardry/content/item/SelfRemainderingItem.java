package dev.sweetberry.wwizardry.content.item;

import net.minecraft.world.item.Item;

public class SelfRemainderingItem extends Item {
	public SelfRemainderingItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}
}
