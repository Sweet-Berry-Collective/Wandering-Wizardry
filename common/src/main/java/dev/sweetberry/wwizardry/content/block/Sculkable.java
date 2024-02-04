package dev.sweetberry.wwizardry.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface Sculkable {

	BooleanProperty SCULK_INFESTED = BooleanProperty.create("sculked");
	BooleanProperty SCULK_BELOW = BooleanProperty.create("sculk_below");

	boolean hasPrimaryAction();

	static boolean testForSculk(BlockGetter world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isFaceSturdy(world, pos, Direction.UP);
	}
}
