package io.github.sweetberrycollective.wwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class SculkflowerBlock extends FlowerBlock implements Sculkable {
	public static final SculkflowerBlock INSTANCE = new SculkflowerBlock(StatusEffects.DARKNESS, 30, QuiltBlockSettings.copyOf(Blocks.POPPY));

	public SculkflowerBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		setDefaultState(getDefaultState().with(WanderingBlocks.SCULK_INFESTED, false));
	}
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WanderingBlocks.SCULK_BELOW);
		builder.add(WanderingBlocks.SCULK_INFESTED);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.SCULK);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return state.with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(world, pos.down()));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos, state.with(WanderingBlocks.SCULK_BELOW, WanderingBlocks.testForSculk(world, pos.down())));
	}

	@Override
	public boolean hasPrimaryAction() {
		return false;
	}
}
