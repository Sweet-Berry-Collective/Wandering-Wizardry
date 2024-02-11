package dev.sweetberry.wwizardry.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.render.model.AltarCatalyzerModel;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.altar.AltarBlock;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class AltarCatalyzerBlockEntityRenderer implements AltarBlockEntityRenderer<AltarCatalyzerBlockEntity> {
	private static final double ALTAR_TOP = 16.5d / 16d;
	private BlockEntityRendererProvider.Context context;
	private final AltarCatalyzerModel model;

	public AltarCatalyzerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
		model = new AltarCatalyzerModel(context.bakeLayer(AltarCatalyzerModel.LAYER_LOCATION));
	}

	@Override
	public boolean shouldHover(AltarCatalyzerBlockEntity entity) {
		return AltarBlockEntityRenderer.super.shouldHover(entity) || !entity.recipeRemainder.isEmpty();
	}

	@Override
	public BlockEntityRendererProvider.Context context() {
		return context;
	}

	@Override
	public void beforeRender(AltarCatalyzerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		var pos = entity.getBlockPos();
		if (entity.shouldUpdateClient)
			Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

		// Set to top of altar
		matrices.translate(0.5, ALTAR_TOP, 0.5);
	}

	@Override
	public void render(AltarCatalyzerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		AltarBlockEntityRenderer.super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
		if (!BlockInitializer.ALTAR_CATALYZER.get().isComplete(entity.getLevel(), entity.getBlockState(), entity.getBlockPos()))
			return;
		matrices.pushPose();
		matrices.translate(0.5f, -0.25, 0.5f);
		var buf = vertexConsumers.getBuffer(RenderType.entityTranslucentEmissive(AltarCatalyzerModel.TEXTURE_ID));
		model.ticks = WanderingWizardryClient.tickCounter + tickDelta;
		model.crafting = entity.crafting;
		model.timeToCraft = entity.crafting ? entity.getCraftingTime(tickDelta) : 0;
		model.renderToBuffer(matrices, buf, light, overlay, 1, 1, 1, entity.clampLerpTime(0, tickDelta, 0.125f, 1));
		matrices.popPose();
	}
}
