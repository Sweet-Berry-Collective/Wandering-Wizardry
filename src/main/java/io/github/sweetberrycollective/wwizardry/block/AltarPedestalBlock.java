package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class AltarPedestalBlock extends AltarBlock<AltarPedestalBlockEntity> {
	public static final AltarPedestalBlock INSTANCE = new AltarPedestalBlock(QuiltBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new QuiltItemSettings());
	public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 10.0, 16.0, 15.0, 16.0),
			createCuboidShape(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			createCuboidShape(0.0, 15.0, 0.0, 16.0, 19.0, 6.0)
	).simplify();
	public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 0.0, 16.0, 15.0, 6.0),
			createCuboidShape(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			createCuboidShape(0.0, 15.0, 10.0, 16.0, 19.0, 16.0)
	).simplify();
	public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 0.0, 6.0, 15.0, 16.0),
			createCuboidShape(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
			createCuboidShape(10.0, 15.0, 0.0, 16.0, 19.0, 16.0)
	).simplify();
	public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(10.0, 11.0, 0.0, 16.0, 15.0, 16.0),
			createCuboidShape(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
			createCuboidShape(0.0, 15.0, 0.0, 6.0, 19.0, 16.0)
	).simplify();


	public AltarPedestalBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH));
	}

	@Override
	public BlockEntityType<AltarPedestalBlockEntity> getBlockEntityType() {
		return AltarPedestalBlockEntity.TYPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarPedestalBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(HorizontalFacingBlock.FACING);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(HorizontalFacingBlock.FACING)) {
			case SOUTH -> SOUTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case WEST -> WEST_SHAPE;
			default -> NORTH_SHAPE;
		};
	}
}
