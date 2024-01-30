package dev.sweetberry.wwizardry.content.block;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.content.block.altar.entity.LogicGateBlockEntity;
import dev.sweetberry.wwizardry.content.block.nature.FallingDecayableBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedFlowerBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedPlantBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.LogicGateBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.sculk.SculkBlock;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.int_provider.UniformIntProvider;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockInitializer {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.of("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.of("sculk_below");

	public static final VoxelShape ALTAR_BASE_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).simplify();

	public static final Block INDIGO_CAERULEUM = registerBlock("indigo_caeruleum", new RootedFlowerBlock(StatusEffects.INVISIBILITY, 20, "mycha_growable", FabricBlockSettings.copyOf(Blocks.POPPY)));

	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS).requiresTool()));

	public static final Block REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane", new PaneBlock(FabricBlockSettings.copyOf(Blocks.GLASS).requiresTool()));

	public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern", new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP)));

	public static final Block ROSE_QUARTZ_ORE = registerBlock("rose_quartz_ore", new ExperienceDroppingBlock(UniformIntProvider.create(1,4), FabricBlockSettings.copyOf(Blocks.IRON_ORE)));
	public static final Block DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock("deepslate_rose_quartz_ore", new ExperienceDroppingBlock(UniformIntProvider.create(1,4), FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE)));
	public static final Block ROSE_QUARTZ_BLOCK = registerBlock("rose_quartz_block", new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)));

	public static final Block MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		new LogicGateBlock(
			FabricBlockSettings.copyOf(Blocks.COMPARATOR),
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
			FabricBlockSettings.copyOf(Blocks.REPEATER),
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		)
	);

	public static final Block MYCELIAL_SAND = registerBlock(
		"mycelial_sand",
		new FallingDecayableBlock(
			FabricBlockSettings.copyOf(Blocks.SAND).mapColor(MapColor.ICE),
			Blocks.SAND,
			"mycha_spread"
		)
	);

	public static final Block MYCHA_ROOTS = registerBlock(
		"mycha_roots",
		new RootedPlantBlock(
			AbstractBlock.Settings.create()
				.mapColor(MapColor.NETHER)
				.replaceable()
				.noCollision()
				.breakInstantly()
				.sounds(BlockSoundGroup.ROOTS)
				.offsetType(AbstractBlock.OffsetType.XZ)
				.pistonBehavior(PistonBehavior.DESTROY),
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
		return Registry.register(Registries.BLOCK, Mod.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Mod.id(id), blockEntity);
	}

	public static boolean testForSculk(BlockView world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isSideSolidFullSquare(world, pos, Direction.UP);
	}
}
