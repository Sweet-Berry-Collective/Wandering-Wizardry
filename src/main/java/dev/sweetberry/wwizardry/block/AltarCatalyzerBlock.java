package dev.sweetberry.wwizardry.block;

import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
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
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class AltarCatalyzerBlock extends AltarBlock<AltarCatalyzerBlockEntity> {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(QuiltBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new QuiltItemSettings());
	public static final VoxelShape SHAPE = VoxelShapes.union(
			WanderingBlocks.ALTAR_BASE_SHAPE,

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
