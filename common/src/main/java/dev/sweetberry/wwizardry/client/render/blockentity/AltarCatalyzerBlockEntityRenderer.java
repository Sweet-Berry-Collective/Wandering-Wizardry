package dev.sweetberry.wwizardry.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public record AltarCatalyzerBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements AltarBlockEntityRenderer<AltarCatalyzerBlockEntity> {
	private static final double ALTAR_TOP = 16.5d / 16d;

	@Override
	public boolean shouldHover(AltarCatalyzerBlockEntity entity) {
		return AltarBlockEntityRenderer.super.shouldHover(entity) || !entity.recipeRemainder.isEmpty();
	}

	@Override
	public void beforeRender(AltarCatalyzerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		var pos = entity.getBlockPos();
		if (entity.shouldUpdateClient)
			Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

		// Set to top of altar
		matrices.translate(0.5, ALTAR_TOP, 0.5);
	}
}
