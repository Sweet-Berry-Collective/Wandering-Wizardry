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
	public static final Item BASALT_BRICKS = registerItem(
			"basalt_bricks",
			new BlockItem(
					WanderingBlocks.BASALT_BRICKS,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_BRICK_STAIRS = registerItem(
			"basalt_brick_stairs",
			new BlockItem(
					WanderingBlocks.BASALT_BRICK_STAIRS,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_BRICK_SLAB = registerItem(
			"basalt_brick_slab",
			new BlockItem(
					WanderingBlocks.BASALT_BRICK_SLAB,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_BRICK_WALL = registerItem(
			"basalt_brick_wall",
			new BlockItem(
					WanderingBlocks.BASALT_BRICK_WALL,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_TILES = registerItem(
			"basalt_tiles",
			new BlockItem(
					WanderingBlocks.BASALT_TILES,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_TILE_STAIRS = registerItem(
			"basalt_tile_stairs",
			new BlockItem(
					WanderingBlocks.BASALT_TILE_STAIRS,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_TILE_SLAB = registerItem(
			"basalt_tile_slab",
			new BlockItem(
					WanderingBlocks.BASALT_TILE_SLAB,
					new QuiltItemSettings()
			)
	);
	public static final Item BASALT_TILE_WALL = registerItem(
			"basalt_tile_wall",
			new BlockItem(
					WanderingBlocks.BASALT_TILE_WALL,
					new QuiltItemSettings()
			)
	);
	public static final Item CHISELED_BASALT = registerItem(
			"chiseled_basalt",
			new BlockItem(
					WanderingBlocks.CHISELED_BASALT,
					new QuiltItemSettings()
			)
	);
	public static final Item CHISELED_BASALT_STAIRS = registerItem(
			"chiseled_basalt_stairs",
			new BlockItem(
					WanderingBlocks.CHISELED_BASALT_STAIRS,
					new QuiltItemSettings()
			)
	);
	public static final Item CHISELED_BASALT_SLAB = registerItem(
			"chiseled_basalt_slab",
			new BlockItem(
					WanderingBlocks.CHISELED_BASALT_SLAB,
					new QuiltItemSettings()
			)
	);
	public static final Item CHISELED_BASALT_WALL = registerItem(
			"chiseled_basalt_wall",
			new BlockItem(
					WanderingBlocks.CHISELED_BASALT_WALL,
					new QuiltItemSettings()
			)
	);

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
