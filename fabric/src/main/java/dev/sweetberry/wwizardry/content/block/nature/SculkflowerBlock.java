package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.Sculkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class SculkflowerBlock extends FlowerBlock implements Sculkable, SculkBehaviour {
	public static final SculkflowerBlock INSTANCE = new SculkflowerBlock(MobEffects.DARKNESS, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).offsetType(OffsetType.NONE));

	public SculkflowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		registerDefaultState(defaultBlockState().setValue(BlockInitializer.SCULK_INFESTED, false));
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockInitializer.SCULK_BELOW);
		builder.add(BlockInitializer.SCULK_INFESTED);
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return super.mayPlaceOn(floor, world, pos) || floor.is(Blocks.SCULK);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		var sup = super.updateShape(state, direction, neighborState, world, pos, neighborPos);
		if (sup.isAir()) return sup;
		return sup.setValue(BlockInitializer.SCULK_BELOW, BlockInitializer.testForSculk(world, pos.below()));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockAndUpdate(pos, state.setValue(BlockInitializer.SCULK_BELOW, BlockInitializer.testForSculk(world, pos.below())));
	}

	@Override
	public boolean hasPrimaryAction() {
		return false;
	}

	@Override
	public int attemptUseCharge(SculkSpreader.ChargeCursor charge, LevelAccessor world, BlockPos pos, RandomSource random, SculkSpreader sculkChargeHandler, boolean spread) {
		var state = world.getBlockState(pos);
		var stateDown = world.getBlockState(pos.below());
		if (stateDown.getBlock() == Blocks.SCULK || stateDown.getBlock() == Blocks.AIR) {
			world.setBlock(pos, state.setValue(BlockInitializer.SCULK_BELOW, true), UPDATE_ALL | UPDATE_KNOWN_SHAPE);
		}
		return charge.getCharge();
	}
}
