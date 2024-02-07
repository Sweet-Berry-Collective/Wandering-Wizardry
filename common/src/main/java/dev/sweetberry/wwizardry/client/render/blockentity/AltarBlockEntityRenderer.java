package dev.sweetberry.wwizardry.client.render.blockentity;

import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.block.entity.AltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public interface AltarBlockEntityRenderer<T extends AltarBlockEntity> extends BlockEntityRenderer<T> {
	double offsetToTop = 3d / 16d;
	double bobOffset = 0.5d / 16d;

	BlockEntityRendererProvider.Context context();

	void beforeRender(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);

	default boolean shouldHover(T entity) {
		return !entity.crafting;
	}

	@Override
	default void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		if (entity.isRemoved()) return;

		matrices.pushPose();

		beforeRender(entity, tickDelta, matrices, vertexConsumers, light, overlay);
		// Get items to be to slightly above the platform
		matrices.translate(0, offsetToTop + bobOffset, 0);

		var shouldHover = shouldHover(entity);

		if (!shouldHover && entity.recipeRemainder.isEmpty())
			matrices.translate(0, entity.clampLerpTime(0, tickDelta, 0, 4), 0);

		if (entity.heldItem.is(Items.END_CRYSTAL)) {
			matrices.scale(0.5f, 0.5f, 0.5f);

			drawEndCrystal(entity, tickDelta, matrices, vertexConsumers, light);
		} else {
			if (shouldHover)
				matrices.translate(0, Math.sin((WanderingWizardryClient.tickCounter + tickDelta) / 16 + entity.rand) * bobOffset, 0);
			matrices.mulPose(Axis.YN.rotationDegrees((WanderingWizardryClient.tickCounter + tickDelta) / 2));

			drawItem(entity, matrices, vertexConsumers, light);
		}

		matrices.popPose();
	}

	default void drawEndCrystal(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		var endCrystalEntity = entity.getOrCreateEndCrystalEntity();

		if (endCrystalEntity == null)
			return;

		endCrystalEntity.time = WanderingWizardryClient.tickCounter;

		context().getEntityRenderer().getRenderer(endCrystalEntity).render(endCrystalEntity, 0, tickDelta, matrices, vertexConsumers, light);
	}

	default void drawItem(T entity, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		var itemRenderer = Minecraft.getInstance().getItemRenderer();
		var model = itemRenderer.getModel(entity.heldItem, entity.getLevel(), null, 0);
		itemRenderer.render(entity.heldItem, ItemDisplayContext.GROUND, true, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, model);
	}
}
