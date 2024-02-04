package dev.sweetberry.wwizardry.content.block.redstone;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ResonatorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<ResonatorBlock> CODEC = BlockBehaviour.simpleCodec(ResonatorBlock::new);

	public static final ResonatorBlock INSTANCE = new ResonatorBlock(BlockBehaviour.Properties.of().sound(SoundType.SCULK_SHRIEKER));
	static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

	protected ResonatorBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false).setValue(BlockStateProperties.SHRIEKING, false).setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected MapCodec<ResonatorBlock> codec() {
		return CODEC;
	}

	public int getPower(Level world, BlockPos pos, Direction dir) {
		var directPower = world.getControlInputSignal(pos, dir, false);

		if (directPower >= 15)
			return directPower;

		var softPower = world.getSignal(pos, dir);

		return Math.max(directPower, softPower);
	}

	public int getSidePower(Level world, BlockState state, BlockPos pos) {
		var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		var front = dir.getOpposite();
		var left = front.getCounterClockWise();
		var right = front.getClockWise();

		return Math.max(
			Math.max(
				getPower(world, pos.relative(front), front),
				getPower(world, pos.relative(left), left)
			),
			getPower(world, pos.relative(right), right)
		);
	}

	public int getBackPower(Level world, BlockState state, BlockPos pos) {
		var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		var behindPos = pos.relative(dir);
		return getPower(world, behindPos, dir);
	}

	public BlockState tryShriek(Level world, BlockState state, BlockPos pos) {
		var sidePower = getSidePower(world, state, pos);

		if (state.getValue(BlockStateProperties.POWERED) && sidePower != 0) return state;

		if (state.getValue(BlockStateProperties.SHRIEKING)) return state.setValue(BlockStateProperties.POWERED, sidePower > 0);

		if (sidePower == 0) return state.setValue(BlockStateProperties.POWERED, false);

		var backPower = getBackPower(world, state, pos);

		if (backPower == 0) return state.setValue(BlockStateProperties.POWERED, true);

		world.gameEvent(VibrationSystem.getResonanceEventByFrequency(backPower), pos, GameEvent.Context.of(world.getBlockState(pos)));
		world.levelEvent(3007, pos, 0);
		world.scheduleTick(pos, this, 20);
		return state.setValue(BlockStateProperties.SHRIEKING, true).setValue(BlockStateProperties.POWERED, true);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState updateShape(
		BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos
	) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		var setState = tryShriek(world, state, pos);
		if (state == setState) return;
		world.setBlockAndUpdate(pos, setState);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.SHRIEKING, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.SHRIEKING, BlockStateProperties.POWERED, BlockStateProperties.WATERLOGGED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
	}
}
