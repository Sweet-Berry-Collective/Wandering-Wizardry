package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BrickType extends AbstractDataGenerator {
	public final String baseName;
	public final boolean plural;

	public final Lazy<Block> BASE;
	public final Lazy<Item> BASE_ITEM;
	public final Lazy<Block> STAIRS;
	public final Lazy<Item> STAIRS_ITEM;
	public final Lazy<Block> SLAB;
	public final Lazy<Item> SLAB_ITEM;
	public final Lazy<Block> WALL;
	public final Lazy<Item> WALL_ITEM;

	public BrickType(String baseName, boolean plural, MapColor color, SoundType sounds) {
		super();

		this.baseName = baseName;
		this.plural = plural;

		final var blockSettings = BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).sound(sounds).mapColor(color).requiresCorrectToolForDrops();
		final var itemSettings = new Item.Properties();

		BASE = BlockInitializer.registerBlock(baseName+(plural?"s":""), () -> new Block(blockSettings));
		BASE_ITEM = ItemInitializer.registerItem(baseName+(plural?"s":""), () -> new BlockItem(BASE.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		STAIRS = BlockInitializer.registerBlock(baseName+"_stairs", () -> new StairBlock(BASE.get().defaultBlockState(), blockSettings));
		STAIRS_ITEM = ItemInitializer.registerItem(baseName+"_stairs", () -> new BlockItem(STAIRS.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		SLAB = BlockInitializer.registerBlock(baseName+"_slab", () -> new SlabBlock(blockSettings));
		SLAB_ITEM = ItemInitializer.registerItem(baseName+"_slab", () -> new BlockItem(SLAB.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		WALL = BlockInitializer.registerBlock(baseName+"_wall", () -> new WallBlock(blockSettings));
		WALL_ITEM = ItemInitializer.registerItem(baseName+"_wall", () -> new BlockItem(WALL.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);
	}

	@Override
	public void onRegisterPack(@NotNull ResourceManager manager, MapBackedPack pack) {
		var blockstates = new BlockstateDataApplier(manager, baseName, plural);
		var blockModels = new BlockModelDataApplier(manager, baseName, plural);
		var itemModels = new ItemModelDataApplier(manager, baseName, plural);
		blockstates.addToResourcePack(pack);
		blockModels.addToResourcePack(pack);
		itemModels.addToResourcePack(pack);
	}

	public static class BlockstateDataApplier extends AbstractDataGenerator.AbstractBlockstateDataApplier {
		public final String BRICKS;
		public final String SLAB;
		public final String STAIRS;
		public final String WALL;

		public final boolean plural;

		public BlockstateDataApplier(@NotNull ResourceManager manager, String baseName, boolean plural) {
			super(manager, baseName, "brick");
			this.plural = plural;
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
			WALL = getResource("wall").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(MapBackedPack pack) {
			put(pack, baseName+(plural?"s":""), BRICKS);
			put(pack, baseName+"_slab", SLAB);
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, baseName+"_wall", WALL);
		}
	}

	public static class BlockModelDataApplier extends AbstractBlockModelDataApplier {
		public final String BRICKS;
		public final String SLAB;
		public final String STAIRS;
		public final String WALL;

		public final boolean plural;

		public BlockModelDataApplier(@NotNull ResourceManager manager, String baseName, boolean plural) {
			super(manager, baseName, "brick");
			this.plural = plural;
			WALL = getResource("wall").replace("?", (plural?"s":""));
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(MapBackedPack pack) {
			put(pack, baseName+(plural?"s":""), BRICKS);
			put(pack, baseName+"_slab", SLAB, null);
			put(pack, baseName+"_slab", SLAB, "top");
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, baseName+"_stairs_inner", STAIRS.replace("stairs", "inner_stairs"));
			put(pack, baseName+"_stairs_outer", STAIRS.replace("stairs", "outer_stairs"));
			put(pack, baseName+"_wall", WALL.replace("template_", ""), "inventory");
			put(pack, baseName+"_wall", WALL, "post");
			put(pack, baseName+"_wall", WALL, "side");
			put(pack, baseName+"_wall", WALL, "side_tall");
		}
	}

	public static class ItemModelDataApplier extends AbstractItemModelDataApplier {
		public final String BRICKS;
		public final String SLAB;
		public final String STAIRS;
		public final String WALL;

		public final boolean plural;

		public ItemModelDataApplier(@NotNull ResourceManager manager, String baseName, boolean plural) {
			super(manager, baseName, "brick");
			this.plural = plural;
			WALL = getResource("wall").replace("?", (plural?"s":""));
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(MapBackedPack pack) {
			put(pack, baseName+(plural?"s":""), BRICKS);
			put(pack, baseName+"_slab", SLAB);
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, baseName+"_wall", WALL);
		}
	}
}
