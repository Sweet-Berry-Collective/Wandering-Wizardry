package dev.sweetberry.wwizardry.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.render.model.SnailModel;
import dev.sweetberry.wwizardry.content.entity.Snail;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnailRenderer extends MobRenderer<Snail, SnailModel> {
	public SnailRenderer(EntityRendererProvider.Context context) {
		super(context, new SnailModel(context.bakeLayer(SnailModel.LAYER_LOCATION)), 0.2f);
	}

	@Override
	public ResourceLocation getTextureLocation(Snail snail) {
		return WanderingWizardry.id(snail.getVariant().getTextureName());
	}

	@Override
	public void render(Snail snail, float $$1, float $$2, PoseStack stack, MultiBufferSource buffer, int $$5) {
		stack.pushPose();
		final float scale = 0.6f;
		if (snail.isBaby())
			stack.scale(scale, scale, scale);

		super.render(snail, $$1, $$2, stack, buffer, $$5);
		stack.popPose();
	}
}
