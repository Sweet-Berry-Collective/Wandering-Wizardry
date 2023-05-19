package dev.sweetberry.wwizardry.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class CameraBlock extends Block implements Waterloggable {
	public static final CameraBlock INSTANCE = new CameraBlock(QuiltBlockSettings.of(Material.METAL));

	public static final VoxelShape SHAPE = createCuboidShape(4, 3, 4, 12, 16, 12);

	public CameraBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, HorizontalFacingBlock.FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
	}
}
