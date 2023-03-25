package io.github.sweetberrycollective.wwizardry.item;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import io.github.sweetberrycollective.wwizardry.block.CrystalSculkBlock;
import io.github.sweetberrycollective.wwizardry.block.SculkflowerBlock;
import io.github.sweetberrycollective.wwizardry.datagen.WanderingDatagen;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Arrays;
import java.util.List;

public class WanderingItems {
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

	public static final Item[] ITEMS = new Item[] {
			CRYSTALLINE_SCULK_SHARD,
			CRYSTALLINE_SCULK_BLOCK,
			SCULKFLOWER,
			AltarPedestalBlock.ITEM,
			AltarCatalyzerBlock.ITEM,
			WanderingDatagen.BASALT_BRICKS.BASE_ITEM,
			WanderingDatagen.BASALT_BRICKS.SLAB_ITEM,
			WanderingDatagen.BASALT_BRICKS.STAIRS_ITEM,
			WanderingDatagen.BASALT_BRICKS.WALL_ITEM,
			WanderingDatagen.BASALT_TILES.BASE_ITEM,
			WanderingDatagen.BASALT_TILES.SLAB_ITEM,
			WanderingDatagen.BASALT_TILES.STAIRS_ITEM,
			WanderingDatagen.BASALT_TILES.WALL_ITEM,
			WanderingDatagen.CHISELED_BASALT.BASE_ITEM,
			WanderingDatagen.CHISELED_BASALT.SLAB_ITEM,
			WanderingDatagen.CHISELED_BASALT.STAIRS_ITEM,
			WanderingDatagen.CHISELED_BASALT.WALL_ITEM,
			WanderingDatagen.DENIA_WOOD.BOAT_CHEST_ITEM,
			WanderingDatagen.DENIA_WOOD.BOAT_ITEM,
			WanderingDatagen.DENIA_WOOD.BUTTON_ITEM,
			WanderingDatagen.DENIA_WOOD.DOOR_ITEM,
			WanderingDatagen.DENIA_WOOD.FENCE_GATE_ITEM,
			WanderingDatagen.DENIA_WOOD.FENCE_ITEM,
			WanderingDatagen.DENIA_WOOD.LEAVES_ITEM,
			WanderingDatagen.DENIA_WOOD.LOG_ITEM,
			WanderingDatagen.DENIA_WOOD.PLANKS_ITEM,
			WanderingDatagen.DENIA_WOOD.PRESSURE_PLATE_ITEM,
			WanderingDatagen.DENIA_WOOD.SIGN_ITEM,
			WanderingDatagen.DENIA_WOOD.SLAB_ITEM,
			WanderingDatagen.DENIA_WOOD.STAIRS_ITEM,
			WanderingDatagen.DENIA_WOOD.TRAPDOOR_ITEM,
			WanderingDatagen.DENIA_WOOD.WOOD_ITEM,
			WanderingDatagen.DENIA_WOOD.STRIPPED_LOG_ITEM,
			WanderingDatagen.DENIA_WOOD.STRIPPED_WOOD_ITEM,
	};
	public static final List<ItemStack> STACKS = Arrays.stream(ITEMS).map(Item::getDefaultStack).toList();

	// This is here because of 'Illegal forward reference' :blobfox_waaaa:
	// just use kotlin:tm: - anonymous (don't git blame me 🥺👉👈)
	public static ItemStack getIcon() {
		return CRYSTALLINE_SCULK_SHARD.getDefaultStack();
	}

	public static ItemGroup GROUP = FabricItemGroup.builder(WanderingMod.id("items"))
			.icon(WanderingItems::getIcon)
			.entries((display, collector) -> {
				collector.addStacks(STACKS);
			})
			.build();

	public static void init() {
		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);
	}

	public static Item registerItem(String id, Item item) {
		return Registry.register(Registries.ITEM, WanderingMod.id(id), item);
	}

	public static Item registerBoatItem(String s, RegistryKey<TerraformBoatType> boat, boolean b, QuiltItemSettings itemSettings) {
		return TerraformBoatItemHelper.registerBoatItem(WanderingMod.id(s), boat, b, itemSettings);
	}
}
