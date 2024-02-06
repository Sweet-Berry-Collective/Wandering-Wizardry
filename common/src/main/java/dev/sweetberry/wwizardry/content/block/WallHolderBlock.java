package dev.sweetberry.wwizardry.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class WallHolderBlock extends Block {
	public static final HashMap<Block, WallHolderBlock> ITEM_LOOKUP = new HashMap<>();

	public static final VoxelShape NORTH_SHAPE = box(6, 0, 0, 10, 6, 6);
	public static final VoxelShape SOUTH_SHAPE = box(6, 0, 10, 10, 6, 16);
	public static final VoxelShape EAST_SHAPE = box(10, 0, 6, 16, 6, 10);
	public static final VoxelShape WEST_SHAPE = box(0, 0, 6, 6, 6, 10);

	public WallHolderBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
			case SOUTH -> SOUTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case WEST -> WEST_SHAPE;
			default -> NORTH_SHAPE;
		};
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos blockPos, RandomSource random) {
		if (shouldSpawnParticle(state)) return;

		var particle = getParticle(state);

		var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

		var vec = getOffset(dir, getParticleHeight(state)).add(blockPos.getX(), blockPos.getY(), blockPos.getZ());

		spawnParticle(world, vec, particle, random);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = this.defaultBlockState();
		LevelReader worldView = ctx.getLevel();
		BlockPos blockPos = ctx.getClickedPos();
		Direction[] directions = ctx.getNearestLookingDirections();

		for(Direction direction : directions) {
			if (direction.getAxis().isHorizontal()) {
				blockState = blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
				if (blockState.canSurvive(worldView, blockPos)) {
					return blockState;
				}
			}
		}

		return null;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		BlockPos blockPos = pos.relative(direction);
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isFaceSturdy(world, blockPos, direction);
	}

	@Override
	public BlockState updateShape(
		BlockState state,
		Direction direction,
		BlockState neighborState,
		LevelAccessor world,
		BlockPos pos,
		BlockPos neighborPos
	) {
		return direction == state.getValue(BlockStateProperties.HORIZONTAL_FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getBlock() == BlockInitializer.WALL_HOLDER.get()) return useEmpty(state, world, pos, player, hand);

		var droppedBlock = getDroppedBlock();
		if (droppedBlock != null && player.isShiftKeyDown()) {
			if (!player.isCreative()) {
				var stackEntity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, droppedBlock.asItem().getDefaultInstance());
				world.addFreshEntity(stackEntity);
			}
			var soundGroup = droppedBlock.getSoundType(droppedBlock.defaultBlockState());
			world.playSound(player, pos, soundGroup.getBreakSound(), SoundSource.BLOCKS);
			world.setBlockAndUpdate(pos, BlockInitializer.WALL_HOLDER.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
			return InteractionResult.SUCCESS;
		}

		return specializedUseAction(state, world, pos, player, hand, hit);
	}

	@Nullable
	public Block getDroppedBlock() {
		return null;
	}

	public InteractionResult specializedUseAction(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	public InteractionResult useEmpty(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) return InteractionResult.PASS;

		var stack = player.getItemInHand(hand);

		if (!(stack.getItem() instanceof BlockItem item)) return InteractionResult.PASS;
		if (!ITEM_LOOKUP.containsKey(item.getBlock())) return InteractionResult.PASS;

		var block = item.getBlock();
		var holder = ITEM_LOOKUP.get(block);
		var soundGroup = block.getSoundType(block.defaultBlockState());
		world.playSound(player, pos, soundGroup.getPlaceSound(), SoundSource.BLOCKS);
		world.setBlockAndUpdate(pos, holder.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)));

		if (!player.isCreative()) {
			stack.shrink(1);
			player.setItemInHand(hand, stack);
		}

		return InteractionResult.SUCCESS;
	}

	public void spawnParticle(Level world, Vec3 pos, ParticleOptions particleEffect, RandomSource random) {
		world.addParticle(particleEffect, pos.x, pos.y, pos.z, 0, 0, 0);
	}

	public Vec3 getOffset(Direction dir, int heightOffset) {
		double y = (heightOffset+7d)/16d;
		return getDirVector(dir).add(0, y, 0);
	}

	public Vec3 getDirVector(Direction dir) {
		return switch (dir) {
			case NORTH -> new Vec3(8d/16d, 0, 3.25d/16d);
			case SOUTH -> new Vec3(8d/16d, 0, 12.75d/16d);
			case EAST -> new Vec3(12.75d/16d, 0, 8d/16d);
			case WEST -> new Vec3(3.25d/16d, 0, 8d/16d);
			default -> Vec3.ZERO;
		};
	}

	public boolean shouldSpawnParticle(BlockState state) {
		return getParticle(state) == null;
	}

	@Nullable
	public ParticleOptions getParticle(BlockState state) {
		return null;
	}

	public int getParticleHeight(BlockState state) {
		return 0;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		if (getDroppedBlock() != null) return getDroppedBlock().getCloneItemStack(world, pos, state);
		return BlockInitializer.WALL_HOLDER.get().asItem().getDefaultInstance();
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
	}
}
