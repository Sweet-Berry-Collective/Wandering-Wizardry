package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
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
public class Mixin_BoatRenderer {
	@Unique
	private final Map<ResourceLocation, Pair<ResourceLocation, ListModel<Boat>>> wwizardry$models = new HashMap<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void wwizardry$buildCustomBoatModels(EntityRendererProvider.Context context, boolean chest, CallbackInfo ci) {
		for (var boat : BoatComponent.BOATS.keySet()) {
			var modelRoot = context.bakeLayer(WanderingWizardryClient.getBoatLayerLocation(boat, chest));
			var model = chest ? new ChestBoatModel(modelRoot) : new BoatModel(modelRoot);
			wwizardry$models.put(boat, Pair.of(WanderingWizardryClient.getBoatTextureLocation(boat, chest), model));
		}
	}

	@WrapOperation(
		method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private <K, V> V wwizardry$getBoat(Map<K, V> instance, K key, Operation<V> original, Boat boat) {
		var type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, boat).type;
		if (type != null)
			return (V) wwizardry$models.get(type);
		return instance.get(key);
	}
}
