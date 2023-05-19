package io.github.sweetberrycollective.wwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class WallCandleBlock extends WallHolderBlock {
	public final CandleBlock candleBlock;

	public static final VoxelShape CANDLE_NORTH = createCuboidShape(7, 6, 2.25, 9, 11, 4.25);
	public static final VoxelShape CANDLE_SOUTH = createCuboidShape(7, 6, 11.75, 9, 11, 13.75);
	public static final VoxelShape CANDLE_EAST = createCuboidShape(11.75, 6, 7, 13.75, 11, 9);
	public static final VoxelShape CANDLE_WEST = createCuboidShape(2.25, 6, 7, 4.25, 11, 9);

	public WallCandleBlock(Settings settings, CandleBlock candleBlock) {
		super(settings.luminance(state -> state.get(Properties.LIT) ? 3 : 0));
		this.candleBlock = candleBlock;
		ITEM_LOOKUP.put(candleBlock, this);
		setDefaultState(getDefaultState().with(Properties.LIT, false));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.union(super.getOutlineShape(state, world, pos, context), switch (state.get(Properties.HORIZONTAL_FACING)) {
			case SOUTH -> CANDLE_SOUTH;
			case EAST -> CANDLE_EAST;
			case WEST -> CANDLE_WEST;
			default -> CANDLE_NORTH;
		});
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.LIT);
	}

	@Override
	public @Nullable ParticleEffect getParticle(BlockState state) {
		if (!state.get(Properties.LIT)) return null;
		return ParticleTypes.SMALL_FLAME;
	}

	@Override
	public void spawnParticle(World world, Vec3d pos, ParticleEffect particleEffect, RandomGenerator random) {
		super.spawnParticle(world, pos, particleEffect, random);
		var randf = random.nextFloat();
		if (randf < 0.3F)
			world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
		if (randf < 0.17F)
			world.playSound(pos.x, pos.y, pos.z, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
	}

	@Override
	public int getParticleHeight(BlockState state) {
		return 6;
	}

	@Override
	public @Nullable Block getDroppedBlock() {
		return candleBlock;
	}

	@Override
	public ActionResult specializedUseAction(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).isEmpty() && state.get(Properties.LIT)) {
			extinguish(state, world, pos);
			return ActionResult.SUCCESS;
		} else if (player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL) && !state.get(Properties.LIT)) {
			light(player, state, world, pos);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	public void extinguish(BlockState state, World world, BlockPos pos) {
		var vec = getOffset(state.get(Properties.HORIZONTAL_FACING), getParticleHeight(state)).add(pos.getX(), pos.getY(), pos.getZ());
		world.addParticle(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, 0.0, 0.1F, 0.0);
		world.setBlockState(pos, state.with(Properties.LIT, false));
		world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public void light(PlayerEntity player, BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, state.with(Properties.LIT, true));
		world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
	}
}
