package dev.sweetberry.wwizardry.content.block.altar;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarPedestalBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AltarPedestalBlock extends AltarBlock<AltarPedestalBlockEntity> {
	public static final AltarPedestalBlock INSTANCE = new AltarPedestalBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new FabricItemSettings());
	public static final VoxelShape NORTH_SHAPE = Shapes.or(
			BlockInitializer.ALTAR_BASE_SHAPE,

			box(0.0, 11.0, 10.0, 16.0, 15.0, 16.0),
			box(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			box(0.0, 15.0, 0.0, 16.0, 19.0, 6.0)
	).optimize();
	public static final VoxelShape SOUTH_SHAPE = Shapes.or(
			BlockInitializer.ALTAR_BASE_SHAPE,

			box(0.0, 11.0, 0.0, 16.0, 15.0, 6.0),
			box(0.0, 13.0, 5.0, 16.0, 17.0, 11.0),
			box(0.0, 15.0, 10.0, 16.0, 19.0, 16.0)
	).optimize();
	public static final VoxelShape EAST_SHAPE = Shapes.or(
			BlockInitializer.ALTAR_BASE_SHAPE,

			box(0.0, 11.0, 0.0, 6.0, 15.0, 16.0),
			box(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
			box(10.0, 15.0, 0.0, 16.0, 19.0, 16.0)
	).optimize();
	public static final VoxelShape WEST_SHAPE = Shapes.or(
			BlockInitializer.ALTAR_BASE_SHAPE,

			box(10.0, 11.0, 0.0, 16.0, 15.0, 16.0),
			box(5.0, 13.0, 0.0, 11.0, 17.0, 16.0),
			box(0.0, 15.0, 0.0, 6.0, 19.0, 16.0)
	).optimize();


	public AltarPedestalBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
	}

	@Override
	public BlockEntityType<AltarPedestalBlockEntity> getBlockEntityType() {
		return AltarPedestalBlockEntity.TYPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AltarPedestalBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, ctx.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HorizontalDirectionalBlock.FACING);
	}

	@Override
	public boolean isComplete(BlockGetter world, BlockState state, BlockPos pos) {
		var facing = state.getValue(HorizontalDirectionalBlock.FACING);
		var offset = pos.relative(facing, -2);
		var center = world.getBlockState(offset);
		if (center.is(AltarCatalyzerBlock.INSTANCE))
			return AltarCatalyzerBlock.INSTANCE.isComplete(world, center, offset);
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
			case SOUTH -> SOUTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case WEST -> WEST_SHAPE;
			default -> NORTH_SHAPE;
		};
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
	}
}
