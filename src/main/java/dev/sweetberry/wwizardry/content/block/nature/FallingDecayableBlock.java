package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.block.*;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FallingDecayableBlock extends NyliumBlock implements LandingBlock {
	public final Block decayBlock;
	public final RegistryKey<ConfiguredFeature<?, ?>> generator;


	public FallingDecayableBlock(Settings settings, Block decayBlock, String generatorName) {
		super(settings);
		this.decayBlock = decayBlock;
		generator = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Mod.id(generatorName));
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.scheduleBlockTick(pos, this, this.getFallDelay());
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		world.scheduleBlockTick(pos, this, this.getFallDelay());
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (!canFallThrough(world.getBlockState(pos.down()))) return;
		if (pos.getY() == world.getBottomY()) return;
		FallingBlockEntity.fall(world, pos, decayBlock.getDefaultState());
	}

	@Override
	public void fertilize(ServerWorld world, RandomGenerator random, BlockPos pos, BlockState state) {
		BlockPos blockPos = pos.up();
		ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE);
		generate(registry, world, chunkGenerator, random, blockPos);
	}

	void generate(
		Registry<ConfiguredFeature<?, ?>> registry,
		ServerWorld world,
		ChunkGenerator chunkGenerator,
		RandomGenerator random,
		BlockPos pos
	) {
		registry.getHolder(generator).ifPresent(registryKeyHolder -> registryKeyHolder.value().generate(world, chunkGenerator, random, pos));
	}

	/**
	 * Gets the amount of time in ticks this block will wait before attempting to start falling.
	 */
	protected int getFallDelay() {
		return 2;
	}

	public static boolean canFallThrough(BlockState state) {
		return state.isAir() || state.isIn(BlockTags.FIRE) || state.isLiquid() || state.materialReplaceable();
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (random.nextInt(16) == 0) {
			BlockPos blockPos = pos.down();
			if (canFallThrough(world.getBlockState(blockPos))) {
				ParticleUtil.spawnParticle(world, pos, random, new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, decayBlock.getDefaultState()));
			}
		}
	}

	public static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = world.getBlockState(blockPos);
		int i = ChunkLightProvider.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
		return i < world.getMaxLightLevel();
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (stayAlive(state, world, pos)) return;
		world.setBlockState(pos, decayBlock.getDefaultState());
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
}
