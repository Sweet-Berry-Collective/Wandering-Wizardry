package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SpecialFlowerBlock extends FlowerBlock {
	public static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
	public final TagKey<Block> tag;

	public SpecialFlowerBlock(StatusEffect suspiciousStewEffect, int effectDuration, String tagName, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		tag = TagKey.of(RegistryKeys.BLOCK, WanderingMod.id(tagName));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Vec3d vec3d = state.getModelOffset(world, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(tag) || super.canPlantOnTop(floor, world, pos);
	}
}
