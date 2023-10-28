package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SpecialRootsBlock extends PlantBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

	public final TagKey<Block> plantable;

	public SpecialRootsBlock(AbstractBlock.Settings settings, String baseName) {
		super(settings);
		plantable = TagKey.of(RegistryKeys.BLOCK, WanderingMod.id(baseName+"_growable"));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(plantable) || super.canPlantOnTop(floor, world, pos);
	}
}
