package io.github.sweetberrycollective.wwizardry.item;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import io.github.sweetberrycollective.wwizardry.block.CrystalSculkBlock;
import io.github.sweetberrycollective.wwizardry.block.SculkflowerBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class WanderingItems {
	static {
		STACKS = new ArrayList<>();
	}

	public static final Item CRYSTALLINE_SCULK_SHARD = registerItem(
			"crystalline_sculk",
			new Item(
					new QuiltItemSettings()
			)
	);

	public static final Item CRYSTALLINE_SCULK_BLOCK = registerItem(
			"crystalline_sculk_block",
			new BlockItem(
					CrystalSculkBlock.INSTANCE,
					new QuiltItemSettings()
			)
	);

	public static final Item SCULKFLOWER = registerItem(
			"sculkflower",
			new BlockItem(
					SculkflowerBlock.INSTANCE,
					new QuiltItemSettings()
			)
	);

	public static final List<ItemStack> STACKS;

	// This is here because of 'Illegal forward reference' :blobfox_waaaa:
	// just use kotlin:tm: - anonymous (don't git blame me 🥺👉👈)
	public static ItemStack getIcon() {
		return CRYSTALLINE_SCULK_SHARD.getDefaultStack();
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder(WanderingMod.id("items"))
			.icon(WanderingItems::getIcon)
			.entries((display, collector) -> collector.addStacks(STACKS))
			.build();

	public static void init() {
		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);
	}

	public static Item registerItem(String id, Item item) {
		STACKS.add(new ItemStack(item));
		return Registry.register(Registries.ITEM, WanderingMod.id(id), item);
	}

	public static Item registerBoatItem(String id, RegistryKey<TerraformBoatType> boatTypeKey, boolean chest, QuiltItemSettings itemSettings) {
		Item item = TerraformBoatItemHelper.registerBoatItem(WanderingMod.id(id), boatTypeKey, chest, itemSettings);
		STACKS.add(new ItemStack(item));
		return item;
	}
}
