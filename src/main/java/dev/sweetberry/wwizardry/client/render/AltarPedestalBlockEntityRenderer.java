package dev.sweetberry.wwizardry.client.render;

import dev.sweetberry.wwizardry.block.AltarPedestalBlock;
import dev.sweetberry.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import org.joml.Quaternionf;

public record AltarPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) implements AltarBlockEntityRenderer<AltarPedestalBlockEntity> {
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

		var transDir = dir.getUnitVector().mul(-0.09335f);
		matrices.translate(transDir.x, transDir.y, transDir.z);

		matrices.multiply(getAxis(dir.rotateYClockwise()).rotationDegrees(22.5f));

		transDir = dir.getUnitVector().mul(0.046875f);
		matrices.translate(transDir.x, transDir.y + 0.25, transDir.z);
	}

	public static Axis getAxis(Direction dir) {
		var mul = dir.getDirection().offset();
		return (amount) -> {
			var quat = new Quaternionf();
			var amp = mul*amount;
			return switch (dir.getAxis()) {
				case X -> quat.rotationX(amp);
				case Y -> quat.rotationY(amp);
				case Z -> quat.rotationZ(amp);
			};
		};
	}
}
