package dev.sweetberry.wwizardry.client.render;

import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public record AltarCatalyzerBlockEntityRenderer(BlockEntityRendererFactory.Context context) implements AltarBlockEntityRenderer<AltarCatalyzerBlockEntity> {
	@Override
	public boolean shouldHover(AltarCatalyzerBlockEntity entity) {
		return AltarBlockEntityRenderer.super.shouldHover(entity) || !entity.recipeRemainder.isEmpty();
	}

	@Override
	public void beforeRender(AltarCatalyzerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var pos = entity.getPos();
		if (entity.shouldUpdateClient)
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

		matrices.translate(0.5, 1.1875, 0.5);
	}
}
