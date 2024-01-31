package dev.sweetberry.wwizardry.content.block.nature;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.world.sapling.BeeHoldingSaplingGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RootedMushroomBlock extends FungusBlock {
	public final TagKey<Block> plantable;

	public RootedMushroomBlock(Properties settings, String baseName, Block block) {
		super(BeeHoldingSaplingGenerator.getId(baseName), block, settings);
		plantable = TagKey.create(Registries.BLOCK, Mod.id(baseName+"_growable"));
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(plantable) || super.mayPlaceOn(floor, world, pos);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		var v = state.getOffset(world, pos);
		return super.getShape(state, world, pos, context).move(v.x, v.y, v.z);
	}
}
