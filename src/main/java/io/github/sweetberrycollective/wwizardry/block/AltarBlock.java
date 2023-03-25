package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.block.sculk.SculkVeinSpreader;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public abstract class AltarBlock<T extends AltarBlockEntity> extends BlockWithEntity implements Waterloggable, SculkVeinSpreader, Sculkable {

	protected AltarBlock(Settings settings) {
		super(settings);
		setDefaultState(
				getDefaultState()
						.with(Properties.WATERLOGGED, false)
						.with(WanderingBlocks.SCULK_INFESTED, false)
						.with(WanderingBlocks.SCULK_BELOW, false)
		);
	}

	public abstract BlockEntityType<T> getBlockEntityType();

	public void handleInput(PlayerEntity player, Hand hand, AltarBlockEntity entity) {
		boolean inserted = false;
		for (int i = 0; i < player.getInventory().size(); i++) {
			var invStack = player.getInventory().getStack(i);
			if (invStack.getCount() == invStack.getMaxCount()) continue;
			if (invStack.getItem() != entity.heldItem.getItem()) continue;
			inserted = true;
			invStack.increment(1);
			player.getInventory().setStack(i, invStack);
			player.getInventory().markDirty();
			break;
		}
		if (!inserted) {
			player.setStackInHand(hand, entity.heldItem);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isSneaking()) return ActionResult.PASS;
		var stack = player.getStackInHand(hand);
		var entity = (AltarBlockEntity) world.getBlockEntity(pos);
		assert entity != null;
		if (entity.crafting) return ActionResult.PASS;
		if (stack.isEmpty() && entity.heldItem.isEmpty()) return ActionResult.PASS;
		if (world.isClient) return ActionResult.SUCCESS;
		var newstack = stack.getItem().getDefaultStack();

		if (stack.getItem() == entity.heldItem.getItem()) {
			if (stack.getCount() == stack.getMaxCount()) {
				if (!player.giveItemStack(entity.heldItem)) return ActionResult.SUCCESS;
			} else {
				stack.increment(1);
				player.setStackInHand(hand, stack);
			}
			entity.heldItem = ItemStack.EMPTY;
			entity.markDirty();
			world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 10f, 0f);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
			return ActionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			if (!entity.heldItem.isEmpty()) {
				if (stack.getCount() == 1) {
					handleInput(player, hand, entity);
				} else {
					if (!player.giveItemStack(entity.heldItem)) return ActionResult.SUCCESS;
				}
			}
			stack.decrement(1);
			entity.heldItem = newstack;
			entity.tryCraft(state);
		} else {
			handleInput(player, hand, entity);
			entity.heldItem = ItemStack.EMPTY;
		}

		((ServerWorld) world).getChunkManager().markForUpdate(pos);
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));

		entity.markDirty();
		world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 10f, 0f);
		return ActionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return state.with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(world, pos.down()));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(world, pos.down())));
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		var e = world.getBlockEntity(pos);
		if (!(e instanceof AltarBlockEntity entity)) return;
		entity.tryCancelCraft(state);
		var stackEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.heldItem);
		world.spawnEntity(stackEntity);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, WanderingBlocks.SCULK_INFESTED, WanderingBlocks.SCULK_BELOW);
	}

	@Nullable
	@Override
	public <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(World world, BlockState state, BlockEntityType<BE> type) {
		return checkType(type, getBlockEntityType(), (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public int getPower(BlockView world, BlockPos pos, Function<AltarBlockEntity, Boolean> func) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarBlockEntity abe)) return 0;
		return func.apply(abe) ? 15 : 0;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return getPower(world, pos, abe -> !abe.heldItem.isEmpty());
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return getPower(world, pos, abe -> abe.crafting);
	}

	@Override
	public boolean trySpreadVein(WorldAccess world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean postProcess) {
		var posDown = pos.down();
		var stateDown = world.getBlockState(posDown);

		if (!state.get(WanderingBlocks.SCULK_INFESTED)) {
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_INFESTED, true).with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(world, pos.down())), NOTIFY_ALL | FORCE_STATE);
			world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1, 1);
			return true;
		}

		if (!state.get(WanderingBlocks.SCULK_BELOW) && stateDown.isIn(BlockTags.SCULK_REPLACEABLE)) {
			world.setBlockState(posDown, Blocks.SCULK.getDefaultState(), NOTIFY_ALL | FORCE_STATE);
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, true), NOTIFY_ALL | FORCE_STATE);
			world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1, 1);
			return true;
		}

		return SculkVeinSpreader.super.trySpreadVein(world, pos, state, directions, postProcess);
	}

	@Override
	public int tryUseCharge(SculkBehavior.ChargeCursor charge, WorldAccess world, BlockPos pos, RandomGenerator random, SculkBehavior sculkChargeHandler, boolean spread) {
		var state = world.getBlockState(pos);
		var stateDown = world.getBlockState(pos.down());
		if (stateDown.getBlock() == Blocks.SCULK || stateDown.getBlock() == Blocks.AIR) {
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, true), NOTIFY_ALL | FORCE_STATE);
		}
		return charge.getCharge();
	}

	@Override
	public boolean canUpdateOnSpread() {
		return true;
	}

	@Override
	public boolean hasPrimaryAction() {
		return true;
	}
}
