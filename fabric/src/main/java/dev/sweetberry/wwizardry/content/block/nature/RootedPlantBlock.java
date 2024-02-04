package dev.sweetberry.wwizardry.content.block.nature;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.wwizardry.WanderingWizardry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RootedPlantBlock extends BushBlock {
	protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

	public final TagKey<Block> plantable;

	private final MapCodec<RootedPlantBlock> codec;

	public RootedPlantBlock(BlockBehaviour.Properties settings, String baseName) {
		super(settings);
		plantable = TagKey.create(Registries.BLOCK, WanderingWizardry.id(baseName+"_growable"));
		codec = BlockBehaviour.simpleCodec(settings1 -> RootedPlantBlock.this);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return null;
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(plantable) || super.mayPlaceOn(floor, world, pos);
	}
}
