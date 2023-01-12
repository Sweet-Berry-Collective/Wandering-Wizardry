package io.github.sweetberrycollective.wwizardry.client.render;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class AltarPedestalBlockEntityRenderer implements AltarBlockEntityRenderer<AltarPedestalBlockEntity> {

	public AltarPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void beforeRender(AltarPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.translate(0.5, 0.9509, 0.5);

		var blockState = entity.getWorld().getBlockState(entity.getPos());
		var dir = blockState.get(HorizontalFacingBlock.FACING);

		switch (dir) {
			case NORTH -> {
				matrices.translate(0, 0, 0.09335);
				matrices.multiply(new Quaternionf().rotationX((float) Math.toRadians(22.5)));
				matrices.translate(0, 0, -0.046875);
			}
			case SOUTH -> {
				matrices.translate(0, 0, -0.09335);
				matrices.multiply(new Quaternionf().rotationX((float) Math.toRadians(-22.5)));
				matrices.translate(0, 0, 0.046875);
			}
			case EAST -> {
				matrices.translate(0.09335, 0, 0);
				matrices.multiply(new Quaternionf().rotationZ((float) Math.toRadians(22.5)));
				matrices.translate(-0.125, 0.0859375, 0);

			}
			case WEST -> {
				matrices.translate(-0.09335, 0, 0);
				matrices.multiply(new Quaternionf().rotationZ((float) Math.toRadians(-22.5)));
				matrices.translate(0.125, 0.0859375, 0);
			}
		}

		matrices.translate(0, 0.25, 0);
	}
}
