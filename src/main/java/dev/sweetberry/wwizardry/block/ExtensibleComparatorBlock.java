package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.block.entity.ExtensibleComparatorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

public class ExtensibleComparatorBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider {
	public static final EnumProperty<ComparatorMode> MODE = Properties.COMPARATOR_MODE;

	public final CompareFunction function;


	public ExtensibleComparatorBlock(Settings settings, CompareFunction function) {
		super(settings);
		this.function = function;
		setDefaultState(getDefaultState().with(POWERED, false).with(MODE, ComparatorMode.COMPARE));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			state = state.cycle(MODE);
			float f = state.get(MODE) == ComparatorMode.SUBTRACT ? 0.55F : 0.5F;
			world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
			update(world, pos, state);
			return ActionResult.success(world.isClient);
		}
	}

	@Override
	protected int getUpdateDelayInternal(BlockState state) {
		return 2;
	}

	@Override
	protected int getOutputLevel(BlockView world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		assert blockEntity != null;
		return ((ExtensibleComparatorBlockEntity)blockEntity).getOutputSignal();
	}

	private int calculateOutputSignal(World world, BlockPos pos, BlockState state) {
		int back = getPower(world, pos, state);
		int side = getMaxInputLevelSides(world, pos, state);
		return function.compare(state, state.get(MODE), side, back);
	}

	@Override
	protected boolean hasPower(World world, BlockPos pos, BlockState state) {
		return calculateOutputSignal(world, pos, state) != 0;
	}

	@Override
	protected int getPower(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(FACING);
		BlockPos blockPos = pos.offset(direction);
		BlockState blockState = world.getBlockState(blockPos);

		if (blockState.hasComparatorOutput())
			return blockState.getComparatorOutput(world, blockPos);

		return super.getPower(world, pos, state);
	}

	@Override
	protected void updatePowered(World world, BlockPos pos, BlockState state) {
		if (!world.getBlockTickScheduler().willTick(pos, this)) {
			int currentPower = calculateOutputSignal(world, pos, state);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			assert blockEntity != null;
			int previousPower = ((ExtensibleComparatorBlockEntity)blockEntity).getOutputSignal();

			boolean currentlyPowered = currentPower != 0;
			boolean previouslyPowered = state.get(POWERED);

			if (currentPower != previousPower || currentlyPowered != previouslyPowered) {
				TickPriority tickPriority = isTargetNotAligned(world, pos, state) ? TickPriority.HIGH : TickPriority.NORMAL;
				world.scheduleBlockTick(pos, this, 2, tickPriority);
			}
		}
	}

	private void update(World world, BlockPos pos, BlockState state) {
		int currentPower = calculateOutputSignal(world, pos, state);
		boolean currentlyPowered = currentPower != 0;

		var blockEntity = (ExtensibleComparatorBlockEntity)world.getBlockEntity(pos);
		assert blockEntity != null;

		int previousPower = blockEntity.getOutputSignal();
		blockEntity.setOutputSignal(currentPower);
		boolean previouslyPowered = state.get(POWERED);

		if (previousPower != currentPower) {
			if (previouslyPowered != currentlyPowered) {
				world.setBlockState(pos, state.with(POWERED, currentlyPowered), Block.NOTIFY_LISTENERS);
			}

			updateTarget(world, pos, state);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		update(world, pos, state);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		super.onSyncedBlockEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ExtensibleComparatorBlockEntity(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, MODE, POWERED);
	}

	@FunctionalInterface
	public interface CompareFunction {
		int compare(BlockState state, ComparatorMode mode, int side, int back);
	}
}
