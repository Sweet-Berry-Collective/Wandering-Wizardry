package dev.sweetberry.wwizardry.content.block.altar;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AltarCatalyzerBlock extends AltarBlock<AltarCatalyzerBlockEntity> {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new FabricItemSettings());
	public static final VoxelShape SHAPE = VoxelShapes.union(
			BlockInitializer.ALTAR_BASE_SHAPE,

			createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
			createCuboidShape(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
	).simplify();

	public AltarCatalyzerBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState());
	}

	@Override
	public BlockEntityType<AltarCatalyzerBlockEntity> getBlockEntityType() {
		return AltarCatalyzerBlockEntity.TYPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarCatalyzerBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
