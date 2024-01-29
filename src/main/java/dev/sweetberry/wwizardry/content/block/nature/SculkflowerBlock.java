package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.Sculkable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.block.sculk.SculkVeinSpreader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class SculkflowerBlock extends FlowerBlock implements Sculkable, SculkVeinSpreader {
	public static final SculkflowerBlock INSTANCE = new SculkflowerBlock(StatusEffects.DARKNESS, 30, QuiltBlockSettings.copyOf(Blocks.POPPY).offsetType(OffsetType.NONE));

	public SculkflowerBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		setDefaultState(getDefaultState().with(BlockInitializer.SCULK_INFESTED, false));
	}
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BlockInitializer.SCULK_BELOW);
		builder.add(BlockInitializer.SCULK_INFESTED);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.SCULK);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		var sup = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		if (sup.isAir()) return sup;
		return sup.with(BlockInitializer.SCULK_BELOW, BlockInitializer.testForSculk(world, pos.down()));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos, state.with(BlockInitializer.SCULK_BELOW, BlockInitializer.testForSculk(world, pos.down())));
	}

	@Override
	public boolean hasPrimaryAction() {
		return false;
	}

	@Override
	public int tryUseCharge(SculkBehavior.ChargeCursor charge, WorldAccess world, BlockPos pos, RandomGenerator random, SculkBehavior sculkChargeHandler, boolean spread) {
		var state = world.getBlockState(pos);
		var stateDown = world.getBlockState(pos.down());
		if (stateDown.getBlock() == Blocks.SCULK || stateDown.getBlock() == Blocks.AIR) {
			world.setBlockState(pos, state.with(BlockInitializer.SCULK_BELOW, true), NOTIFY_ALL | FORCE_STATE);
		}
		return charge.getCharge();
	}
}
