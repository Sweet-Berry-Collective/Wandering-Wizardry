package io.github.sweetberrycollective.wwizardry.item;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class WanderingItems {
	public static void init() {
		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);
	}

	public static void registerItem(String id, Item item) {
		Registry.register(Registry.ITEM, WanderingMod.id(id), item);
	}
}
