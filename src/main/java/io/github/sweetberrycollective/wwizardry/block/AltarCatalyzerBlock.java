package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class AltarCatalyzerBlock extends BlockWithEntity implements Waterloggable {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(QuiltBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new QuiltItemSettings());
	public static final VoxelShape SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
			createCuboidShape(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
	).simplify();

	public AltarCatalyzerBlock(Settings settings) {
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
		var down = world.getBlockState(pos.down()).getBlock();
		return state.with(WanderingBlocks.SCULK_BELOW, down == Blocks.SCULK || down == Blocks.AIR);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		var down = world.getBlockState(pos.down()).getBlock();
		world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, down == Blocks.SCULK || down == Blocks.AIR));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, WanderingBlocks.SCULK_INFESTED, WanderingBlocks.SCULK_BELOW);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarCatalyzerBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, AltarCatalyzerBlockEntity.TYPE, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);
		var entity = (AltarCatalyzerBlockEntity) world.getBlockEntity(pos);
		assert entity != null;
		if (entity.crafting) return ActionResult.PASS;
		if (stack.isEmpty() && entity.heldItem.isEmpty()) return ActionResult.PASS;
		if (world.isClient) return ActionResult.SUCCESS;
		var newstack = stack.getItem().getDefaultStack();
		if (stack.getItem() == entity.heldItem.getItem()) {
			if (stack.getCount() == stack.getMaxCount()) {
				player.giveItemStack(entity.heldItem);
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
			entity.tryCraft();
		} else if (!stack.isEmpty()) {
			stack.decrement(1);
			if (stack.isEmpty()) {
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
				player.giveItemStack(entity.heldItem);
			}
			entity.heldItem = newstack;
			entity.tryCraft();
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
		if (!(e instanceof AltarCatalyzerBlockEntity entity)) return;
		entity.cancelCraft();
		var stackEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.heldItem);
		world.spawnEntity(stackEntity);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarCatalyzerBlockEntity abe)) return 0;
		return !abe.heldItem.isEmpty() ? 15 : 0;
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarCatalyzerBlockEntity abe)) return 0;
		return abe.crafting ? 15 : 0;
	}
}
