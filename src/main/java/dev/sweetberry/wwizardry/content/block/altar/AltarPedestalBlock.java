package dev.sweetberry.wwizardry.content.block.altar;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarPedestalBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AltarPedestalBlock extends AltarBlock<AltarPedestalBlockEntity> {
	public static final AltarPedestalBlock INSTANCE = new AltarPedestalBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new FabricItemSettings());
	public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
			BlockInitializer.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 10.0, 16.0, 15.0, 16.0),
			createCuboidShape(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			createCuboidShape(0.0, 15.0, 0.0, 16.0, 19.0, 6.0)
	).simplify();
	public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
			BlockInitializer.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 0.0, 16.0, 15.0, 6.0),
			createCuboidShape(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			createCuboidShape(0.0, 15.0, 10.0, 16.0, 19.0, 16.0)
	).simplify();
	public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
			BlockInitializer.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 11.0, 0.0, 6.0, 15.0, 16.0),
			createCuboidShape(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
			createCuboidShape(10.0, 15.0, 0.0, 16.0, 19.0, 16.0)
	).simplify();
	public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
			BlockInitializer.ALTAR_BASE_SHAPE,

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
	public boolean isComplete(BlockView world, BlockState state, BlockPos pos) {
		var facing = state.get(HorizontalFacingBlock.FACING);
		var offset = pos.offset(facing, -2);
		var center = world.getBlockState(offset);
		if (center.isOf(AltarCatalyzerBlock.INSTANCE))
			return AltarCatalyzerBlock.INSTANCE.isComplete(world, center, offset);
		return false;
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

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(HorizontalFacingBlock.FACING, rotation.rotate(state.get(HorizontalFacingBlock.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)));
	}
}
