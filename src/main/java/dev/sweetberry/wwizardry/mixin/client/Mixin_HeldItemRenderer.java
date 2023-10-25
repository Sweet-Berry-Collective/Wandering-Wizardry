package dev.sweetberry.wwizardry.mixin.client;

import dev.sweetberry.wwizardry.client.WanderingClient;
import dev.sweetberry.wwizardry.item.SoulMirrorItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class Mixin_HeldItemRenderer {
	@Inject(
		method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			ordinal = 1
		)
	)
	private void wwizardry$renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (item.getItem() instanceof SoulMirrorItem && player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
			if (WanderingClient.useItemTick == -1)
				WanderingClient.useItemTick = WanderingClient.tickCounter;
			Arm arm = (hand == Hand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();

			int i = arm == Arm.RIGHT ? 1 : -1;

			matrices.translate(
				(float)i * 0.41f,
				-0.41f, //y
				-0.7f
			);

			int timeSinceUse = WanderingClient.tickCounter - WanderingClient.useItemTick;

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
		} else {
			WanderingClient.useItemTick = -1;
		}
	}
}
