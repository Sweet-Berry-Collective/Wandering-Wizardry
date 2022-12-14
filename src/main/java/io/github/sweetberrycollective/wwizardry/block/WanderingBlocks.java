package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class WanderingBlocks {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.of("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.of("sculk_below");
	public static final BooleanProperty NATURALLY_GENERATED = BooleanProperty.of("natural");

	public static final VoxelShape ALTAR_BASE_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).simplify();

	public static final Block BASALT_BRICKS = registerBlock(
			"basalt_bricks",
			new Block(
					QuiltBlockSettings
							.copyOf(Blocks.SMOOTH_BASALT)
			)
	);
	public static final Block BASALT_BRICK_STAIRS = registerBlock(
			"basalt_brick_stairs",
			new StairsBlock(
					BASALT_BRICKS.getDefaultState(),
					QuiltBlockSettings
							.copyOf(BASALT_BRICKS)
			)
	);
	public static final Block BASALT_BRICK_SLAB = registerBlock(
			"basalt_brick_slab",
			new SlabBlock(
					QuiltBlockSettings
							.copyOf(BASALT_BRICKS)
			)
	);
	public static final Block BASALT_BRICK_WALL = registerBlock(
			"basalt_brick_wall",
			new WallBlock(
					QuiltBlockSettings
							.copyOf(BASALT_BRICKS)
			)
	);

	public static final Block BASALT_TILES = registerBlock(
			"basalt_tiles",
			new Block(
					QuiltBlockSettings
							.copyOf(Blocks.SMOOTH_BASALT)
			)
	);
	public static final Block BASALT_TILE_STAIRS = registerBlock(
			"basalt_tile_stairs",
			new StairsBlock(
					BASALT_TILES.getDefaultState(),
					QuiltBlockSettings
							.copyOf(BASALT_TILES)
			)
	);
	public static final Block BASALT_TILE_SLAB = registerBlock(
			"basalt_tile_slab",
			new SlabBlock(
					QuiltBlockSettings
							.copyOf(BASALT_TILES)
			)
	);
	public static final Block BASALT_TILE_WALL = registerBlock(
			"basalt_tile_wall",
			new WallBlock(
					QuiltBlockSettings
							.copyOf(BASALT_TILES)
			)
	);

	public static final Block CHISELED_BASALT = registerBlock(
			"chiseled_basalt",
			new Block(
					QuiltBlockSettings
							.copyOf(Blocks.SMOOTH_BASALT)
			)
	);
	public static final Block CHISELED_BASALT_STAIRS = registerBlock(
			"chiseled_basalt_stairs",
			new StairsBlock(
					CHISELED_BASALT.getDefaultState(),
					QuiltBlockSettings
							.copyOf(CHISELED_BASALT)
			)
	);
	public static final Block CHISELED_BASALT_SLAB = registerBlock(
			"chiseled_basalt_slab",
			new SlabBlock(
					QuiltBlockSettings
							.copyOf(CHISELED_BASALT)
			)
	);
	public static final Block CHISELED_BASALT_WALL = registerBlock(
			"chiseled_basalt_wall",
			new WallBlock(
					QuiltBlockSettings
							.copyOf(CHISELED_BASALT)
			)
	);

	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
	}

	public static Block registerBlock(String id, Block block) {
		return Registry.register(Registry.BLOCK, WanderingMod.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, WanderingMod.id(id), blockEntity);
	}

	public static boolean testForSculk(BlockView world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isSideSolidFullSquare(world, pos, Direction.UP);
	}
}
