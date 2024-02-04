package dev.sweetberry.wwizardry.content.block.redstone;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.content.block.entity.LogicGateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;

public class LogicGateBlock extends DiodeBlock implements EntityBlock {
	public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;

	public final CompareFunction function;
	public final SideInput inputType;
	public final boolean multistate;

	public final MapCodec<LogicGateBlock> codec;


	public LogicGateBlock(Properties settings, SideInput inputType, boolean multistate, CompareFunction function) {
		super(settings);
		this.inputType = inputType;
		this.multistate = multistate;
		this.function = function;
		var state = defaultBlockState().setValue(POWERED, false).setValue(MODE, ComparatorMode.COMPARE);
		registerDefaultState(state);
		codec = BlockBehaviour.simpleCodec(settings1 -> LogicGateBlock.this);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!multistate)
			return InteractionResult.PASS;
		if (!player.getAbilities().mayBuild)
			return InteractionResult.PASS;
		state = state.cycle(MODE);
		float f = state.getValue(MODE) == ComparatorMode.SUBTRACT ? 0.55F : 0.5F;
		world.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, f);
		world.setBlock(pos, state, Block.UPDATE_CLIENTS);
		update(world, pos, state);
		return InteractionResult.sidedSuccess(world.isClientSide);
	}

	@Override
	protected int getDelay(BlockState state) {
		return 2;
	}

	@Override
	protected int getOutputSignal(BlockGetter world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		assert blockEntity != null;
		return ((LogicGateBlockEntity)blockEntity).getOutputSignal();
	}

	private int calculateOutputSignal(Level world, BlockPos pos, BlockState state) {
		int back = getInputSignal(world, pos, state);
		int side = getAlternateSignal(world, pos, state);
		var mode = state.getValue(MODE);
		return function.compare(state, mode, side, back);
	}

	@Override
	protected int getInputSignal(Level world, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(FACING);
		BlockPos blockPos = pos.relative(direction);
		BlockState blockState = world.getBlockState(blockPos);

		if (blockState.hasAnalogOutputSignal())
			return blockState.getAnalogOutputSignal(world, blockPos);

		return super.getInputSignal(world, pos, state);
	}

	@Override
	protected void checkTickOnNeighbor(Level world, BlockPos pos, BlockState state) {
		if (!world.getBlockTicks().willTickThisTick(pos, this)) {
			int currentPower = calculateOutputSignal(world, pos, state);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			assert blockEntity != null;
			int previousPower = ((LogicGateBlockEntity)blockEntity).getOutputSignal();

			boolean currentlyPowered = currentPower != 0;
			boolean previouslyPowered = state.getValue(POWERED);

			if (currentPower != previousPower || currentlyPowered != previouslyPowered) {
				TickPriority tickPriority = shouldPrioritize(world, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
				world.scheduleTick(pos, this, 2, tickPriority);
			}
		}
	}

	private void update(Level world, BlockPos pos, BlockState state) {
		int currentPower = calculateOutputSignal(world, pos, state);
		boolean currentlyPowered = currentPower != 0;

		var blockEntity = (LogicGateBlockEntity)world.getBlockEntity(pos);
		assert blockEntity != null;

		int previousPower = blockEntity.getOutputSignal();
		blockEntity.setOutputSignal(currentPower);
		boolean previouslyPowered = state.getValue(POWERED);

		if (previousPower != currentPower) {
			if (previouslyPowered != currentlyPowered) {
				world.setBlock(pos, state.setValue(POWERED, currentlyPowered), Block.UPDATE_CLIENTS);
			}

			updateNeighborsInFront(world, pos, state);
		}
	}

	@Override
	protected MapCodec<? extends DiodeBlock> codec() {
		return codec;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		update(world, pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
		super.triggerEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(type, data);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LogicGateBlockEntity(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, MODE);
	}

	@FunctionalInterface
	public interface CompareFunction {
		int compare(BlockState state, ComparatorMode mode, int side, int back);
	}

	public enum SideInput {
		ALL,
		GATES,
		SELF,
		NONE
	}
}
