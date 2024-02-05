package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import dev.sweetberry.wwizardry.duck.Duck_SignRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin({SignRenderer.class, HangingSignRenderer.class})
public class Mixin_SignRenderer_HangingSignRenderer implements Duck_SignRenderer {
	@Unique
	private static final Map<ResourceLocation, Object> wwizardry$models = new HashMap<>();

	@Unique
	private static ResourceLocation wwizardry$type = null;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void wwizardry$buildCustomSignModels(BlockEntityRendererProvider.Context context, CallbackInfo ci) {
		var hanging = ((Object)this) instanceof HangingSignRenderer;
		for (var sign : ModdedSignBlock.SIGNS) {
			wwizardry$models.put(sign, hanging
				? new HangingSignRenderer.HangingSignModel(
					context.bakeLayer(
						WanderingWizardryClient.getSignLayerLocation(
							sign,
							true
						)
					)
				)
				: new SignRenderer.SignModel(
					context.bakeLayer(
						WanderingWizardryClient.getSignLayerLocation(
							sign,
							false
						)
					)
				)
			);
		}
	}

	@Inject(
		method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
		at = @At("HEAD")
	)
	private void wwizardry$captureWoodType(SignBlockEntity signEntity, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5, CallbackInfo ci) {
		var block = signEntity.getBlockState().getBlock();
		if (block instanceof ModdedSignBlock moddedSign) {
			wwizardry$type = moddedSign.getSignId();
			return;
		}
		wwizardry$type = null;
	}

	@WrapOperation(
		method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
		at = @At(
			value = "INVOKE",
			target = "java/util/Map.get(Ljava/lang/Object;)Ljava/lang/Object;"
		)
	)
	private Object wwizardry$useCustomSignModel(Map<Object, Object> instance, Object key, Operation<Object> original) {
		if (wwizardry$type == null)
			return original.call(instance, key);
        return wwizardry$models.get(wwizardry$type);
	}

	@Override
	public ResourceLocation wwizardry$getSignType() {
		return wwizardry$type;
	}

	@Override
	public void wwizardry$setSignType(ResourceLocation id) {
		wwizardry$type = id;
	}
}
