package dev.sweetberry.wwizardry.datagen;

import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.item.WanderingItems;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resource.MultiPackResourceManager;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

public class BrickType extends AbstractDataGenerator {
	public final String baseName;
	public final boolean plural;

	public final Block BASE;
	public final Item BASE_ITEM;
	public final Block STAIRS;
	public final Item STAIRS_ITEM;
	public final Block SLAB;
	public final Item SLAB_ITEM;
	public final Block WALL;
	public final Item WALL_ITEM;

	public BrickType(String baseName, boolean plural, MapColor color, BlockSoundGroup sounds) {
		super();

		this.baseName = baseName;
		this.plural = plural;

		final var blockSettings = QuiltBlockSettings.of(Material.STONE).sounds(sounds).mapColor(color);
		final var itemSettings = new QuiltItemSettings();

		BASE = WanderingBlocks.registerBlock(baseName+(plural?"s":""), new Block(blockSettings));
		BASE_ITEM = WanderingItems.registerItem(baseName+(plural?"s":""), new BlockItem(BASE, itemSettings));

		STAIRS = WanderingBlocks.registerBlock(baseName+"_stairs", new StairsBlock(BASE.getDefaultState(), blockSettings));
		STAIRS_ITEM = WanderingItems.registerItem(baseName+"_stairs", new BlockItem(STAIRS, itemSettings));

		SLAB = WanderingBlocks.registerBlock(baseName+"_slab", new SlabBlock(blockSettings));
		SLAB_ITEM = WanderingItems.registerItem(baseName+"_slab", new BlockItem(SLAB, itemSettings));

		WALL = WanderingBlocks.registerBlock(baseName+"_wall", new WallBlock(blockSettings));
		WALL_ITEM = WanderingItems.registerItem(baseName+"_wall", new BlockItem(WALL, itemSettings));
	}

	@Override
	public void onRegisterPack(@NotNull ResourcePackRegistrationContext context) {
		var manager = context.resourceManager();
		if (!(manager instanceof MultiPackResourceManager multiManager)) return;
		var pack = WanderingDatagen.pack;
		var blockstates = new BlockstateDataApplier(context, baseName, plural);
		var blockModels = new BlockModelDataApplier(context, baseName, plural);
		var itemModels = new ItemModelDataApplier(context, baseName, plural);
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

		public BlockstateDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, boolean plural) {
			super(context, baseName, "brick");
			this.plural = plural;
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
			WALL = getResource("wall").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(InMemoryResourcePack pack) {
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

		public BlockModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, boolean plural) {
			super(context, baseName, "brick");
			this.plural = plural;
			WALL = getResource("wall").replace("?", (plural?"s":""));
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(InMemoryResourcePack pack) {
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

		public ItemModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, boolean plural) {
			super(context, baseName, "brick");
			this.plural = plural;
			WALL = getResource("wall").replace("?", (plural?"s":""));
			BRICKS = getResource("bricks").replace("?", (plural?"s":""));
			SLAB = getResource("slab").replace("?", (plural?"s":""));
			STAIRS = getResource("stairs").replace("?", (plural?"s":""));
		}

		@Override
		public void addToResourcePack(InMemoryResourcePack pack) {
			put(pack, baseName+(plural?"s":""), BRICKS);
			put(pack, baseName+"_slab", SLAB);
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, baseName+"_wall", WALL);
		}
	}
}
