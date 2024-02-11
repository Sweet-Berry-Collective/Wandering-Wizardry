package dev.sweetberry.wwizardry.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class Mixin_ItemInHandRenderer {
	@Inject(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			ordinal = 1,
			shift = At.Shift.BEFORE
		)
	)
	private void wwizardry$renderFirstPersonItem(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		if (player.getUsedItemHand() != hand)
			return;
		if (!(
			item.getItem() instanceof SoulMirrorItem
			&& player.isUsingItem()
		)) {
			WanderingWizardryClient.useItemTick = -1;
			return;
		}

		if (WanderingWizardryClient.useItemTick == -1)
			WanderingWizardryClient.useItemTick = WanderingWizardryClient.tickCounter;

		HumanoidArm arm = (hand == InteractionHand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();

		int i = arm == HumanoidArm.RIGHT ? 1 : -1;

		matrices.translate(
			(float)i * 0.41f,
			-0.41f, //y
			-0.7f
		);

		int timeSinceUse = WanderingWizardryClient.tickCounter - WanderingWizardryClient.useItemTick;

		float time = (float)timeSinceUse + tickDelta;

		final int chargeTime = 30;
		final int wobbleStart = 15;
		final float addedWidth = 0.25f;
		final float finalWidth = 1+addedWidth;
		final float wobbleRate = 0.4f;
		final float wobbleDepth = 0.02f;

		float stretch;

		if (time < chargeTime)
			stretch = ((time/chargeTime)*addedWidth)+1;
		else
			stretch = finalWidth;

		float wobble = 0;
		float wobbleTime = time-chargeTime;

		if (time > wobbleStart)
			wobble = (float)Math.sin(wobbleTime*wobbleRate)*wobbleDepth;

		matrices.translate(wobble, 0, 0);

		matrices.scale(1, 1, stretch);
	}
}
