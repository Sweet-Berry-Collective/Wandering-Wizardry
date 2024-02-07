package dev.sweetberry.wwizardry.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import org.joml.Quaternionf;

public record AltarPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements AltarBlockEntityRenderer<AltarPedestalBlockEntity> {
	private static final double ALTAR_TOP = 16.1854d / 16d;
	private static final double ALTAR_OFFSET = 1.0306d / 16d;

	@Override
	public void beforeRender(AltarPedestalBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		// Move block to the top of the pedestal's platform
		matrices.translate(0.5, ALTAR_TOP, 0.5);

		var world = entity.getLevel();

		if (world == null)
			return;

		var blockState = entity.getBlockState();

		if (!blockState.is(BlockInitializer.ALTAR_PEDESTAL.get()))
			return;

		// Offset in the direction it's facing
		var dir = blockState.getValue(HorizontalDirectionalBlock.FACING);
		var transDir = dir.step().mul((float) -ALTAR_OFFSET);
		matrices.translate(transDir.x, transDir.y, transDir.z);

		// Rotate
		matrices.mulPose(getAxis(dir.getClockWise()).rotationDegrees(22.5f));
	}

	public static Axis getAxis(Direction dir) {
		var mul = dir.getAxisDirection().getStep();
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
