package dev.sweetberry.wwizardry.content.block;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.LogicGateBlockEntity;
import dev.sweetberry.wwizardry.content.block.nature.*;
import dev.sweetberry.wwizardry.content.block.redstone.CopperLensBlock;
import dev.sweetberry.wwizardry.content.block.redstone.LogicGateBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import dev.sweetberry.wwizardry.content.block.redstone.WeatheringCopperLensBlock;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import dev.sweetberry.wwizardry.mixin.Accessor_AxeItem;
import dev.sweetberry.wwizardry.mixin.Accessor_BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BlockInitializer {
	public static final RegistryContext<Block> BLOCKS = new RegistryContext<>(BuiltInRegistries.BLOCK);
	public static final RegistryContext<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryContext<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);

	public static final Lazy<Block> INDIGO_CAERULEUM = registerBlock(
		"indigo_caeruleum",
		(p) -> new RootedFlowerBlock(
			MobEffects.INVISIBILITY,
			20,
			"mycha_growable",
			p
		),
		Blocks.POPPY
	);

	public static final Lazy<Block> REINFORCED_GLASS = registerBlock(
		"reinforced_glass",
		(p) -> new TransparentBlock(
			p
				.requiresCorrectToolForDrops()
		),
		Blocks.GLASS
	);

	public static final Lazy<Block> REINFORCED_GLASS_PANE = registerBlock(
		"reinforced_glass_pane",
		(p) -> new IronBarsBlock(
			p
				.requiresCorrectToolForDrops()
		),
		Blocks.GLASS
	);

	public static final Lazy<Block> REDSTONE_LANTERN = registerBlock(
		"redstone_lantern",
		RedstoneLampBlock::new,
		Blocks.REDSTONE_LAMP
	);

	public static final Lazy<Block> ROSE_QUARTZ_ORE = registerBlock(
		"rose_quartz_ore",
		(p) -> new DropExperienceBlock(
			UniformInt.of(1,4),
			p
		),
		Blocks.IRON_ORE
	);
	public static final Lazy<Block> DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock(
		"deepslate_rose_quartz_ore",
		(p) -> new DropExperienceBlock(
			UniformInt.of(1, 4),
			p
		),
		Blocks.DEEPSLATE_IRON_ORE
	);
	public static final Lazy<Block> ROSE_QUARTZ_BLOCK = registerBlock(
		"rose_quartz_block",
		Block::new,
		Blocks.AMETHYST_BLOCK
	);

	public static final Lazy<Block> MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		(p) -> new LogicGateBlock(
			p,
			LogicGateBlock.SideInput.ALL,
			true,
			(state, mode, side, back) -> {
				if (mode == ComparatorMode.SUBTRACT)
					return back - (side == 0 ? 0 : back % side);
				return side == 0 ? back : back % side;
			}
		),
		Blocks.COMPARATOR
	);

	public static final Lazy<Block> REDSTONE_STEPPER = registerBlock(
		"redstone_stepper",
		(p) -> new LogicGateBlock(
			p,
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		),
		Blocks.REPEATER
	);

	public static final Lazy<Block> MYCELIAL_SAND = registerBlock(
		"mycelial_sand",
		(p) -> new FallingDecayableBlock(
			p
				.mapColor(MapColor.ICE),
			Blocks.SAND,
			"mycha_spread"
		),
		Blocks.SAND
	);

	public static final Lazy<Block> MYCHA_ROOTS = registerBlock(
		"mycha_roots",
		(p) -> new RootedPlantBlock(
			p
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
		AltarPedestalBlock::new,
		Blocks.REDSTONE_BLOCK
	);

	public static final Lazy<AltarCatalyzerBlock> ALTAR_CATALYZER = registerBlock(
		"altar_catalyzer",
		AltarCatalyzerBlock::new,
		Blocks.REDSTONE_BLOCK
	);

	public static final Lazy<Block> SCULK_RESONATOR = registerBlock(
		"sculk_resonator",
		(p) -> new ResonatorBlock(
			p
				.sound(SoundType.SCULK_SHRIEKER)
		)
	);

	public static final Lazy<Block> SCULKFLOWER = registerBlock(
		"sculkflower",
		(p) -> new SculkflowerBlock(
			MobEffects.DARKNESS,
			30,
			p
				.offsetType(BlockBehaviour.OffsetType.NONE)
		),
		Blocks.POPPY
	);

	public static final Lazy<Block> CRYSTALLINE_SCULK = registerBlock(
		"crystalline_sculk_block",
		(p) -> new CrystalSculkBlock(
			p
				.lightLevel((state) -> 1)
				.mapColor(MapColor.ICE)
		),
		Blocks.AMETHYST_BLOCK
	);

	public static final Lazy<Block> CAMERA = registerBlock(
		"camera",
		(p) -> new CameraBlock(
			p
				.mapColor(MapColor.COLOR_GRAY)
		)
	);

	public static final Lazy<Block> SCONCE = registerBlock(
		"wall_holder",
		(p) ->  new SconceBlock(
			p
				.instabreak()
				.mapColor(MapColor.COLOR_GRAY)
		)
	);

	public static final Lazy<Block> SNAIL_SHELL = registerBlock(
		"snail_shell",
		(p) -> new ShellBlock(
			p
				.instabreak()
				.mapColor(MapColor.TERRACOTTA_PINK)
				.sound(SoundInitializer.SNAIL.get())
		)
	);

	public static final Lazy<CopperLensBlock> WAXED_COPPER_LENS = registerBlock(
		"waxed_copper_lens",
		(p) -> new CopperLensBlock(
			WeatheringCopper.WeatherState.UNAFFECTED,
			p
				.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<CopperLensBlock> WAXED_EXPOSED_COPPER_LENS = registerBlock(
		"waxed_exposed_copper_lens",
		(p) -> new CopperLensBlock(
			WeatheringCopper.WeatherState.EXPOSED,
			p
				.mapColor(Blocks.EXPOSED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<CopperLensBlock> WAXED_WEATHERED_COPPER_LENS = registerBlock(
		"waxed_weathered_copper_lens",
		(p) -> new CopperLensBlock(
			WeatheringCopper.WeatherState.WEATHERED,
			p
				.mapColor(Blocks.WEATHERED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<CopperLensBlock> WAXED_OXIDIZED_COPPER_LENS = registerBlock(
		"waxed_oxidized_copper_lens",
		(p) -> new CopperLensBlock(
			WeatheringCopper.WeatherState.OXIDIZED,
			p
				.mapColor(Blocks.OXIDIZED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<WeatheringCopperLensBlock> COPPER_LENS = registerBlock(
		"copper_lens",
		(p) -> new WeatheringCopperLensBlock(
			WeatheringCopper.WeatherState.UNAFFECTED,
			p
				.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<WeatheringCopperLensBlock> EXPOSED_COPPER_LENS = registerBlock(
		"exposed_copper_lens",
		(p) -> new WeatheringCopperLensBlock(
			WeatheringCopper.WeatherState.EXPOSED,
			p
				.mapColor(Blocks.EXPOSED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<WeatheringCopperLensBlock> WEATHERED_COPPER_LENS = registerBlock(
		"weathered_copper_lens",
		(p) -> new WeatheringCopperLensBlock(
			WeatheringCopper.WeatherState.WEATHERED,
			p
				.mapColor(Blocks.WEATHERED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<WeatheringCopperLensBlock> OXIDIZED_COPPER_LENS = registerBlock(
		"oxidized_copper_lens",
		(p) -> new WeatheringCopperLensBlock(
			WeatheringCopper.WeatherState.OXIDIZED,
			p
				.mapColor(Blocks.OXIDIZED_COPPER.defaultMapColor())
				.strength(3.0F, 6.0F)
				.sound(SoundType.COPPER_BULB)
				.requiresCorrectToolForDrops()
				.isRedstoneConductor((state, getter, pos) -> false)
				.noOcclusion()
		)
	);

	public static final Lazy<Block> SMALL_SCULK_BUD = registerBlock(
		"small_sculk_bud",
		(p) -> new AmethystClusterBlock(
			3, 4,
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
		),
		Blocks.SMALL_AMETHYST_BUD
	);

	public static final Lazy<Block> MEDIUM_SCULK_BUD = registerBlock(
		"medium_sculk_bud",
		(p) -> new AmethystClusterBlock(
			4, 3,
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
		),
		Blocks.MEDIUM_AMETHYST_BUD
	);

	public static final Lazy<Block> LARGE_SCULK_BUD = registerBlock(
		"large_sculk_bud",
		(p) -> new AmethystClusterBlock(
			5, 3,
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
		),
		Blocks.LARGE_AMETHYST_BUD
	);

	public static final Lazy<Block> SCULK_CLUSTER = registerBlock(
		"sculk_cluster",
		(p) -> new AmethystClusterBlock(
			7, 3,
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
		),
		Blocks.AMETHYST_CLUSTER
	);

	public static final Lazy<Block> SCULK_CRYSTAL = registerBlock(
		"sculk_crystal",
		(p) -> new Block(
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
		),
		Blocks.AMETHYST_BLOCK
	);

	public static final Lazy<Block> BUDDING_SCULK_CRYSTAL = registerBlock(
		"budding_sculk_crystal",
		(p) -> new BuddingBlock(
			p
				.mapColor(MapColor.ICE)
				.lightLevel(it -> 2)
				.randomTicks(),
			SMALL_SCULK_BUD,
			MEDIUM_SCULK_BUD,
			LARGE_SCULK_BUD,
			SCULK_CLUSTER
		),
		Blocks.AMETHYST_BLOCK
	);

	public static final Lazy<Block> QUARTZ_GLASS = registerBlock(
		"quartz_glass",
		(p) -> new StainedGlassBlock(
			DyeColor.WHITE,
			p
		),
		Blocks.GLASS
	);

	public static final Lazy<Block> ROSE_QUARTZ_GLASS = registerBlock(
		"rose_quartz_glass",
		(p) -> new StainedGlassBlock(
			DyeColor.PINK,
			p
		),
		Blocks.GLASS
	);

	public static final Lazy<Block> DIAMOND_GLASS = registerBlock(
		"diamond_glass",
		(p) -> new StainedGlassBlock(
			DyeColor.CYAN,
			p
		),
		Blocks.GLASS
	);

	public static final Lazy<Block> AMETHYST_GLASS = registerBlock(
		"amethyst_glass",
		(p) -> new StainedGlassBlock(
			DyeColor.MAGENTA,
			p
		),
		Blocks.GLASS
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

	public static final Pair<Lazy<WeatheringCopperLensBlock>, Lazy<CopperLensBlock>>[] WAXABLES = new Pair[] {
		new Pair<>(BlockInitializer.COPPER_LENS, BlockInitializer.WAXED_COPPER_LENS),
		new Pair<>(BlockInitializer.EXPOSED_COPPER_LENS, BlockInitializer.WAXED_EXPOSED_COPPER_LENS),
		new Pair<>(BlockInitializer.WEATHERED_COPPER_LENS, BlockInitializer.WAXED_WEATHERED_COPPER_LENS),
		new Pair<>(BlockInitializer.OXIDIZED_COPPER_LENS, BlockInitializer.WAXED_OXIDIZED_COPPER_LENS),
	};

	public static final Pair<Lazy<WeatheringCopperLensBlock>, Lazy<WeatheringCopperLensBlock>>[] WEATHERABLES = new Pair[] {
		new Pair<>(BlockInitializer.COPPER_LENS, BlockInitializer.EXPOSED_COPPER_LENS),
		new Pair<>(BlockInitializer.EXPOSED_COPPER_LENS, BlockInitializer.WEATHERED_COPPER_LENS),
		new Pair<>(BlockInitializer.WEATHERED_COPPER_LENS, BlockInitializer.OXIDIZED_COPPER_LENS),
	};

	public static <T extends Block> Lazy<T> registerBlock(String id, Supplier<T> block) {
		return (Lazy<T>) BLOCKS.register(WanderingWizardry.id(id), (Supplier<Block>) block);
	}

	public static <T extends Block> Lazy<T> registerBlock(String id, BlockInitFunc<T> block) {
		return (Lazy<T>) BLOCKS.register(WanderingWizardry.id(id), () -> block.init(BlockBehaviour.Properties.of()));
	}

	public static <T extends Block> Lazy<T> registerBlock(String id, BlockInitFunc<T> block, BlockBehaviour copy) {
		return (Lazy<T>) BLOCKS.register(WanderingWizardry.id(id), () -> block.init(BlockBehaviour.Properties.ofFullCopy(copy)));
	}

	public static <T extends Block> Lazy<T> registerBlock(String id, BlockInitFunc<T> block, Supplier<? extends BlockBehaviour> copy) {
		return (Lazy<T>) BLOCKS.register(WanderingWizardry.id(id), () -> block.init(BlockBehaviour.Properties.ofFullCopy(copy.get())));
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
		set.addAll(Arrays.asList(blocks));
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

	@FunctionalInterface
	public interface BlockInitFunc<T extends Block> {
		T init(BlockBehaviour.Properties properties);
	}
}
