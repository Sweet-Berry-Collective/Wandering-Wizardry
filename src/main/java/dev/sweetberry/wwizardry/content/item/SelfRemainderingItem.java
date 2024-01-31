package dev.sweetberry.wwizardry.content.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SelfRemainderingItem extends Item {
	public SelfRemainderingItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		return stack;
	}
}
