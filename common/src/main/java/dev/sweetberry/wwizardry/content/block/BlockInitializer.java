package dev.sweetberry.wwizardry.content.block;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.LogicGateBlockEntity;
import dev.sweetberry.wwizardry.content.block.nature.FallingDecayableBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedFlowerBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedPlantBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.LogicGateBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import dev.sweetberry.wwizardry.mixin.Accessor_AxeItem;
import dev.sweetberry.wwizardry.mixin.Accessor_BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockInitializer {
	public static final RegistryContext<Block> BLOCKS = new RegistryContext<>(BuiltInRegistries.BLOCK);
	public static final RegistryContext<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryContext<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);

	public static final Block INDIGO_CAERULEUM = registerBlock("indigo_caeruleum", new RootedFlowerBlock(MobEffects.INVISIBILITY, 20, "mycha_growable", BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).requiresCorrectToolForDrops()));

	public static final Block REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane", new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).requiresCorrectToolForDrops()));

	public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern", new RedstoneLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP)));

	public static final Block ROSE_QUARTZ_ORE = registerBlock("rose_quartz_ore", new DropExperienceBlock(UniformInt.of(1,4), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
	public static final Block DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock("deepslate_rose_quartz_ore", new DropExperienceBlock(UniformInt.of(1,4), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
	public static final Block ROSE_QUARTZ_BLOCK = registerBlock("rose_quartz_block", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));

	public static final Block MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		new LogicGateBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.COMPARATOR),
			LogicGateBlock.SideInput.ALL,
			true,
			(state, mode, side, back) -> {
				int value = side == 0 ? 0 : back % side;
				if (mode == ComparatorMode.SUBTRACT)
					value = back - value;
				return value;
			}
		)
	);

	public static final Block REDSTONE_STEPPER = registerBlock(
		"redstone_stepper",
		new LogicGateBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER),
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		)
	);

	public static final Block MYCELIAL_SAND = registerBlock(
		"mycelial_sand",
		new FallingDecayableBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).mapColor(MapColor.ICE),
			Blocks.SAND,
			"mycha_spread"
		)
	);

	public static final Block MYCHA_ROOTS = registerBlock(
		"mycha_roots",
		new RootedPlantBlock(
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.NETHER)
				.replaceable()
				.noCollission()
				.instabreak()
				.sound(SoundType.ROOTS)
				.offsetType(BlockBehaviour.OffsetType.XZ)
				.pushReaction(PushReaction.DESTROY),
			"mycha"
		)
	);

	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlock("sculk_resonator", ResonatorBlock.INSTANCE);
		registerBlock("sculkflower", SculkflowerBlock.INSTANCE);
		registerBlock("crystalline_sculk_block", CrystalSculkBlock.INSTANCE);
		registerBlock("camera", CameraBlock.INSTANCE);
		registerBlock("wall_holder", WallHolderBlock.EMPTY);

		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
		registerBlockEntity("extensible_comparator", LogicGateBlockEntity.TYPE);
	}

	public static <T extends Block> T registerBlock(String id, T block) {
		return (T) BLOCKS.register(WanderingWizardry.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return (BlockEntityType<T>) BLOCK_ENTITIES.register(WanderingWizardry.id(id), blockEntity);
	}

	public static void addSignBlocks(Block... blocks) {
		addBlocksToType(BlockEntityType.SIGN, blocks);
	}

	public static void addHangingSignBlocks(Block... blocks) {
		addBlocksToType(BlockEntityType.HANGING_SIGN, blocks);
	}

	private static void addBlocksToType(BlockEntityType<?> type, Block... blocks) {
		var set = getBlocksForType(type);
		for (var block : blocks)
			set.add(block);
	}

	private static Set<Block> getBlocksForType(BlockEntityType<?> oldType) {
		var type = ((Accessor_BlockEntityType)oldType);
		var old = type.getValidBlocks();
		if (old instanceof HashSet<Block>)
			return old;
		var out = new HashSet<>(old);
		type.setValidBlocks(out);
		return out;
	}

	public static void addStrippedBlock(Block base, Block stripped) {
		getSrippedBlocks().put(base, stripped);
	}

	private static Map<Block, Block> getSrippedBlocks() {
		var old = Accessor_AxeItem.getStrippedBlocks();
		if (old instanceof HashMap<Block, Block>)
			return old;
		var map = new HashMap<>(old);
		Accessor_AxeItem.setStrippedBlocks(map);
		return map;
	}
}
