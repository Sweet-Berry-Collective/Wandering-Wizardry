package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.block.entity.LogicGateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.int_provider.UniformIntProvider;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class WanderingBlocks {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.of("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.of("sculk_below");

	public static final VoxelShape ALTAR_BASE_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).simplify();

	public static final Block INDIGO_CAERULEUM = registerBlock("indigo_caeruleum", new WanderingFlowerBlock(StatusEffects.INVISIBILITY, 20, QuiltBlockSettings.copyOf(Blocks.POPPY)));

	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(QuiltBlockSettings.copyOf(Blocks.GLASS).requiresTool()));

	public static final Block REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane", new PaneBlock(QuiltBlockSettings.copyOf(Blocks.GLASS).requiresTool()));

	public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern", new RedstoneLampBlock(QuiltBlockSettings.copyOf(Blocks.REDSTONE_LAMP)));

	public static final Block ROSE_QUARTZ_ORE = registerBlock("rose_quartz_ore", new ExperienceDroppingBlock(QuiltBlockSettings.copyOf(Blocks.IRON_ORE), UniformIntProvider.create(1,4)));
	public static final Block DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock("deepslate_rose_quartz_ore", new ExperienceDroppingBlock(QuiltBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE), UniformIntProvider.create(1,4)));
	public static final Block ROSE_QUARTZ_BLOCK = registerBlock("rose_quartz_block", new Block(QuiltBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)));

	public static final Block MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		new LogicGateBlock(
			QuiltBlockSettings.copyOf(Blocks.COMPARATOR),
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
			QuiltBlockSettings.copyOf(Blocks.REPEATER),
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		)
	);

	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlock("sculkflower", SculkflowerBlock.INSTANCE);
		registerBlock("crystalline_sculk_block", CrystalSculkBlock.INSTANCE);
		registerBlock("camera", CameraBlock.INSTANCE);
		registerBlock("wall_holder", WallHolderBlock.EMPTY);

		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
		registerBlockEntity("extensible_comparator", LogicGateBlockEntity.TYPE);
	}

	public static <T extends Block> T registerBlock(String id, T block) {
		return Registry.register(Registries.BLOCK, WanderingMod.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, WanderingMod.id(id), blockEntity);
	}

	public static boolean testForSculk(BlockView world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isSideSolidFullSquare(world, pos, Direction.UP);
	}
}
