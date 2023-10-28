package dev.sweetberry.wwizardry.client.render;

import dev.sweetberry.wwizardry.block.entity.AltarBlockEntity;
import dev.sweetberry.wwizardry.client.WanderingClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Axis;

public interface AltarBlockEntityRenderer<T extends AltarBlockEntity> extends BlockEntityRenderer<T> {
	BlockEntityRendererFactory.Context context();

	void beforeRender(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);

	default boolean shouldHover(T entity) {
		return !entity.crafting;
	}

	@Override
	default void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.isRemoved()) return;

		matrices.push();

		beforeRender(entity, tickDelta, matrices, vertexConsumers, light, overlay);

		var shouldHover = shouldHover(entity);

		if (!shouldHover && entity.recipeRemainder.isEmpty())
			matrices.translate(0, (entity.craftingTick + tickDelta) / 25, 0);

		if (entity.heldItem.isOf(Items.END_CRYSTAL)) {
			matrices.scale(0.5f, 0.5f, 0.5f);

			drawEndCrystal(entity, tickDelta, matrices, vertexConsumers, light);
		} else {
			if (shouldHover)
				matrices.translate(0, Math.sin((WanderingClient.tickCounter + tickDelta) / 16 + entity.rand) * 0.03125, 0);
			matrices.multiply(Axis.Y_NEGATIVE.rotationDegrees((WanderingClient.tickCounter + tickDelta) / 2));

			drawItem(entity, matrices, vertexConsumers, light);
		}

		matrices.pop();
	}

	default void drawEndCrystal(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		var endCrystalEntity = entity.getOrCreateEndCrystalEntity();

		if (endCrystalEntity == null)
			return;

		endCrystalEntity.endCrystalAge = WanderingClient.tickCounter;

		context().getEntityRendererDispatcher().getRenderer(endCrystalEntity).render(endCrystalEntity, 0, tickDelta, matrices, vertexConsumers, light);
	}

	default void drawItem(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		var model = itemRenderer.getHeldItemModel(entity.heldItem, entity.getWorld(), null, 0);
		itemRenderer.renderItem(entity.heldItem, ModelTransformationMode.GROUND, true, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
	}
}
