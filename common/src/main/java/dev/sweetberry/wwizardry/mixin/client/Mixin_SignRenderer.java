package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.duck.Duck_SignRenderer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SignRenderer.class)
public class Mixin_SignRenderer {
	@WrapOperation(
		method = "renderSign(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/world/level/block/state/properties/WoodType;Lnet/minecraft/client/model/Model;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;getSignMaterial(Lnet/minecraft/world/level/block/state/properties/WoodType;)Lnet/minecraft/client/resources/model/Material;"
		)
	)
	private Material wwizardry$getMaterial(SignRenderer instance, WoodType woodType, Operation<Material> original) {
		var duck = (Duck_SignRenderer)instance;
		var type = duck.wwizardry$getSignType();
		if (type == null)
			return original.call(instance, woodType);
		var material = (
			instance instanceof HangingSignRenderer
				? WanderingWizardryClient.HANGING_SIGN_MATERIALS
				: WanderingWizardryClient.SIGN_MATERIALS
		).get(type);
		duck.wwizardry$setSignType(null);
		return material;
	}
}
