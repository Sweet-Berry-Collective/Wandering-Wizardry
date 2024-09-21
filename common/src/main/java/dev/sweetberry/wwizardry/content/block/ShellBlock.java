package dev.sweetberry.wwizardry.content.block;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShellBlock extends HorizontalDirectionalBlock {
	public static final VoxelShape[] NORTH_SOUTH = new VoxelShape[] {
		box(6.5, 0, 6, 9.5, 4, 10),
		box(4.5, 0, 6, 11.5, 4, 10),
		Shapes.join(
			box(4.5, 0, 6, 11.5, 4, 10),
			box(6.5, 4, 6, 9.5, 8, 10),
			BooleanOp.OR
		)
	};
	public static final VoxelShape[] EAST_WEST = new VoxelShape[] {
		box(6, 0, 6.5, 10, 4, 9.5),
		box(6, 0, 4.5, 10, 4, 11.5),
		Shapes.join(
			box(6, 0, 4.5, 10, 4, 11.5),
			box(6, 4, 6.5, 10, 8, 9.5),
			BooleanOp.OR
		)
	};

	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 3);

	public static final MapCodec<ShellBlock> CODEC = BlockBehaviour.simpleCodec(ShellBlock::new);

	public ShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HorizontalDirectionalBlock.FACING).add(COUNT);
	}

	@Override
	@NotNull
	protected ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		if (player.isCrouching())
			return super.useItemOn(stack, state, level, pos, player, hand, hit);

		if (!stack.is(ItemInitializer.SNAIL_SHELL.get()))
			return super.useItemOn(stack, state, level, pos, player, hand, hit);

		int count = state.getValue(COUNT);
		if (count == 3)
			return super.useItemOn(stack, state, level, pos, player, hand, hit);

		level.setBlock(pos, state.setValue(COUNT, count + 1), Block.UPDATE_ALL);

		if (!player.isCreative())
			stack.shrink(1);

		level.playSound(player, pos, SoundInitializer.SNAIL_PLACE.get(), SoundSource.BLOCKS);

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
			case NORTH, SOUTH -> NORTH_SOUTH[state.getValue(COUNT) - 1];
			default -> EAST_WEST[state.getValue(COUNT) - 1];
		};
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState()
			.setValue(HorizontalDirectionalBlock.FACING, ctx.getHorizontalDirection())
			.setValue(COUNT, 1);
	}
}
