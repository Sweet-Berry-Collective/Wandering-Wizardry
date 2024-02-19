package dev.sweetberry.wwizardry.content.block;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class BlockInitializer {
	public static final RegistryContext<Block> BLOCKS = new RegistryContext<>(BuiltInRegistries.BLOCK);
	public static final RegistryContext<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryContext<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);

	public static final Lazy<Block> INDIGO_CAERULEUM = registerBlock(
		"indigo_caeruleum",
		() -> new RootedFlowerBlock(
			MobEffects.INVISIBILITY,
			20,
			"mycha_growable",
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.POPPY)
		)
	);

	public static final Lazy<Block> REINFORCED_GLASS = registerBlock(
		"reinforced_glass",
		() -> new TransparentBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.GLASS)
				.requiresCorrectToolForDrops()
		)
	);

	public static final Lazy<Block> REINFORCED_GLASS_PANE = registerBlock(
		"reinforced_glass_pane",
		() -> new IronBarsBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.GLASS)
				.requiresCorrectToolForDrops()
		)
	);

	public static final Lazy<Block> REDSTONE_LANTERN = registerBlock(
		"redstone_lantern",
		() -> new RedstoneLampBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.REDSTONE_LAMP)
		)
	);

	public static final Lazy<Block> ROSE_QUARTZ_ORE = registerBlock(
		"rose_quartz_ore",
		() -> new DropExperienceBlock(
			UniformInt.of(1,4),
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.IRON_ORE)
		)
	);
	public static final Lazy<Block> DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock(
		"deepslate_rose_quartz_ore",
		() -> new DropExperienceBlock(
			UniformInt.of(1,4),
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)
		)
	);
	public static final Lazy<Block> ROSE_QUARTZ_BLOCK = registerBlock(
		"rose_quartz_block",
		() -> new Block(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.AMETHYST_BLOCK)
		)
	);

	public static final Lazy<Block> MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		() -> new LogicGateBlock(
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

	public static final Lazy<Block> REDSTONE_STEPPER = registerBlock(
		"redstone_stepper",
		() -> new LogicGateBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER),
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		)
	);

	public static final Lazy<Block> MYCELIAL_SAND = registerBlock(
		"mycelial_sand",
		() -> new FallingDecayableBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).mapColor(MapColor.ICE),
			Blocks.SAND,
			"mycha_spread"
		)
	);

	public static final Lazy<Block> MYCHA_ROOTS = registerBlock(
		"mycha_roots",
		() -> new RootedPlantBlock(
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

	public static final Lazy<Block> ALTAR_PEDESTAL = registerBlock(
		"altar_pedestal",
		() -> new AltarPedestalBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.REDSTONE_BLOCK)
		)
	);

	public static final Lazy<AltarCatalyzerBlock> ALTAR_CATALYZER = registerBlock(
		"altar_catalyzer",
		() -> new AltarCatalyzerBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.REDSTONE_BLOCK)
		)
	);

	public static final Lazy<Block> SCULK_RESONATOR = registerBlock(
		"sculk_resonator",
		() -> new ResonatorBlock(
			BlockBehaviour.Properties.of()
				.sound(SoundType.SCULK_SHRIEKER)
		)
	);

	public static final Lazy<Block> SCULKFLOWER = registerBlock(
		"sculkflower",
		() -> new SculkflowerBlock(
			MobEffects.DARKNESS,
			30,
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.POPPY)
				.offsetType(BlockBehaviour.OffsetType.NONE)
		)
	);

	public static final Lazy<Block> CRYSTALLINE_SCULK = registerBlock(
		"crystalline_sculk_block",
		() -> new CrystalSculkBlock(
			BlockBehaviour.Properties
				.ofFullCopy(Blocks.AMETHYST_BLOCK)
				.lightLevel((state) -> 1)
				.mapColor(MapColor.ICE)
		)
	);

	public static final Lazy<Block> CAMERA = registerBlock(
		"camera",
		() -> new CameraBlock(
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.COLOR_GRAY)
		)
	);

	public static final Lazy<Block> WALL_HOLDER = registerBlock(
		"wall_holder",
		() ->  new WallHolderBlock(
			BlockBehaviour.Properties.of()
				.instabreak()
				.mapColor(MapColor.COLOR_GRAY)
		)
	);


	public static final Lazy<BlockEntityType<AltarPedestalBlockEntity>> ALTAR_PEDESTAL_TYPE = registerBlockEntity(
		"altar_pedestal",
		() -> BlockEntityType.Builder
			.of(
				AltarPedestalBlockEntity::new,
				ALTAR_PEDESTAL.get()
			)
			.build(null)
	);

	public static final Lazy<BlockEntityType<AltarCatalyzerBlockEntity>> ALTAR_CATALYZER_TYPE = registerBlockEntity(
		"altar_catalyzer",
		() -> BlockEntityType.Builder.of(
			AltarCatalyzerBlockEntity::new,
			ALTAR_CATALYZER.get()
		).build(null)
	);

	public static final Lazy<BlockEntityType<LogicGateBlockEntity>> LOGIC_GATE_TYPE = registerBlockEntity(
		"extensible_comparator",
		() -> BlockEntityType.Builder
			.of(
				LogicGateBlockEntity::new,
				MODULO_COMPARATOR.get(),
				REDSTONE_STEPPER.get()
			).build(null)
	);


	public static <T extends Block> Lazy<T> registerBlock(String id, Supplier<T> block) {
		return (Lazy<T>) BLOCKS.register(WanderingWizardry.id(id), (Supplier<Block>) block);
	}

	public static <T extends BlockEntity> Lazy<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntity) {
		return (Lazy<BlockEntityType<T>>)(Object) BLOCK_ENTITIES.register(WanderingWizardry.id(id), (Supplier<BlockEntityType<?>>)(Object) blockEntity);
	}

	public static void addSignBlocks(Lazy<Block>... blocks) {
		addBlocksToType(BlockEntityType.SIGN, blocks);
	}

	public static void addHangingSignBlocks(Lazy<Block>... blocks) {
		addBlocksToType(BlockEntityType.HANGING_SIGN, blocks);
	}

	private static final Map<BlockEntityType<?>, Set<Lazy<Block>>> INTERNAL_BLOCKENTITY_MAP = new HashMap<>();

	private static void addBlocksToType(BlockEntityType<?> type, Lazy<Block>... blocks) {
		if (!INTERNAL_BLOCKENTITY_MAP.containsKey(type))
			INTERNAL_BLOCKENTITY_MAP.put(type, new HashSet<>());
		var set = INTERNAL_BLOCKENTITY_MAP.get(type);
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

	private static final Map<Lazy<Block>, Lazy<Block>> INTERNAL_STRIPPABLE_MAP = new HashMap<>();

	public static void addStrippedBlock(Lazy<Block> base, Lazy<Block> stripped) {
		INTERNAL_STRIPPABLE_MAP.put(base, stripped);
	}

	public static void registerSecondaryBlockFunctions() {
		var stripped = getSrippedBlocks();
		for (var set : INTERNAL_STRIPPABLE_MAP.entrySet())
			stripped.put(set.getKey().get(), set.getValue().get());
		for (var set : INTERNAL_BLOCKENTITY_MAP.entrySet())
			for (var block : set.getValue())
				getBlocksForType(set.getKey()).add(block.get());
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
