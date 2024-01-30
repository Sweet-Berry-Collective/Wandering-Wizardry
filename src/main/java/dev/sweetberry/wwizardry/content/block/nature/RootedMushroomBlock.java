package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.sapling.BeeHoldingSaplingGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class RootedMushroomBlock extends FungusBlock {
	public final TagKey<Block> plantable;

	public RootedMushroomBlock(Settings settings, String baseName, Block block) {
		super(BeeHoldingSaplingGenerator.getId(baseName), block, settings);
		plantable = TagKey.of(RegistryKeys.BLOCK, Mod.id(baseName+"_growable"));
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(plantable) || super.canPlantOnTop(floor, world, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var v = state.getModelOffset(world, pos);
		return super.getOutlineShape(state, world, pos, context).offset(v.x, v.y, v.z);
	}
}
