package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Collection;

public class AltarPedestalBlock extends BlockWithEntity implements Waterloggable, SculkVeinSpreader {
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
		setDefaultState(
				getDefaultState()
						.with(Properties.WATERLOGGED, false)
						.with(WanderingBlocks.SCULK_INFESTED, false)
						.with(WanderingBlocks.SCULK_BELOW, false)
		);
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING, Properties.WATERLOGGED, WanderingBlocks.SCULK_INFESTED, WanderingBlocks.SCULK_BELOW);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarPedestalBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
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

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, AltarPedestalBlockEntity.TYPE, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isSneaking()) return ActionResult.PASS;
		var stack = player.getStackInHand(hand);
		var entity = (AltarPedestalBlockEntity) world.getBlockEntity(pos);
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
			return ActionResult.SUCCESS;
		}
		if (!stack.isEmpty() && entity.heldItem.isEmpty()) {
			stack.decrement(1);
			entity.heldItem = newstack;
			entity.tryCraft(state);
		} else if (!stack.isEmpty()) {
			if (stack.getCount() == 1) {
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
			} else {
				if (!player.giveItemStack(entity.heldItem)) return ActionResult.SUCCESS;
			}
			stack.decrement(1);
			entity.heldItem = newstack;
			entity.tryCraft(state);
		} else {
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
			entity.heldItem = ItemStack.EMPTY;
		}
		((ServerWorld)world).getChunkManager().markForUpdate(pos);

		entity.markDirty();
		world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 10f, 0f);
		return ActionResult.SUCCESS;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		var e = world.getBlockEntity(pos);
		if (!(e instanceof AltarPedestalBlockEntity entity)) return;
		entity.tryCancelCraft(state);
		var stackEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.heldItem);
		world.spawnEntity(stackEntity);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarPedestalBlockEntity abe)) return 0;
		return !abe.heldItem.isEmpty() ? 15 : 0;
	}


	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarPedestalBlockEntity abe)) return 0;
		return abe.crafting ? 15 : 0;
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
		var posDown = pos.down();
		var stateDown = world.getBlockState(posDown);
		if (stateDown.getBlock() == Blocks.SCULK || stateDown.getBlock() == Blocks.AIR) {
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, true), NOTIFY_ALL | FORCE_STATE);
		}
		return charge.getCharge();
	}

	@Override
	public boolean canUpdateOnSpread() {
		return true;
	}
}
