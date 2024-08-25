package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.content.block.redstone.CopperLensBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconRenderer.class)
public class Mixin_BeaconRenderer {
	@Unique
	private boolean wwizardry$isFocusedBeam;

	@Inject(
		method = "render(Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
		at = @At("HEAD")
	)
	private void setFocus(BeaconBlockEntity entity, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5, CallbackInfo ci) {
		wwizardry$isFocusedBeam = entity.getLevel().getBlockState(entity.getBlockPos().above()).getBlock() instanceof CopperLensBlock;
	}


	@WrapOperation(
		method = "render(Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BeaconRenderer;renderBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FJIII)V")
	)
	private void focusBeam(PoseStack stack, MultiBufferSource buffer, float p_112188_, long p_112190_, int p_112191_, int p_112192_, int p_350457_, Operation<Void> original) {
		stack.pushPose();
		if (wwizardry$isFocusedBeam) {
			final float middle = (2/3f)-(1/2f);
			stack.scale(0.75f, 1, 0.75f);
			stack.translate(middle, 0, middle);
		}
		original.call(stack, buffer, p_112188_, p_112190_, p_112191_, p_112192_, p_350457_);
		stack.popPose();
	}
}
