package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class AltarCatalyzerBlock extends BlockWithEntity {
	public static final AltarCatalyzerBlock INSTANCE = new AltarCatalyzerBlock(QuiltBlockSettings.of(Material.STONE));
	public static final BlockItem ITEM = new BlockItem(INSTANCE, new QuiltItemSettings());
	public static final VoxelShape SHAPE = VoxelShapes.union(
			createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			createCuboidShape(4.0, 2.0 , 4.0, 12.0, 15.0, 12.0),
			createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
			createCuboidShape(3.0, 16.0, 3.0, 13.0, 17.0, 13.0)
	).simplify();



	public AltarCatalyzerBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AltarCatalyzerBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, AltarCatalyzerBlockEntity.TYPE, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}
}
