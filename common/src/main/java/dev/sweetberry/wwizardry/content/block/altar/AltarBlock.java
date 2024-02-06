package dev.sweetberry.wwizardry.content.block.altar;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.content.block.Sculkable;
import dev.sweetberry.wwizardry.content.block.entity.AltarBlockEntity;
import dev.sweetberry.wwizardry.content.criterion.CriterionInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public abstract class AltarBlock<T extends AltarBlockEntity> extends BaseEntityBlock implements SimpleWaterloggedBlock, SculkBehaviour, Sculkable {
	public static final VoxelShape ALTAR_BASE_SHAPE = Shapes.or(
		Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
		Block.box(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).optimize();

	private final MapCodec<AltarBlock<T>> codec;

	protected AltarBlock(Properties settings) {
		super(settings);
		registerDefaultState(
				defaultBlockState()
						.setValue(BlockStateProperties.WATERLOGGED, false)
						.setValue(Sculkable.SCULK_INFESTED, false)
						.setValue(Sculkable.SCULK_BELOW, false)
		);
		codec = BlockBehaviour.simpleCodec(settings1 -> AltarBlock.this);
	}

	public abstract BlockEntityType<T> getBlockEntityType();

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return codec;
	}

	public void handleInput(Player player, InteractionHand hand, AltarBlockEntity entity) {
		boolean inserted = false;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			var invStack = player.getInventory().getItem(i);
			if (invStack.getCount() == invStack.getMaxStackSize()) continue;
			if (invStack.getItem() != entity.heldItem.getItem()) continue;
			inserted = true;
			invStack.grow(1);
			player.getInventory().setItem(i, invStack);
			player.getInventory().setChanged();
			break;
		}
		if (!inserted)
			player.setItemInHand(hand, entity.heldItem);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.isShiftKeyDown()) return InteractionResult.PASS;
		var stack = player.getItemInHand(hand);
		var entity = (AltarBlockEntity) world.getBlockEntity(pos);
		assert entity != null;
		if (entity.crafting) return InteractionResult.PASS;
		if (stack.isEmpty() && entity.heldItem.isEmpty()) return InteractionResult.PASS;
		if (world.isClientSide) return InteractionResult.SUCCESS;
		var newstack = stack.copy().copyWithCount(1);

		if (stack.getItem() == entity.heldItem.getItem()) {
			if (stack.getCount() == stack.getMaxStackSize()) {
				if (!player.addItem(entity.heldItem)) return InteractionResult.SUCCESS;
			} else {
				stack.grow(1);
				player.setItemInHand(hand, stack);
			}
			entity.heldItem = ItemStack.EMPTY;
			entity.setChanged();
			world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 7.5f, 0f);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
			return InteractionResult.SUCCESS;
		}

		if (!stack.isEmpty()) {
			if (stack.is(Items.END_CRYSTAL) && player instanceof ServerPlayer serverPlayerEntity)
				CriterionInitializer.ALTAR_END_CRYSTAL.get().trigger(serverPlayerEntity);

			if (!entity.heldItem.isEmpty())
				if (stack.getCount() == 1)
					handleInput(player, hand, entity);
				else if (!player.addItem(entity.heldItem))
					return InteractionResult.SUCCESS;

			stack.shrink(1);
			entity.heldItem = newstack;
			entity.tryCraft(state);
		} else {
			handleInput(player, hand, entity);
			entity.heldItem = ItemStack.EMPTY;
		}

		((ServerLevel) world).getChunkSource().blockChanged(pos);
		world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));

		entity.setChanged();
		world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 10f, 0f);
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return state.setValue(Sculkable.SCULK_BELOW, Sculkable.testForSculk(world, pos.below()));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockAndUpdate(pos, state.setValue(Sculkable.SCULK_BELOW, Sculkable.testForSculk(world, pos.below())));
		if (isComplete(world, state, pos) && placer instanceof ServerPlayer serverPlayerEntity)
			CriterionInitializer.COMPLETE_ALTAR.get().trigger(serverPlayerEntity);
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		var e = world.getBlockEntity(pos);
		if (!(e instanceof AltarBlockEntity entity)) return state;
		entity.tryCancelCraft(state);
		var stackEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.heldItem);
		world.addFreshEntity(stackEntity);
		super.playerWillDestroy(world, pos, state, player);
		return state;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, Sculkable.SCULK_INFESTED, Sculkable.SCULK_BELOW);
	}

	@Nullable
	@Override
	public <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(Level world, BlockState state, BlockEntityType<BE> type) {
		return createTickerHelper(type, getBlockEntityType(), (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public int getPower(BlockGetter world, BlockPos pos, Function<AltarBlockEntity, Boolean> func) {
		var be = world.getBlockEntity(pos);
		if (!(be instanceof AltarBlockEntity abe)) return 0;
		return func.apply(abe) ? 15 : 0;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return getPower(world, pos, abe -> !abe.heldItem.isEmpty());
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return getPower(world, pos, abe -> abe.crafting);
	}

	@Override
	public boolean attemptSpreadVein(LevelAccessor world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean postProcess) {
		var posDown = pos.below();
		var stateDown = world.getBlockState(posDown);

		if (!state.getValue(Sculkable.SCULK_INFESTED)) {
			world.setBlock(pos, state.setValue(Sculkable.SCULK_INFESTED, true).setValue(Sculkable.SCULK_BELOW, Sculkable.testForSculk(world, pos.below())), UPDATE_ALL | UPDATE_KNOWN_SHAPE);
			world.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1, 1);
			return true;
		}

		if (!state.getValue(Sculkable.SCULK_BELOW) && stateDown.is(BlockTags.SCULK_REPLACEABLE)) {
			world.setBlock(posDown, Blocks.SCULK.defaultBlockState(), UPDATE_ALL | UPDATE_KNOWN_SHAPE);
			world.setBlock(pos, state.setValue(Sculkable.SCULK_BELOW, true), UPDATE_ALL | UPDATE_KNOWN_SHAPE);
			world.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1, 1);
			return true;
		}

		return SculkBehaviour.super.attemptSpreadVein(world, pos, state, directions, postProcess);
	}

	@Override
	public int attemptUseCharge(SculkSpreader.ChargeCursor charge, LevelAccessor world, BlockPos pos, RandomSource random, SculkSpreader sculkChargeHandler, boolean spread) {
		var state = world.getBlockState(pos);
		var stateDown = world.getBlockState(pos.below());
		if (stateDown.getBlock() == Blocks.SCULK || stateDown.getBlock() == Blocks.AIR) {
			world.setBlock(pos, state.setValue(Sculkable.SCULK_BELOW, true), UPDATE_ALL | UPDATE_KNOWN_SHAPE);
		}
		return charge.getCharge();
	}

	@Override
	public boolean canChangeBlockStateOnSpread() {
		return true;
	}

	@Override
	public boolean hasPrimaryAction() {
		return true;
	}

	public abstract boolean isComplete(BlockGetter world, BlockState state, BlockPos pos);
}
