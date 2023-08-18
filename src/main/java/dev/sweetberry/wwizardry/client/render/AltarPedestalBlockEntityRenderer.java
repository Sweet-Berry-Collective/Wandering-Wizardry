package dev.sweetberry.wwizardry.client.render;

import dev.sweetberry.wwizardry.block.AltarPedestalBlock;
import dev.sweetberry.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;

public class AltarPedestalBlockEntityRenderer implements AltarBlockEntityRenderer<AltarPedestalBlockEntity> {

	public AltarPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void beforeRender(AltarPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.translate(0.5, 0.9509, 0.5);

		var world = entity.getWorld();

		if (world == null)
			return;

		var blockState = world.getBlockState(entity.getPos());

		if (!blockState.isOf(AltarPedestalBlock.INSTANCE))
			return;

		var dir = blockState.get(HorizontalFacingBlock.FACING);

		switch (dir) {
			case NORTH -> {
				matrices.translate(0, 0, 0.09335);
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(22.5f));
				matrices.translate(0, 0, -0.046875);
			}
			case SOUTH -> {
				matrices.translate(0, 0, -0.09335);
				matrices.multiply(Axis.X_NEGATIVE.rotationDegrees(22.5f));
				matrices.translate(0, 0, 0.046875);
			}
			case EAST -> {
				matrices.translate(0.09335, 0, 0);
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(22.5f));
				matrices.translate(-0.125, 0.0859375, 0);

			}
			case WEST -> {
				matrices.translate(-0.09335, 0, 0);
				matrices.multiply(Axis.Z_NEGATIVE.rotationDegrees(22.5f));
				matrices.translate(0.125, 0.0859375, 0);
			}
		}

		matrices.translate(0, 0.25, 0);
	}
}
