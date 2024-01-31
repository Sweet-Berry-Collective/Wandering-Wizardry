package dev.sweetberry.wwizardry.content.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

public class CrystalSculkBlock extends AmethystBlock implements SculkBehaviour {
	public static final CrystalSculkBlock INSTANCE = new CrystalSculkBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(1).mapColor(MapColor.ICE));

	public CrystalSculkBlock(Properties settings) {
		super(settings);
	}

	@Override
	public int attemptUseCharge(
			SculkSpreader.ChargeCursor charge, LevelAccessor world, BlockPos pos, RandomSource random, SculkSpreader sculkChargeHandler, boolean spread
	) {
		int i = charge.getCharge();
		if (i != 0 && random.nextInt(sculkChargeHandler.chargeDecayRate()) == 0) {
			BlockPos blockPos = charge.getPos();
			boolean bl = blockPos.closerThan(pos, (double)sculkChargeHandler.noGrowthRadius());
			if (!bl && canSpreadTo(world, blockPos)) {
				int j = sculkChargeHandler.growthSpawnCost();
				if (random.nextInt(j) < i) {
					BlockPos blockPos2 = blockPos.above();
					BlockState blockState = this.getRandomGrowthState(world, blockPos2, random, sculkChargeHandler.isWorldGeneration());
					world.setBlock(blockPos2, blockState, Block.UPDATE_ALL);
					world.playSound(null, blockPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				}

				return Math.max(0, i - j);
			} else {
				return random.nextInt(sculkChargeHandler.additionalDecayRate()) != 0 ? i : i - (bl ? 1 : getDecayPenalty(sculkChargeHandler, blockPos, pos, i));
			}
		} else {
			return i;
		}
	}

	private static int getDecayPenalty(SculkSpreader behavior, BlockPos cursorPos, BlockPos pos, int charge) {
		int i = behavior.noGrowthRadius();
		float f = Mth.square((float)Math.sqrt(cursorPos.distSqr(pos)) - (float)i);
		int j = Mth.square(24 - i);
		float g = Math.min(1.0F, f / (float)j);
		return Math.max(1, (int)((float)charge * g * 0.5F));
	}

	private BlockState getRandomGrowthState(LevelAccessor world, BlockPos pos, RandomSource random, boolean randomize) {
		BlockState blockState;
		if (random.nextInt(11) == 0) {
			blockState = Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(randomize));
		} else {
			blockState = Blocks.SCULK_SENSOR.defaultBlockState();
		}

		return blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !world.getFluidState(pos).isEmpty()
				? blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true))
				: blockState;
	}

	private static boolean canSpreadTo(LevelAccessor world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.above());
		if (blockState.isAir() || blockState.is(Blocks.WATER) && blockState.getFluidState().is(Fluids.WATER)) {
			int i = 0;

			for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 2, 4))) {
				BlockState blockState2 = world.getBlockState(blockPos);
				if (blockState2.is(Blocks.SCULK_SENSOR) || blockState2.is(Blocks.SCULK_SHRIEKER)) {
					++i;
				}

				if (i > 2) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canChangeBlockStateOnSpread() {
		return false;
	}
}
