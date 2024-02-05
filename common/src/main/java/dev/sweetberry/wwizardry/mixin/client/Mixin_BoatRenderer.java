package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BoatRenderer.class)
public abstract class Mixin_BoatRenderer {
	@Unique
	private final Map<ResourceLocation, Pair<ResourceLocation, ListModel<Boat>>> wwizardry$models = new HashMap<>();

	@Unique
	private ResourceLocation wwizardry$type = null;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void wwizardry$buildCustomBoatModels(EntityRendererProvider.Context context, boolean chest, CallbackInfo ci) {
		for (var boat : BoatComponent.BOATS.keySet()) {
			var modelRoot = context.bakeLayer(WanderingWizardryClient.getBoatLayerLocation(boat, chest));
			var model = chest ? new ChestBoatModel(modelRoot) : new BoatModel(modelRoot);
			wwizardry$models.put(boat, Pair.of(WanderingWizardryClient.getBoatTextureLocation(boat, chest), model));
		}
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void wwizardry$captureWoodType(Boat boat, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		wwizardry$type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, boat).type;
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "java/util/Map.get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object wwizardry$useCustomBoatModel(Map<Object, Object> instance, Object key, Operation<Object> original) {
		if (wwizardry$type == null)
			return original.call(instance, key);
		var model = wwizardry$models.get(wwizardry$type);
		wwizardry$type = null;
		return model;
	}
}
