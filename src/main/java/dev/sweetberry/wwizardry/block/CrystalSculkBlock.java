package dev.sweetberry.wwizardry.block;

import net.minecraft.block.*;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.block.sculk.SculkVeinSpreader;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.WorldAccess;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class CrystalSculkBlock extends AmethystBlock implements SculkVeinSpreader {
	public static final CrystalSculkBlock INSTANCE = new CrystalSculkBlock(QuiltBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(1).mapColor(MapColor.DARK_AQUA));

	public CrystalSculkBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int tryUseCharge(
			SculkBehavior.ChargeCursor charge, WorldAccess world, BlockPos pos, RandomGenerator random, SculkBehavior sculkChargeHandler, boolean spread
	) {
		int i = charge.getCharge();
		if (i != 0 && random.nextInt(sculkChargeHandler.getChargeDecayRate()) == 0) {
			BlockPos blockPos = charge.getPos();
			boolean bl = blockPos.isWithinDistance(pos, (double)sculkChargeHandler.getNoGrowthRadius());
			if (!bl && canSpreadTo(world, blockPos)) {
				int j = sculkChargeHandler.getGrowthSpawnCost();
				if (random.nextInt(j) < i) {
					BlockPos blockPos2 = blockPos.up();
					BlockState blockState = this.getRandomGrowthState(world, blockPos2, random, sculkChargeHandler.isWorldGen());
					world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
					world.playSound(null, blockPos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				return Math.max(0, i - j);
			} else {
				return random.nextInt(sculkChargeHandler.getAdditionalDecayRate()) != 0 ? i : i - (bl ? 1 : getDecayPenalty(sculkChargeHandler, blockPos, pos, i));
			}
		} else {
			return i;
		}
	}

	private static int getDecayPenalty(SculkBehavior behavior, BlockPos cursorPos, BlockPos pos, int charge) {
		int i = behavior.getNoGrowthRadius();
		float f = MathHelper.square((float)Math.sqrt(cursorPos.getSquaredDistance(pos)) - (float)i);
		int j = MathHelper.square(24 - i);
		float g = Math.min(1.0F, f / (float)j);
		return Math.max(1, (int)((float)charge * g * 0.5F));
	}

	private BlockState getRandomGrowthState(WorldAccess world, BlockPos pos, RandomGenerator random, boolean randomize) {
		BlockState blockState;
		if (random.nextInt(11) == 0) {
			blockState = Blocks.SCULK_SHRIEKER.getDefaultState().with(SculkShriekerBlock.CAN_SUMMON, Boolean.valueOf(randomize));
		} else {
			blockState = Blocks.SCULK_SENSOR.getDefaultState();
		}

		return blockState.contains(Properties.WATERLOGGED) && !world.getFluidState(pos).isEmpty()
				? blockState.with(Properties.WATERLOGGED, Boolean.valueOf(true))
				: blockState;
	}

	private static boolean canSpreadTo(WorldAccess world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.up());
		if (blockState.isAir() || blockState.isOf(Blocks.WATER) && blockState.getFluidState().isOf(Fluids.WATER)) {
			int i = 0;

			for(BlockPos blockPos : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 2, 4))) {
				BlockState blockState2 = world.getBlockState(blockPos);
				if (blockState2.isOf(Blocks.SCULK_SENSOR) || blockState2.isOf(Blocks.SCULK_SHRIEKER)) {
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
	public boolean canUpdateOnSpread() {
		return false;
	}
}
