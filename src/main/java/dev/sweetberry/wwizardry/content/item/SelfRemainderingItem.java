package dev.sweetberry.wwizardry.content.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SelfRemainderingItem extends Item {
	public SelfRemainderingItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasRecipeRemainder() {
		return true;
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		return stack;
	}
}
