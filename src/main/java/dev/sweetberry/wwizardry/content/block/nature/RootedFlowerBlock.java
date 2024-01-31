package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RootedFlowerBlock extends FlowerBlock {
	public static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
	public final TagKey<Block> tag;

	public RootedFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, String tagName, Properties settings) {
		super(suspiciousStewEffect, effectDuration, settings);
		tag = TagKey.create(Registries.BLOCK, Mod.id(tagName));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 vec3d = state.getOffset(world, pos);
		return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(tag) || super.mayPlaceOn(floor, world, pos);
	}
}
