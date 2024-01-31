package dev.sweetberry.wwizardry.content.block;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallCandleBlock extends WallHolderBlock {
	public final CandleBlock candleBlock;

	public static final VoxelShape CANDLE_NORTH = box(7, 6, 2.25, 9, 11, 4.25);
	public static final VoxelShape CANDLE_SOUTH = box(7, 6, 11.75, 9, 11, 13.75);
	public static final VoxelShape CANDLE_EAST = box(11.75, 6, 7, 13.75, 11, 9);
	public static final VoxelShape CANDLE_WEST = box(2.25, 6, 7, 4.25, 11, 9);

	public WallCandleBlock(Properties settings, CandleBlock candleBlock) {
		super(settings.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 5 : 0));
		this.candleBlock = candleBlock;
		ITEM_LOOKUP.put(candleBlock, this);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.LIT, false));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		var stacks = new ArrayList<>(EMPTY.getDrops(state, builder));
		stacks.add(new ItemStack(candleBlock));
		return stacks;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.or(super.getShape(state, world, pos, context), switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case SOUTH -> CANDLE_SOUTH;
			case EAST -> CANDLE_EAST;
			case WEST -> CANDLE_WEST;
			default -> CANDLE_NORTH;
		});
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.LIT);
	}

	@Override
	public @Nullable ParticleOptions getParticle(BlockState state) {
		if (!state.getValue(BlockStateProperties.LIT)) return null;
		return ParticleTypes.SMALL_FLAME;
	}

	@Override
	public void spawnParticle(Level world, Vec3 pos, ParticleOptions particleEffect, RandomSource random) {
		super.spawnParticle(world, pos, particleEffect, random);
		var randf = random.nextFloat();
		if (randf < 0.3F)
			world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
		if (randf < 0.17F)
			world.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
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
	public InteractionResult specializedUseAction(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var handStack = player.getItemInHand(hand);
		if (handStack.isEmpty() && state.getValue(BlockStateProperties.LIT)) {
			extinguish(state, world, pos);
			return InteractionResult.SUCCESS;
		} else if (handStack.is(Items.FLINT_AND_STEEL) && !state.getValue(BlockStateProperties.LIT)) {
			if (world instanceof ServerLevel)
				handStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			light(player, state, world, pos);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public void extinguish(BlockState state, Level world, BlockPos pos) {
		var vec = getOffset(state.getValue(BlockStateProperties.HORIZONTAL_FACING), getParticleHeight(state)).add(pos.getX(), pos.getY(), pos.getZ());
		world.addParticle(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, 0.0, 0.1F, 0.0);
		world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
		world.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	public void light(Player player, BlockState state, Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
		world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
	}
}
