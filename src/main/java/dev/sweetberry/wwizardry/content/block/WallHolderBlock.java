package dev.sweetberry.wwizardry.content.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class WallHolderBlock extends Block {
	public static final WallHolderBlock EMPTY = new WallHolderBlock(FabricBlockSettings.create().breakInstantly().mapColor(MapColor.GRAY));
	public static final HashMap<Block, WallHolderBlock> ITEM_LOOKUP = new HashMap<>();

	public static final VoxelShape NORTH_SHAPE = createCuboidShape(6, 0, 0, 10, 6, 6);
	public static final VoxelShape SOUTH_SHAPE = createCuboidShape(6, 0, 10, 10, 6, 16);
	public static final VoxelShape EAST_SHAPE = createCuboidShape(10, 0, 6, 16, 6, 10);
	public static final VoxelShape WEST_SHAPE = createCuboidShape(0, 0, 6, 6, 6, 10);

	public WallHolderBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
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

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, RandomGenerator random) {
		if (shouldSpawnParticle(state)) return;

		var particle = getParticle(state);

		var dir = state.get(Properties.HORIZONTAL_FACING);

		var vec = getOffset(dir, getParticleHeight(state)).add(blockPos.getX(), blockPos.getY(), blockPos.getZ());

		spawnParticle(world, vec, particle, random);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = this.getDefaultState();
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction[] directions = ctx.getPlacementDirections();

		for(Direction direction : directions) {
			if (direction.getAxis().isHorizontal()) {
				blockState = blockState.with(Properties.HORIZONTAL_FACING, direction);
				if (blockState.canPlaceAt(worldView, blockPos)) {
					return blockState;
				}
			}
		}

		return null;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		BlockPos blockPos = pos.offset(direction);
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isSideSolidFullSquare(world, blockPos, direction);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		Direction direction,
		BlockState neighborState,
		WorldAccess world,
		BlockPos pos,
		BlockPos neighborPos
	) {
		return direction == state.get(Properties.HORIZONTAL_FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (state.getBlock() == EMPTY) return useEmpty(state, world, pos, player, hand);

		var droppedBlock = getDroppedBlock();
		if (droppedBlock != null && player.isSneaking()) {
			if (!player.isCreative()) {
				var stackEntity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, droppedBlock.asItem().getDefaultStack());
				world.spawnEntity(stackEntity);
			}
			var soundGroup = droppedBlock.getSoundGroup(droppedBlock.getDefaultState());
			world.playSound(player, pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS);
			world.setBlockState(pos, EMPTY.getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)));
			return ActionResult.SUCCESS;
		}

		return specializedUseAction(state, world, pos, player, hand, hit);
	}

	@Nullable
	public Block getDroppedBlock() {
		return null;
	}

	public ActionResult specializedUseAction(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return ActionResult.PASS;
	}

	public ActionResult useEmpty(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
		if (player.shouldCancelInteraction()) return ActionResult.PASS;

		var stack = player.getStackInHand(hand);

		if (!(stack.getItem() instanceof BlockItem item)) return ActionResult.PASS;
		if (!ITEM_LOOKUP.containsKey(item.getBlock())) return ActionResult.PASS;

		var block = item.getBlock();
		var holder = ITEM_LOOKUP.get(block);
		var soundGroup = block.getSoundGroup(block.getDefaultState());
		world.playSound(player, pos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS);
		world.setBlockState(pos, holder.getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)));

		if (!player.isCreative()) {
			stack.decrement(1);
			player.setStackInHand(hand, stack);
		}

		return ActionResult.SUCCESS;
	}

	public void spawnParticle(World world, Vec3d pos, ParticleEffect particleEffect, RandomGenerator random) {
		world.addParticle(particleEffect, pos.x, pos.y, pos.z, 0, 0, 0);
	}

	public Vec3d getOffset(Direction dir, int heightOffset) {
		double y = (heightOffset+7d)/16d;
		return getDirVector(dir).add(0, y, 0);
	}

	public Vec3d getDirVector(Direction dir) {
		return switch (dir) {
			case NORTH -> new Vec3d(8d/16d, 0, 3.25d/16d);
			case SOUTH -> new Vec3d(8d/16d, 0, 12.75d/16d);
			case EAST -> new Vec3d(12.75d/16d, 0, 8d/16d);
			case WEST -> new Vec3d(3.25d/16d, 0, 8d/16d);
			default -> Vec3d.ZERO;
		};
	}

	public boolean shouldSpawnParticle(BlockState state) {
		return getParticle(state) == null;
	}

	@Nullable
	public ParticleEffect getParticle(BlockState state) {
		return null;
	}

	public int getParticleHeight(BlockState state) {
		return 0;
	}

	@Override
	public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
		if (getDroppedBlock() != null) return getDroppedBlock().getPickStack(world, pos, state);
		return EMPTY.asItem().getDefaultStack();
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(HorizontalFacingBlock.FACING, rotation.rotate(state.get(HorizontalFacingBlock.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)));
	}
}
