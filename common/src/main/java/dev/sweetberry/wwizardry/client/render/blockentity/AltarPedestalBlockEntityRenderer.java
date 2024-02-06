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
	@Override
	public void beforeRender(AltarPedestalBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		// I need to document my fucking magic numbers wtf
		matrices.translate(0.5, 0.9509, 0.5);

		var world = entity.getLevel();

		if (world == null)
			return;

		var blockState = world.getBlockState(entity.getBlockPos());

		if (!blockState.is(BlockInitializer.ALTAR_PEDESTAL.get()))
			return;

		var dir = blockState.getValue(HorizontalDirectionalBlock.FACING);

		// Why?
		var transDir = dir.step().mul(-0.09335f);
		matrices.translate(transDir.x, transDir.y, transDir.z);

		matrices.mulPose(getAxis(dir.getClockWise()).rotationDegrees(22.5f));

		// Huh?
		transDir = dir.step().mul(0.046875f);
		matrices.translate(transDir.x, transDir.y + 0.25, transDir.z);
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
