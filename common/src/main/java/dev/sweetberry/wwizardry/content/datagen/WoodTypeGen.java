package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.world.sapling.BeeHoldingSaplingGenerator;
import dev.sweetberry.wwizardry.content.block.nature.RootedMushroomBlock;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.mixin.Accessor_BlockSetType;
import dev.sweetberry.wwizardry.mixin.Accessor_WoodType;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class WoodTypeGen extends AbstractDataGenerator {
	public final String baseName;

	public final Lazy<BlockSetType> BLOCK_SET;
	public final Lazy<WoodType> TYPE;

	public final Lazy<Block> LOG;
	public final Lazy<Item> LOG_ITEM;
	public final Lazy<Block> STRIPPED_LOG;
	public final Lazy<Item> STRIPPED_LOG_ITEM;
	public final Lazy<Block> WOOD;
	public final Lazy<Item> WOOD_ITEM;
	public final Lazy<Block> STRIPPED_WOOD;
	public final Lazy<Item> STRIPPED_WOOD_ITEM;
	public final Lazy<Block> PLANKS;
	public final Lazy<Item> PLANKS_ITEM;
	public final Lazy<Block> STAIRS;
	public final Lazy<Item> STAIRS_ITEM;
	public final Lazy<Block> SLAB;
	public final Lazy<Item> SLAB_ITEM;
	public final Lazy<Block> BUTTON;
	public final Lazy<Item> BUTTON_ITEM;
	public final Lazy<Block> PRESSURE_PLATE;
	public final Lazy<Item> PRESSURE_PLATE_ITEM;
	public final Lazy<Block> DOOR;
	public final Lazy<Item> DOOR_ITEM;
	public final Lazy<Block> TRAPDOOR;
	public final Lazy<Item> TRAPDOOR_ITEM;
	public final Lazy<StandingSignBlock> SIGN;
	public final Lazy<WallSignBlock> SIGN_WALL;
	public final Lazy<Item> SIGN_ITEM;
	public final Lazy<CeilingHangingSignBlock> HANGING_SIGN;
	public final Lazy<WallHangingSignBlock> HANGING_SIGN_WALL;
	public final Lazy<Item> HANGING_SIGN_ITEM;
	public final Lazy<Block> FENCE;
	public final Lazy<Item> FENCE_ITEM;
	public final Lazy<Block> FENCE_GATE;
	public final Lazy<Item> FENCE_GATE_ITEM;
	public final Lazy<Block> LEAVES;
	public final Lazy<Item> LEAVES_ITEM;
	public final Lazy<Block> SAPLING;
	public final Lazy<Item> SAPLING_ITEM;

	public final boolean fungus;

	public final Lazy<Item> BOAT_ITEM;

	public final Lazy<Item> BOAT_CHEST_ITEM;
	public WoodTypeGen(String baseName, MapColor wood, MapColor bark, SoundType sounds) {
		this(baseName, wood, bark, sounds, null);
	}

	@FunctionalInterface
	interface BlockBehaviorFunction extends Function<BlockBehaviour.Properties, BlockBehaviour.Properties> {}

	@SuppressWarnings("unchecked")
	public WoodTypeGen(String baseName, MapColor wood, MapColor bark, SoundType sounds, @Nullable Supplier<Block> fungusBaseBlock) {
		super();
		this.baseName = baseName;

		fungus = fungusBaseBlock != null;

		final BlockBehaviorFunction defaultBlockProperties = (it) -> it
			.sound(sounds)
			.mapColor(wood)
			.instrument(NoteBlockInstrument.BASS)
			.ignitedByLava();

		final BlockBehaviorFunction nonCollidableBlockProperties = (it) -> defaultBlockProperties.apply(it)
			.noCollission();

		final BlockBehaviorFunction nonOpaqueBlockProperties = (it) -> defaultBlockProperties.apply(it)
			.noOcclusion();

		final BlockBehaviorFunction signBlockProperties = (it) -> it
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollission()
			.strength(1.0F)
			.ignitedByLava()
			.sound(sounds)
			.mapColor(wood);

		final BlockBehaviorFunction hangingSignBlockProperties = (it) -> it
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollission()
			.strength(1.0F)
			.ignitedByLava()
			.sound(sounds)
			.mapColor(wood);
		final var itemSettings = new Item.Properties();
		final var singleStack = new Item.Properties().stacksTo(1);

		final var typeName = WanderingWizardry.id(baseName).toString();

		BLOCK_SET = createBlockSetType(typeName, () -> new BlockSetType(typeName, true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING, sounds, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON));
		TYPE = createWoodType(typeName, () -> new WoodType(typeName, BLOCK_SET.get(), sounds, SoundType.HANGING_SIGN, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN));

		final var logName = fungus ? "stem" : "log";
		final var woodName = fungus ? "hyphae" : "wood";

		STRIPPED_LOG = BlockInitializer.registerBlock("stripped_"+baseName+"_"+logName, (properties) -> createLogBlock(properties, wood, wood, sounds));
		STRIPPED_LOG_ITEM = ItemInitializer.registerItem("stripped_"+baseName+"_"+logName, () -> new BlockItem(STRIPPED_LOG.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		LOG = BlockInitializer.registerBlock(baseName+"_"+logName, (properties) -> createLogBlock(properties, bark, wood, sounds));
		LOG_ITEM = ItemInitializer.registerItem(baseName+"_"+logName, () -> new BlockItem(LOG.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		BlockInitializer.addStrippedBlock(LOG, STRIPPED_LOG);

		STRIPPED_WOOD = BlockInitializer.registerBlock("stripped_"+baseName+"_"+woodName, (properties) -> createLogBlock(properties, wood, wood, sounds));
		STRIPPED_WOOD_ITEM = ItemInitializer.registerItem("stripped_"+baseName+"_"+woodName, () -> new BlockItem(STRIPPED_WOOD.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		WOOD = BlockInitializer.registerBlock(baseName+"_"+woodName, (properties) -> createLogBlock(properties, bark, wood, sounds));
		WOOD_ITEM = ItemInitializer.registerItem(baseName+"_"+woodName, () -> new BlockItem(WOOD.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		BlockInitializer.addStrippedBlock(WOOD, STRIPPED_WOOD);

		PLANKS = BlockInitializer.registerBlock(baseName+"_planks", (properties) -> new Block(defaultBlockProperties.apply(properties)));
		PLANKS_ITEM = ItemInitializer.registerItem(baseName+"_planks", () -> new BlockItem(PLANKS.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		STAIRS = BlockInitializer.registerBlock(baseName+"_stairs", (properties) -> new StairBlock(PLANKS.get().defaultBlockState(), defaultBlockProperties.apply(properties)));
		STAIRS_ITEM = ItemInitializer.registerItem(baseName+"_stairs", () -> new BlockItem(STAIRS.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		SLAB = BlockInitializer.registerBlock(baseName+"_slab", (properties) -> new SlabBlock(defaultBlockProperties.apply(properties)));
		SLAB_ITEM = ItemInitializer.registerItem(baseName+"_slab", () -> new BlockItem(SLAB.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		BUTTON = BlockInitializer.registerBlock(baseName+"_button", (properties) -> new ButtonBlock(BLOCK_SET.get(), 30, nonCollidableBlockProperties.apply(properties)));
		BUTTON_ITEM = ItemInitializer.registerItem(baseName+"_button", () -> new BlockItem(BUTTON.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		PRESSURE_PLATE = BlockInitializer.registerBlock(baseName+"_pressure_plate", (properties) -> new PressurePlateBlock(BLOCK_SET.get(), nonCollidableBlockProperties.apply(properties)));
		PRESSURE_PLATE_ITEM = ItemInitializer.registerItem(baseName+"_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		DOOR = BlockInitializer.registerBlock(baseName+"_door", (properties) -> new DoorBlock(BLOCK_SET.get(), nonOpaqueBlockProperties.apply(properties)));
		DOOR_ITEM = ItemInitializer.registerItem(baseName+"_door", () -> new BlockItem(DOOR.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		TRAPDOOR = BlockInitializer.registerBlock(baseName+"_trapdoor", (properties) -> new TrapDoorBlock(BLOCK_SET.get(), nonOpaqueBlockProperties.apply(properties)));
		TRAPDOOR_ITEM = ItemInitializer.registerItem(baseName+"_trapdoor", () -> new BlockItem(TRAPDOOR.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		SIGN = BlockInitializer.registerBlock(baseName+"_sign",(properties) ->  new StandingSignBlock(TYPE.get(), signBlockProperties.apply(properties)));
		SIGN_WALL = BlockInitializer.registerBlock(baseName+"_wall_sign", (properties) -> new WallSignBlock(TYPE.get(), signBlockProperties.apply(properties)));
		BlockInitializer.addSignBlocks((Lazy<Block>)(Object)SIGN, (Lazy<Block>)(Object)SIGN_WALL);
		SIGN_ITEM = ItemInitializer.registerItem(baseName+"_sign", () -> new SignItem(itemSettings, SIGN.get(), SIGN_WALL.get()), ItemInitializer.BLOCKS_STACKS);

		HANGING_SIGN = BlockInitializer.registerBlock(baseName+"_hanging_sign", (properties) -> new CeilingHangingSignBlock(TYPE.get(), hangingSignBlockProperties.apply(properties)));
		HANGING_SIGN_WALL = BlockInitializer.registerBlock(baseName+"_wall_hanging_sign", (properties) -> new WallHangingSignBlock(TYPE.get(), hangingSignBlockProperties.apply(properties)));
		BlockInitializer.addHangingSignBlocks((Lazy<Block>)(Object)HANGING_SIGN, (Lazy<Block>)(Object)HANGING_SIGN_WALL);
		HANGING_SIGN_ITEM = ItemInitializer.registerItem(baseName+"_hanging_sign", () -> new HangingSignItem(HANGING_SIGN.get(), HANGING_SIGN_WALL.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		FENCE = BlockInitializer.registerBlock(baseName+"_fence", (properties) -> new FenceBlock(defaultBlockProperties.apply(properties)));
		FENCE_ITEM = ItemInitializer.registerItem(baseName+"_fence", () -> new BlockItem(FENCE.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		FENCE_GATE = BlockInitializer.registerBlock(baseName+"_fence_gate", (properties) -> new FenceGateBlock(TYPE.get(), defaultBlockProperties.apply(properties)));
		FENCE_GATE_ITEM = ItemInitializer.registerItem(baseName+"_fence_gate", () -> new BlockItem(FENCE_GATE.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

		if (!fungus) {
			LEAVES = BlockInitializer.registerBlock(baseName+"_leaves", WoodTypeGen::createLeavesBlock);
			LEAVES_ITEM = ItemInitializer.registerItem(baseName+"_leaves", () -> new BlockItem(LEAVES.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

			SAPLING = BlockInitializer.registerBlock(baseName+"_sapling", (properties) -> createSaplingBlock(properties, WanderingWizardry.id(baseName).toString(), baseName, baseName+"_bees"));
			SAPLING_ITEM = ItemInitializer.registerItem(baseName+"_sapling", () -> new BlockItem(SAPLING.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

			BOAT_ITEM = ItemInitializer.registerBoatItem(baseName+"_boat", WanderingWizardry.id(baseName), false, singleStack);
			BOAT_CHEST_ITEM = ItemInitializer.registerBoatItem(baseName+"_chest_boat", WanderingWizardry.id(baseName), true, singleStack);
		} else {
			LEAVES = BlockInitializer.registerBlock(baseName+"_wart", (properties) -> new Block(properties.mapColor(MapColor.COLOR_RED).strength(1.0F).sound(SoundType.WART_BLOCK)));
			LEAVES_ITEM = ItemInitializer.registerItem(baseName+"_wart", () -> new BlockItem(LEAVES.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

			SAPLING = BlockInitializer.registerBlock(baseName+"_fungus", (properties) -> createFungusBlock(properties, baseName, fungusBaseBlock.get()));
			SAPLING_ITEM = ItemInitializer.registerItem(baseName+"_fungus", () -> new BlockItem(SAPLING.get(), itemSettings), ItemInitializer.BLOCKS_STACKS);

			BOAT_ITEM = null;
			BOAT_CHEST_ITEM = null;
		}
	}

	private static Lazy<BlockSetType> createBlockSetType(String name, Supplier<BlockSetType> supplier) {
		return Lazy.create(() -> {
			var value = supplier.get();
			Accessor_BlockSetType.getTYPES().put(name, value);
			return value;
		});
	}

	private static Lazy<WoodType> createWoodType(String name, Supplier<WoodType> supplier) {
		return Lazy.create(() -> {
			var value = supplier.get();
			Accessor_WoodType.getTYPES().put(name, value);
			return value;
		});
	}

	private static FungusBlock createFungusBlock(BlockBehaviour.Properties properties, String baseName, Block base) {
		return new RootedMushroomBlock(
			properties
				.mapColor(MapColor.NETHER)
				.pushReaction(PushReaction.DESTROY)
				.instabreak()
				.noCollission()
				.sound(SoundType.FUNGUS)
				.offsetType(BlockBehaviour.OffsetType.XZ),
			baseName,
			base
		);
	}

	private static SaplingBlock createSaplingBlock(BlockBehaviour.Properties properties, String name, String noBees, @Nullable String bees) {
		return new SaplingBlock(
			BeeHoldingSaplingGenerator.create(name, noBees, bees),
			properties
				.mapColor(MapColor.PLANT)
				.noCollission()
				.randomTicks()
				.instabreak()
				.sound(SoundType.GRASS)
				.pushReaction(PushReaction.DESTROY)
		);
	}

	private static LeavesBlock createLeavesBlock(BlockBehaviour.Properties properties) {
		return new LeavesBlock(
				properties
					.mapColor(MapColor.PLANT)
					.strength(0.2F)
					.randomTicks()
					.sound(SoundType.AZALEA_LEAVES)
					.noOcclusion()
					.isValidSpawn((a,b,c, type) -> type == EntityType.OCELOT || type == EntityType.PARROT)
					.isSuffocating((a,b,c) -> false)
					.isViewBlocking((a,b,c) -> false)
		);
	}

	private static Block createLogBlock(BlockBehaviour.Properties properties, MapColor top, MapColor side, SoundType sounds) {
		return new RotatedPillarBlock(
			properties
				.strength(2.0F)
				.instrument(NoteBlockInstrument.BASS)
				.ignitedByLava()
				.sound(sounds)
				.mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side)
		);
	}

	@Override
	public void onRegisterPack(@NotNull ResourceManager manager, MapBackedPack pack) {
		var blockstates = new BlockstateDataApplier(manager, baseName, fungus);
		var blockModels = new BlockModelDataApplier(manager, baseName, fungus);
		var itemModels = new ItemModelDataApplier(manager, baseName, fungus);
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

		public BlockstateDataApplier(@NotNull ResourceManager manager, String baseName, boolean fungus) {
			super(manager, baseName, "wood");

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
		public void addToResourcePack(MapBackedPack pack) {
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

		public BlockModelDataApplier(@NotNull ResourceManager manager, String baseName, boolean fungus) {
			super(manager, baseName, "wood");

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
		public void addToResourcePack(MapBackedPack pack) {
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

		public ItemModelDataApplier(@NotNull ResourceManager manager, String baseName, boolean fungus) {
			super(manager, baseName, "wood");
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
		public void addToResourcePack(MapBackedPack pack) {
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
