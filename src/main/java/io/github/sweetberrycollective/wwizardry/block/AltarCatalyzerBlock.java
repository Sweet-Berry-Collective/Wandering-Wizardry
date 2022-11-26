package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class AltarCatalyzerBlock extends BlockWithEntity implements Waterloggable {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(QuiltBlockSettings.of(Material.STONE));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new QuiltItemSettings());
	public static final VoxelShape SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
			createCuboidShape(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
	).simplify();

	public AltarCatalyzerBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
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
		if (player.isSneaking()) return ActionResult.PASS;
		var entity = (AltarCatalyzerBlockEntity)world.getBlockEntity(pos);
		assert entity != null;
		var stack = player.getStackInHand(hand);
		if (stack.isEmpty() && entity.heldItem.isEmpty()) return ActionResult.PASS;
		if (world.isClient) return ActionResult.SUCCESS;
		var newstack = stack.getItem().getDefaultStack();
		if (stack.getItem() == entity.heldItem.getItem()) {
			entity.heldItem = ItemStack.EMPTY;
			stack.increment(1);
			player.setStackInHand(hand, stack);
			entity.markDirty();
			world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 10f, 0f);
			return ActionResult.SUCCESS;
		}
		if (!stack.isEmpty() && entity.heldItem.isEmpty()) {
			stack.decrement(1);
			entity.heldItem = newstack;
		} else if (!stack.isEmpty()) {
			stack.decrement(1);
			if (stack.isEmpty()) {
				boolean inserted = false;
				for (int i = 0; i < player.getInventory().size(); i++) {
					var invStack = player.getInventory().getStack(i);
					if (invStack.getCount() > 64) continue;
					if (invStack.getItem() != entity.heldItem.getItem()) continue;
					inserted = true;
					invStack.increment(1);
					player.getInventory().setStack(i, invStack);
					player.getInventory().markDirty();
				}
				if (!inserted) {
					player.setStackInHand(hand, entity.heldItem);
				}
			} else {
				player.giveItemStack(entity.heldItem);
			}
			entity.heldItem = newstack;
		} else {
			boolean inserted = false;
			for (int i = 0; i < player.getInventory().size(); i++) {
				var invStack = player.getInventory().getStack(i);
				if (invStack.getCount() > 64) continue;
				if (invStack.getItem() != entity.heldItem.getItem()) continue;
				inserted = true;
				invStack.increment(1);
				player.getInventory().setStack(i, invStack);
				player.getInventory().markDirty();
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
		var entity = (AltarCatalyzerBlockEntity)world.getBlockEntity(pos);
		var stackEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.heldItem);
		world.spawnEntity(stackEntity);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
}
