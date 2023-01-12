package io.github.sweetberrycollective.wwizardry.client.render;

import io.github.sweetberrycollective.wwizardry.block.entity.AltarBlockEntity;
import io.github.sweetberrycollective.wwizardry.client.WanderingClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public interface AltarBlockEntityRenderer<T extends AltarBlockEntity> extends BlockEntityRenderer<T> {

	void beforeRender(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);

	default boolean shouldHover(T entity) {
		return !entity.crafting;
	}

	@Override
	default void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.isRemoved()) return;

		matrices.push();

		beforeRender(entity, tickDelta, matrices, vertexConsumers, light, overlay);

		if (shouldHover(entity)) {
			matrices.translate(0, Math.sin((WanderingClient.ITEM_ROTATION + tickDelta) / 16 + entity.rand) * 0.03125, 0);
		} else {
			matrices.translate(0, (entity.craftingTick + tickDelta) / 25, 0);
		}

		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(WanderingClient.ITEM_ROTATION + tickDelta / 2));

		MinecraftClient.getInstance().getItemRenderer().renderItem(entity.heldItem, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);

		matrices.pop();
	}
}
