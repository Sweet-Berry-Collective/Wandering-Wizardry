package io.github.sweetberrycollective.wwizardry.client.render;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.client.WanderingClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public class AltarCatalyzerBlockEntityRenderer implements BlockEntityRenderer<AltarCatalyzerBlockEntity> {
	public AltarCatalyzerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(AltarCatalyzerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.isRemoved()) return;

		matrices.push();

		matrices.translate(0.5, 1.1875, 0.5);
		if (!entity.crafting) {
			matrices.translate(0, Math.sin(WanderingClient.ITEM_ROTATION * 0.25 + entity.rand) * 0.03125, 0);
		} else {
			matrices.translate(0, (float) entity.craftingTick / 25, 0);
		}
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(WanderingClient.ITEM_ROTATION));

		var lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());

		MinecraftClient.getInstance().getItemRenderer().renderItem(entity.heldItem, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);

		matrices.pop();
	}
}
