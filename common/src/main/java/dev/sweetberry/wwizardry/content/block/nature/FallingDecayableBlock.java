package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.WanderingWizardry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;
import org.jetbrains.annotations.NotNull;

public class FallingDecayableBlock extends NyliumBlock implements Fallable {
	public final Block decayBlock;
	public final ResourceKey<ConfiguredFeature<?, ?>> generator;


	public FallingDecayableBlock(Properties settings, Block decayBlock, String generatorName) {
		super(settings);
		this.decayBlock = decayBlock;
		generator = ResourceKey.create(Registries.CONFIGURED_FEATURE, WanderingWizardry.id(generatorName));
	}

	@Override
	public void onPlace(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean notify) {
		world.scheduleTick(pos, this, this.getFallDelay());
	}

	@Override
	public @NotNull BlockState updateShape(
		@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos
	) {
		world.scheduleTick(pos, this, this.getFallDelay());
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public void tick(@NotNull BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
		if (!canFallThrough(world.getBlockState(pos.below()))) return;
		if (pos.getY() == world.getMinBuildHeight()) return;
		FallingBlockEntity.fall(world, pos, decayBlock.defaultBlockState());
	}

	@Override
	public void performBonemeal(ServerLevel world, @NotNull RandomSource random, BlockPos pos, @NotNull BlockState state) {
		BlockPos blockPos = pos.above();
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
		generate(registry, world, chunkGenerator, random, blockPos);
	}

	void generate(
		Registry<ConfiguredFeature<?, ?>> registry,
		ServerLevel world,
		ChunkGenerator chunkGenerator,
		RandomSource random,
		BlockPos pos
	) {
		registry.getHolder(generator).ifPresent(registryKeyHolder -> registryKeyHolder.value().place(world, chunkGenerator, random, pos));
	}

	/**
	 * Gets the amount of time in ticks this block will wait before attempting to start falling.
	 */
	protected int getFallDelay() {
		return 2;
	}

	@SuppressWarnings("Deprecated")
	public static boolean canFallThrough(BlockState state) {
		return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
	}

	@Override
	public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, RandomSource random) {
		if (random.nextInt(16) == 0) {
			BlockPos blockPos = pos.below();
			if (canFallThrough(world.getBlockState(blockPos))) {
				ParticleUtils.spawnParticleBelow(world, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, decayBlock.defaultBlockState()));
			}
		}
	}

	public static boolean canBeNylium(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState blockState = world.getBlockState(blockPos);
		int i = LightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
		return i < world.getMaxLightLevel();
	}

	@Override
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if (canBeNylium(state, world, pos)) return;
		world.setBlockAndUpdate(pos, decayBlock.defaultBlockState());
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return true;
	}
}
