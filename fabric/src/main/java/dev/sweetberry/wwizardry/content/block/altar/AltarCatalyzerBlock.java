package dev.sweetberry.wwizardry.content.block.altar;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AltarCatalyzerBlock extends AltarBlock<AltarCatalyzerBlockEntity> {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new Item.Properties());
	public static final VoxelShape SHAPE = Shapes.or(
			BlockInitializer.ALTAR_BASE_SHAPE,

			box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
			box(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
	).optimize();

	public AltarCatalyzerBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState());
	}

	@Override
	public BlockEntityType<AltarCatalyzerBlockEntity> getBlockEntityType() {
		return AltarCatalyzerBlockEntity.TYPE;
	}

	@Override
	public boolean isComplete(BlockGetter world, BlockState state, BlockPos pos) {
		Direction[] directions = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };
		for (var direction : directions) {
			var neighbor = world.getBlockState(pos.relative(direction, 2));
			if (!neighbor.is(AltarPedestalBlock.INSTANCE))
				return false;
			if (neighbor.getValue(HorizontalDirectionalBlock.FACING) != direction)
				return false;
		}
		return true;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AltarCatalyzerBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
