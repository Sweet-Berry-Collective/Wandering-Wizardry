package dev.sweetberry.wwizardry.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShellBlock extends HorizontalDirectionalBlock {
	public static final VoxelShape NORTH_SOUTH = box(6.5, 0, 6, 9.5, 4, 10);
	public static final VoxelShape EAST_WEST = box(6, 0, 6.5, 10, 4, 9.5);

	public static final MapCodec<ShellBlock> CODEC = BlockBehaviour.simpleCodec(ShellBlock::new);

	public ShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HorizontalDirectionalBlock.FACING);
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
			case NORTH, SOUTH -> NORTH_SOUTH;
			default -> EAST_WEST;
		};
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, ctx.getHorizontalDirection());
	}
}
