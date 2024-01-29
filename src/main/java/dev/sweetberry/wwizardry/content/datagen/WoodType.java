package dev.sweetberry.wwizardry.content.datagen;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.sign.block.TerraformHangingSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallHangingSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.sapling.BeeHoldingSaplingGenerator;
import dev.sweetberry.wwizardry.content.block.nature.RootedMushroomBlock;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingBlock;
import net.minecraft.block.sign.SignType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.MultiPackResourceManager;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.content.registry.api.BlockContentRegistries;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.resource.loader.api.InMemoryPack;
import org.quiltmc.qsl.resource.loader.api.PackRegistrationContext;

public class WoodType extends AbstractDataGenerator {
	public final String baseName;

	public final Block LOG;
	public final Item LOG_ITEM;
	public final Block STRIPPED_LOG;
	public final Item STRIPPED_LOG_ITEM;
	public final Block WOOD;
	public final Item WOOD_ITEM;
	public final Block STRIPPED_WOOD;
	public final Item STRIPPED_WOOD_ITEM;
	public final Block PLANKS;
	public final Item PLANKS_ITEM;
	public final Block STAIRS;
	public final Item STAIRS_ITEM;
	public final Block SLAB;
	public final Item SLAB_ITEM;
	public final Block BUTTON;
	public final Item BUTTON_ITEM;
	public final Block PRESSURE_PLATE;
	public final Item PRESSURE_PLATE_ITEM;
	public final Block DOOR;
	public final Item DOOR_ITEM;
	public final Block TRAPDOOR;
	public final Item TRAPDOOR_ITEM;
	public final TerraformSignBlock SIGN;
	public final TerraformWallSignBlock SIGN_WALL;
	public final Item SIGN_ITEM;
	public final TerraformHangingSignBlock HANGING_SIGN;
	public final TerraformWallHangingSignBlock HANGING_SIGN_WALL;
	public final Item HANGING_SIGN_ITEM;
	public final Block FENCE;
	public final Item FENCE_ITEM;
	public final Block FENCE_GATE;
	public final Item FENCE_GATE_ITEM;
	public final Block LEAVES;
	public final Item LEAVES_ITEM;
	public final Block SAPLING;
	public final Item SAPLING_ITEM;

	public final boolean fungus;

	public final TerraformBoatType BOAT;

	public final RegistryKey<TerraformBoatType> BOAT_KEY;

	public final Item BOAT_ITEM;

	public final Item BOAT_CHEST_ITEM;
	public WoodType(String baseName, MapColor wood, MapColor bark, BlockSoundGroup sounds) {
		this(baseName, wood, bark, sounds, null);
	}


	public WoodType(String baseName, MapColor wood, MapColor bark, BlockSoundGroup sounds, @Nullable Block fungusBaseBlock) {
		super();
		this.baseName = baseName;

		fungus = fungusBaseBlock != null;

		final var blockSettings = QuiltBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(sounds).mapColor(wood);
		final var nonCollidable = QuiltBlockSettings.copyOf(blockSettings).collidable(false);
		final var nonOpaque = QuiltBlockSettings.copyOf(blockSettings).nonOpaque();
		final var hanging = QuiltBlockSettings
			.copyOf(Blocks.OAK_HANGING_SIGN)
			.sounds(sounds)
			.mapColor(wood);
		final var itemSettings = new QuiltItemSettings();
		final var singleStack = new QuiltItemSettings().maxCount(1);

		final var logName = fungus ? "stem" : "log";
		final var woodName = fungus ? "hyphae" : "wood";

		STRIPPED_LOG = BlockInitializer.registerBlock("stripped_"+baseName+"_"+logName, createLogBlock(wood, wood, sounds));
		STRIPPED_LOG_ITEM = ItemInitializer.registerItem("stripped_"+baseName+"_"+logName, new BlockItem(STRIPPED_LOG, itemSettings));

		LOG = BlockInitializer.registerBlock(baseName+"_"+logName, createLogBlock(bark, wood, sounds));
		LOG_ITEM = ItemInitializer.registerItem(baseName+"_"+logName, new BlockItem(LOG, itemSettings));

		BlockContentRegistries.STRIPPABLE.put(LOG, STRIPPED_LOG);

		STRIPPED_WOOD = BlockInitializer.registerBlock("stripped_"+baseName+"_"+woodName, createLogBlock(wood, wood, sounds));
		STRIPPED_WOOD_ITEM = ItemInitializer.registerItem("stripped_"+baseName+"_"+woodName, new BlockItem(STRIPPED_WOOD, itemSettings));

		WOOD = BlockInitializer.registerBlock(baseName+"_"+woodName, createLogBlock(bark, wood, sounds));
		WOOD_ITEM = ItemInitializer.registerItem(baseName+"_"+woodName, new BlockItem(WOOD, itemSettings));

		BlockContentRegistries.STRIPPABLE.put(WOOD, STRIPPED_WOOD);

		PLANKS = BlockInitializer.registerBlock(baseName+"_planks", new Block(blockSettings));
		PLANKS_ITEM = ItemInitializer.registerItem(baseName+"_planks", new BlockItem(PLANKS, itemSettings));

		STAIRS = BlockInitializer.registerBlock(baseName+"_stairs", new StairsBlock(PLANKS.getDefaultState(), blockSettings));
		STAIRS_ITEM = ItemInitializer.registerItem(baseName+"_stairs", new BlockItem(STAIRS, itemSettings));

		SLAB = BlockInitializer.registerBlock(baseName+"_slab", new SlabBlock(blockSettings));
		SLAB_ITEM = ItemInitializer.registerItem(baseName+"_slab", new BlockItem(SLAB, itemSettings));

		BUTTON = BlockInitializer.registerBlock(baseName+"_button", new ButtonBlock(nonCollidable, BlockSetType.OAK, 30, true));
		BUTTON_ITEM = ItemInitializer.registerItem(baseName+"_button", new BlockItem(BUTTON, itemSettings));

		PRESSURE_PLATE = BlockInitializer.registerBlock(baseName+"_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, nonCollidable, BlockSetType.OAK));
		PRESSURE_PLATE_ITEM = ItemInitializer.registerItem(baseName+"_pressure_plate", new BlockItem(PRESSURE_PLATE, itemSettings));

		DOOR = BlockInitializer.registerBlock(baseName+"_door", new DoorBlock(nonOpaque, BlockSetType.OAK));
		DOOR_ITEM = ItemInitializer.registerItem(baseName+"_door", new BlockItem(DOOR, itemSettings));

		TRAPDOOR = BlockInitializer.registerBlock(baseName+"_trapdoor", new TrapdoorBlock(nonOpaque, BlockSetType.OAK));
		TRAPDOOR_ITEM = ItemInitializer.registerItem(baseName+"_trapdoor", new BlockItem(TRAPDOOR, itemSettings));

		var sign_id = Mod.id("entity/signs/"+baseName);
		SIGN = BlockInitializer.registerBlock(baseName+"_sign", new TerraformSignBlock(sign_id, nonCollidable));
		SIGN_WALL = BlockInitializer.registerBlock(baseName+"_wall_sign", new TerraformWallSignBlock(sign_id, nonCollidable));
		SIGN_ITEM = ItemInitializer.registerItem(baseName+"_sign", new SignItem(itemSettings, SIGN, SIGN_WALL));

		var hanging_sign_id = Mod.id("entity/signs/hanging/"+baseName);
		var hanging_sign_gui_id = Mod.id("textures/gui/hanging_signs/"+baseName);
		HANGING_SIGN = BlockInitializer.registerBlock(baseName+"_hanging_sign", new TerraformHangingSignBlock(hanging_sign_id, hanging_sign_gui_id, hanging));
		HANGING_SIGN_WALL = BlockInitializer.registerBlock(baseName+"_wall_hanging_sign", new TerraformWallHangingSignBlock(hanging_sign_id, hanging_sign_gui_id, hanging));
		HANGING_SIGN_ITEM = ItemInitializer.registerItem(baseName+"_hanging_sign", new HangingSignItem(HANGING_SIGN, HANGING_SIGN_WALL, itemSettings));

		FENCE = BlockInitializer.registerBlock(baseName+"_fence", new FenceBlock(blockSettings));
		FENCE_ITEM = ItemInitializer.registerItem(baseName+"_fence", new BlockItem(FENCE, itemSettings));

		FENCE_GATE = BlockInitializer.registerBlock(baseName+"_fence_gate", new FenceGateBlock(blockSettings, SignType.OAK));
		FENCE_GATE_ITEM = ItemInitializer.registerItem(baseName+"_fence_gate", new BlockItem(FENCE_GATE, itemSettings));

		if (!fungus) {
			LEAVES = BlockInitializer.registerBlock(baseName+"_leaves", createLeavesBlock());
			LEAVES_ITEM = ItemInitializer.registerItem(baseName+"_leaves", new BlockItem(LEAVES, itemSettings));

			SAPLING = BlockInitializer.registerBlock(baseName+"_sapling", createSaplingBlock(baseName, baseName+"_bees"));
			SAPLING_ITEM = ItemInitializer.registerItem(baseName+"_sapling", new BlockItem(SAPLING, itemSettings));

			BOAT_KEY = TerraformBoatTypeRegistry.createKey(Mod.id(baseName));
			BOAT_ITEM = ItemInitializer.registerBoatItem(baseName+"_boat", BOAT_KEY, false, singleStack);
			BOAT_CHEST_ITEM = ItemInitializer.registerBoatItem(baseName+"_chest_boat", BOAT_KEY, true, singleStack);
			BOAT = Registry.register(TerraformBoatTypeRegistry.INSTANCE, BOAT_KEY, new TerraformBoatType.Builder().planks(PLANKS_ITEM).item(BOAT_ITEM).chestItem(BOAT_CHEST_ITEM).build());
		} else {
			LEAVES = BlockInitializer.registerBlock(baseName+"_wart", new Block(QuiltBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)));
			LEAVES_ITEM = ItemInitializer.registerItem(baseName+"_wart", new BlockItem(LEAVES, itemSettings));

			SAPLING = BlockInitializer.registerBlock(baseName+"_fungus", createFungusBlock(baseName, fungusBaseBlock));
			SAPLING_ITEM = ItemInitializer.registerItem(baseName+"_fungus", new BlockItem(SAPLING, itemSettings));

			BOAT_KEY = null;
			BOAT_ITEM = null;
			BOAT_CHEST_ITEM = null;
			BOAT = null;
		}
	}

	private static FungusBlock createFungusBlock(String baseName, Block base) {
		return new RootedMushroomBlock(
			QuiltBlockSettings
				.copyOf(Blocks.CRIMSON_FUNGUS)
				.breakInstantly()
				.noCollision()
				.sounds(BlockSoundGroup.FUNGUS)
				.offsetType(AbstractBlock.OffsetType.XZ),
			baseName,
			base
		);
	}

	private static SaplingBlock createSaplingBlock(String noBees, @Nullable String bees) {
		return new SaplingBlock(
			new BeeHoldingSaplingGenerator(noBees, bees),
			QuiltBlockSettings
				.copyOf(Blocks.OAK_SAPLING)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
		);
	}

	private static LeavesBlock createLeavesBlock() {
		return new LeavesBlock(
				QuiltBlockSettings
					.copyOf(Blocks.OAK_LEAVES)
					.strength(0.2F)
					.ticksRandomly()
					.sounds(BlockSoundGroup.AZALEA_LEAVES)
					.nonOpaque()
					.allowsSpawning((a,b,c, type) -> type == EntityType.OCELOT || type == EntityType.PARROT)
					.suffocates((a,b,c) -> false)
					.blockVision((a,b,c) -> false)
		);
	}

	private static Block createLogBlock(MapColor top, MapColor side, BlockSoundGroup sounds) {
		return new PillarBlock(
			QuiltBlockSettings.copyOf(Blocks.OAK_LOG)
				.strength(2.0F)
				.sounds(sounds)
				.mapColor((state) -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? top : side)
		);
	}

	@Override
	public void onRegisterPack(@NotNull PackRegistrationContext context) {
		var manager = context.resourceManager();
		if (!(manager instanceof MultiPackResourceManager multiManager)) return;
		var pack = DatagenInitializer.pack;
		var blockstates = new BlockstateDataApplier(context, baseName, fungus);
		var blockModels = new BlockModelDataApplier(context, baseName, fungus);
		var itemModels = new ItemModelDataApplier(context, baseName, fungus);
		blockstates.addToResourcePack(pack);
		blockModels.addToResourcePack(pack);
		itemModels.addToResourcePack(pack);
	}

	public static class BlockstateDataApplier extends AbstractDataGenerator.AbstractBlockstateDataApplier {
		public final String BUTTON;
		public final String DOOR;
		public final String FENCE;
		public final String FENCE_GATE;
		public final String LOG;
		public final String PLANKS;
		public final String PRESSURE_PLATE;
		public final String SIGN;
		public final String HANGING_SIGN;
		public final String SLAB;
		public final String STAIRS;
		public final String STRIPPED_LOG;
		public final String STRIPPED_WOOD;
		public final String TRAPDOOR;
		public final String WOOD;
		public final String LEAVES;

		final boolean fungus;

		public BlockstateDataApplier(@NotNull PackRegistrationContext context, String baseName, boolean fungus) {
			super(context, baseName, "wood");

			this.fungus = fungus;

			BUTTON = getResource("button");
			DOOR = getResource("door");
			FENCE = getResource("fence");
			FENCE_GATE = getResource("fence_gate");
			LOG = getResource("log");
			PLANKS = getResource("planks");
			PRESSURE_PLATE = getResource("pressure_plate");
			SIGN = getResource("sign");
			HANGING_SIGN = getResource("hanging_sign");
			SLAB = getResource("slab");
			STAIRS = getResource("stairs");
			STRIPPED_LOG = getResource("stripped_log");
			STRIPPED_WOOD = getResource("stripped_wood");
			TRAPDOOR = getResource("trapdoor");
			WOOD = getResource("wood");
			LEAVES = getResource("leaves");
		}

		@Override
		public void addToResourcePack(InMemoryPack pack) {
			final var logName = fungus ? "stem" : "log";
			final var woodName = fungus ? "hyphae" : "wood";
			final var leavesName = fungus ? "wart" : "leaves";
			final var saplingName = fungus ? "fungus" : "sapling";

			put(pack, baseName+"_button", BUTTON);
			put(pack, baseName+"_door", DOOR);
			put(pack, baseName+"_fence", FENCE);
			put(pack, baseName+"_fence_gate", FENCE_GATE);
			put(pack, baseName+"_"+logName, LOG.replaceAll("#", logName));
			put(pack, baseName+"_planks", PLANKS);
			put(pack, baseName+"_pressure_plate", PRESSURE_PLATE);
			put(pack, baseName+"_sign", SIGN);
			put(pack, baseName+"_wall_sign", SIGN);
			put(pack, baseName+"_hanging_sign", HANGING_SIGN);
			put(pack, baseName+"_wall_hanging_sign", HANGING_SIGN);
			put(pack, baseName+"_slab", SLAB);
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, "stripped_"+baseName+"_"+logName, STRIPPED_LOG.replaceAll("#", logName));
			put(pack, "stripped_"+baseName+"_"+woodName, STRIPPED_WOOD.replaceAll("#", woodName));
			put(pack, baseName+"_trapdoor", TRAPDOOR);
			put(pack, baseName+"_"+woodName, WOOD.replaceAll("#", woodName));
			put(pack, baseName+"_"+leavesName, LEAVES.replaceAll("#", leavesName));
			put(pack, baseName+"_"+saplingName, LEAVES.replaceAll("#", saplingName));
		}
	}

	public static class BlockModelDataApplier extends AbstractBlockModelDataApplier {
		public final String BUTTON;
		public final String DOOR;
		public final String FENCE;
		public final String FENCE_GATE;
		public final String LOG;
		public final String PLANKS;
		public final String PRESSURE_PLATE;
		public final String SIGN;
		public final String HANGING_SIGN;
		public final String SLAB;
		public final String STAIRS;
		public final String STRIPPED_LOG;
		public final String STRIPPED_WOOD;
		public final String TRAPDOOR;
		public final String WOOD;
		public final String LEAVES;
		public final String SAPLING;

		final boolean fungus;

		public BlockModelDataApplier(@NotNull PackRegistrationContext context, String baseName, boolean fungus) {
			super(context, baseName, "wood");

			this.fungus = fungus;

			BUTTON = getResource("button");
			DOOR = getResource("door");
			FENCE = getResource("fence");
			FENCE_GATE = getResource("fence_gate");
			LOG = getResource("log");
			PLANKS = getResource("planks");
			PRESSURE_PLATE = getResource("pressure_plate");
			SIGN = getResource("sign");
			HANGING_SIGN = getResource("hanging_sign");
			SLAB = getResource("slab");
			STAIRS = getResource("stairs");
			STRIPPED_LOG = getResource("stripped_log");
			STRIPPED_WOOD = getResource("stripped_wood");
			TRAPDOOR = getResource("trapdoor");
			WOOD = getResource("wood");
			LEAVES = getResource("leaves");
			SAPLING = getResource("sapling");
		}

		@Override
		public void addToResourcePack(InMemoryPack pack) {
			final var logName = fungus ? "stem" : "log";
			final var woodName = fungus ? "hyphae" : "wood";
			final var leavesName = fungus ? "wart" : "leaves";
			final var saplingName = fungus ? "fungus" : "sapling";

			put(pack, baseName+"_button", BUTTON, null);
			put(pack, baseName+"_button", BUTTON, "inventory");
			put(pack, baseName+"_button", BUTTON, "pressed");
			put(pack, baseName+"_door", DOOR, "bottom_left");
			put(pack, baseName+"_door", DOOR, "bottom_left_open");
			put(pack, baseName+"_door", DOOR, "bottom_right");
			put(pack, baseName+"_door", DOOR, "bottom_right_open");
			put(pack, baseName+"_door", DOOR, "top_left");
			put(pack, baseName+"_door", DOOR, "top_left_open");
			put(pack, baseName+"_door", DOOR, "top_right");
			put(pack, baseName+"_door", DOOR, "top_right_open");
			put(pack, baseName+"_fence_gate", FENCE_GATE, null);
			put(pack, baseName+"_fence_gate", FENCE_GATE, "open");
			put(pack, baseName+"_fence_gate", FENCE_GATE, "wall");
			put(pack, baseName+"_fence_gate", FENCE_GATE, "wall_open");
			put(pack, baseName+"_fence", FENCE, "inventory");
			put(pack, baseName+"_fence", FENCE, "post");
			put(pack, baseName+"_fence", FENCE, "side");
			put(pack, baseName+"_"+logName, LOG.replaceAll("#", logName), null);
			put(pack, baseName+"_"+logName, LOG.replaceAll("#", logName), "horizontal");
			put(pack, baseName+"_planks", PLANKS);
			put(pack, baseName+"_pressure_plate", PRESSURE_PLATE);
			put(pack, baseName+"_pressure_plate_down", PRESSURE_PLATE.replace("up", "down"));
			put(pack, baseName+"_sign", SIGN);
			put(pack, baseName+"_hanging_sign", HANGING_SIGN.replaceAll("#", logName));
			put(pack, baseName+"_slab", SLAB, null);
			put(pack, baseName+"_slab", SLAB, "top");
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, baseName+"_stairs_inner", STAIRS.replace("stairs", "inner_stairs"));
			put(pack, baseName+"_stairs_outer", STAIRS.replace("stairs", "outer_stairs"));
			put(pack, "stripped_"+baseName+"_"+logName, STRIPPED_LOG.replaceAll("#", logName), null);
			put(pack, "stripped_"+baseName+"_"+logName, STRIPPED_LOG.replaceAll("#", logName), "horizontal");
			put(pack, "stripped_"+baseName+"_"+woodName, STRIPPED_WOOD.replaceAll("#", logName), null);
			put(pack, "stripped_"+baseName+"_"+woodName, STRIPPED_WOOD.replaceAll("#", logName), "horizontal");
			put(pack, baseName+"_trapdoor", TRAPDOOR, "bottom");
			put(pack, baseName+"_trapdoor", TRAPDOOR, "open");
			put(pack, baseName+"_trapdoor", TRAPDOOR, "top");
			put(pack, baseName+"_"+woodName, WOOD.replaceAll("#", logName), null);
			put(pack, baseName+"_"+woodName, WOOD.replaceAll("#", logName), "horizontal");
			put(pack, baseName+"_"+leavesName, LEAVES.replaceAll("#", leavesName));
			put(pack, baseName+"_"+saplingName, SAPLING.replaceAll("#", saplingName));
		}
	}

	public static class ItemModelDataApplier extends AbstractDataGenerator.AbstractItemModelDataApplier {
		public final String BUTTON;
		public final String DOOR;
		public final String FENCE;
		public final String FENCE_GATE;
		public final String LOG;
		public final String PLANKS;
		public final String PRESSURE_PLATE;
		public final String SIGN;
		public final String HANGING_SIGN;
		public final String SLAB;
		public final String STAIRS;
		public final String STRIPPED_LOG;
		public final String STRIPPED_WOOD;
		public final String TRAPDOOR;
		public final String WOOD;
		public final String LEAVES;
		public final String SAPLING;
		public final String BOAT;

		final boolean fungus;

		public ItemModelDataApplier(@NotNull PackRegistrationContext context, String baseName, boolean fungus) {
			super(context, baseName, "wood");
			this.fungus = fungus;

			BUTTON = getResource("button");
			DOOR = getResource("door");
			FENCE = getResource("fence");
			FENCE_GATE = getResource("fence_gate");
			LOG = getResource("log");
			PLANKS = getResource("planks");
			PRESSURE_PLATE = getResource("pressure_plate");
			SIGN = getResource("sign");
			HANGING_SIGN = getResource("hanging_sign");
			SLAB = getResource("slab");
			STAIRS = getResource("stairs");
			STRIPPED_LOG = getResource("stripped_log");
			STRIPPED_WOOD = getResource("stripped_wood");
			TRAPDOOR = getResource("trapdoor");
			WOOD = getResource("wood");
			LEAVES = getResource("leaves");
			SAPLING = getResource("sapling");
			BOAT = getResource("boat");
		}

		@Override
		public void addToResourcePack(InMemoryPack pack) {
			final var logName = fungus ? "stem" : "log";
			final var woodName = fungus ? "hyphae" : "wood";
			final var leavesName = fungus ? "wart" : "leaves";
			final var saplingName = fungus ? "fungus" : "sapling";

			put(pack, baseName+"_button", BUTTON);
			put(pack, baseName+"_door", DOOR);
			put(pack, baseName+"_fence", FENCE);
			put(pack, baseName+"_fence_gate", FENCE_GATE);
			put(pack, baseName+"_"+logName, LOG.replaceAll("#", logName));
			put(pack, baseName+"_planks", PLANKS);
			put(pack, baseName+"_pressure_plate", PRESSURE_PLATE);
			put(pack, baseName+"_sign", SIGN);
			put(pack, baseName+"_hanging_sign", HANGING_SIGN);
			put(pack, baseName+"_slab", SLAB);
			put(pack, baseName+"_stairs", STAIRS);
			put(pack, "stripped_"+baseName+"_"+logName, STRIPPED_LOG.replaceAll("#", logName));
			put(pack, "stripped_"+baseName+"_"+woodName, STRIPPED_WOOD.replaceAll("#", woodName));
			put(pack, baseName+"_trapdoor", TRAPDOOR);
			put(pack, baseName+"_"+woodName, WOOD.replaceAll("#", woodName));
			put(pack, baseName+"_"+leavesName, LEAVES.replaceAll("#", leavesName));
			put(pack, baseName+"_"+saplingName, SAPLING.replaceAll("#", saplingName));
			put(pack, baseName+"_boat", BOAT.replaceAll("\\$", ""));
			put(pack, baseName+"_chest_boat", BOAT.replaceAll("\\$", "_chest"));
		}
	}
}
