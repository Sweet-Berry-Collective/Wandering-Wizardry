package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class RootedPlantBlock extends PlantBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

	public final TagKey<Block> plantable;

	public RootedPlantBlock(AbstractBlock.Settings settings, String baseName) {
		super(settings);
		plantable = TagKey.of(RegistryKeys.BLOCK, Mod.id(baseName+"_growable"));
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
