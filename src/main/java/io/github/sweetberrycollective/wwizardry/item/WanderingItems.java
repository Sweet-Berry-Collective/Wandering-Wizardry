package io.github.sweetberrycollective.wwizardry.item;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class WanderingItems {
	public static final Item CRYSTALLINE_SCULK_SHARD = registerItem(
			"crystalline_sculk",
			new Item(
					new QuiltItemSettings()
			)
	);

	public static void init() {
		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);
	}

	public static Item registerItem(String id, Item item) {
		return Registry.register(Registry.ITEM, WanderingMod.id(id), item);
	}
}
