package io.github.sweetberrycollective.wwizardry.client.render;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.client.WanderingClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public class AltarCatalyzerBlockEntityRenderer implements AltarBlockEntityRenderer<AltarCatalyzerBlockEntity> {

	public AltarCatalyzerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public boolean shouldHover(AltarCatalyzerBlockEntity entity) {
		return AltarBlockEntityRenderer.super.shouldHover(entity) || entity.keepCatalyst;
	}

	@Override
	public void beforeRender(AltarCatalyzerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var pos = entity.getPos();
		if (entity.shouldUpdateClient)
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

		matrices.translate(0.5, 1.1875, 0.5);
	}
}
