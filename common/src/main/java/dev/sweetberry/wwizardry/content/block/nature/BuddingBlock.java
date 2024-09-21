package dev.sweetberry.wwizardry.content.block.nature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BuddingBlock extends Block {
	public static final int GROWTH_CHANCE = 5;
	private static final Direction[] DIRECTIONS = Direction.values();
	public final Supplier<Block> smallBud;
	public final Supplier<Block> mediumBud;
	public final Supplier<Block> largeBud;
	public final Supplier<Block> cluster;

	public BuddingBlock(BlockBehaviour.Properties properties, Supplier<Block> smallBud, Supplier<Block> mediumBud, Supplier<Block> largeBud, Supplier<Block> cluster) {
		super(properties);
		this.smallBud = smallBud;
		this.mediumBud = mediumBud;
		this.largeBud = largeBud;
		this.cluster = cluster;
	}

	@Override
	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, RandomSource rand) {
		if (rand.nextInt(GROWTH_CHANCE) != 0)
			return;
		var direction = DIRECTIONS[rand.nextInt(DIRECTIONS.length)];
		var budPos = pos.relative(direction);
		var budState = level.getBlockState(budPos);
		Block block = null;
		if (canClusterGrowAtState(budState)) {
			block = smallBud.get();
		} else if (budState.is(smallBud.get()) && budState.getValue(AmethystClusterBlock.FACING) == direction) {
			block = mediumBud.get();
		} else if (budState.is(mediumBud.get()) && budState.getValue(AmethystClusterBlock.FACING) == direction) {
			block = largeBud.get();
		} else if (budState.is(largeBud.get()) && budState.getValue(AmethystClusterBlock.FACING) == direction) {
			block = cluster.get();
		}

		if (block != null) {
			var newBugState = block
				.defaultBlockState()
				.setValue(AmethystClusterBlock.FACING, direction)
				.setValue(AmethystClusterBlock.WATERLOGGED, budState.getFluidState().getType() == Fluids.WATER);
			level.setBlockAndUpdate(budPos, newBugState);
		}
	}

	public static boolean canClusterGrowAtState(BlockState state) {
		return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
	}
}
