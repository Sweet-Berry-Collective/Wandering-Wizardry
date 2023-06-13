package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.WanderingSaplingGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SpecialMushroomBlock extends FungusBlock {
	public final TagKey<Block> plantable;

	public SpecialMushroomBlock(Settings settings, String baseName, Block block) {
		super(settings, WanderingSaplingGenerator.getId(baseName), block);
		this.plantable = TagKey.of(RegistryKeys.BLOCK, WanderingMod.id(baseName+"_growable"));
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
