package dev.sweetberry.wwizardry.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public record AltarCatalyzerBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements AltarBlockEntityRenderer<AltarCatalyzerBlockEntity> {
	@Override
	public boolean shouldHover(AltarCatalyzerBlockEntity entity) {
		return AltarBlockEntityRenderer.super.shouldHover(entity) || !entity.recipeRemainder.isEmpty();
	}

	@Override
	public void beforeRender(AltarCatalyzerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		var pos = entity.getBlockPos();
		if (entity.shouldUpdateClient)
			Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

		matrices.translate(0.5, 1.1875, 0.5);
	}
}
