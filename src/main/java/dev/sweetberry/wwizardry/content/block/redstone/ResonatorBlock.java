package dev.sweetberry.wwizardry.content.block.redstone;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.vibration.VibrationManager;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class ResonatorBlock extends HorizontalFacingBlock implements Waterloggable {
	public static final ResonatorBlock INSTANCE = new ResonatorBlock(QuiltBlockSettings.create().sounds(BlockSoundGroup.SCULK_SHRIEKER));
	static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

	protected ResonatorBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(Properties.SHRIEKING, false).with(Properties.POWERED, false));
	}

	public int getPower(World world, BlockPos pos, Direction dir) {
		var directPower = world.getFilteredEmittedRedstonePower(pos, dir, false);

		if (directPower >= 15)
			return directPower;

		var softPower = world.getEmittedRedstonePower(pos, dir);

		return Math.max(directPower, softPower);
	}

	public int getSidePower(World world, BlockState state, BlockPos pos) {
		var dir = state.get(Properties.HORIZONTAL_FACING);
		var front = dir.getOpposite();
		var left = front.rotateYCounterclockwise();
		var right = front.rotateYClockwise();

		return Math.max(
			Math.max(
				getPower(world, pos.offset(front), front),
				getPower(world, pos.offset(left), left)
			),
			getPower(world, pos.offset(right), right)
		);
	}

	public int getBackPower(World world, BlockState state, BlockPos pos) {
		var dir = state.get(Properties.HORIZONTAL_FACING);
		var behindPos = pos.offset(dir);
		return getPower(world, behindPos, dir);
	}

	public BlockState tryShriek(World world, BlockState state, BlockPos pos) {
		var sidePower = getSidePower(world, state, pos);

		if (state.get(Properties.POWERED) && sidePower != 0) return state;

		if (state.get(Properties.SHRIEKING)) return state.with(Properties.POWERED, sidePower > 0);

		if (sidePower == 0) return state.with(Properties.POWERED, false);

		var backPower = getBackPower(world, state, pos);

		if (backPower == 0) return state.with(Properties.POWERED, true);

		world.emitGameEvent(VibrationManager.getResonationEvent(backPower), pos, GameEvent.Context.create(world.getBlockState(pos)));
		world.syncWorldEvent(3007, pos, 0);
		world.scheduleBlockTick(pos, this, 20);
		return state.with(Properties.SHRIEKING, true).with(Properties.POWERED, true);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		var setState = tryShriek(world, state, pos);
		if (state == setState) return;
		world.setBlockState(pos, setState);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		world.setBlockState(pos, state.with(Properties.SHRIEKING, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public boolean isRedstonePowerSource(BlockState state) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING, Properties.SHRIEKING, Properties.POWERED, Properties.WATERLOGGED);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}
}
