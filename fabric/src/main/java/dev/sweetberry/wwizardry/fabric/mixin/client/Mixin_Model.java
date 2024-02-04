package dev.sweetberry.wwizardry.fabric.mixin.client;

import dev.sweetberry.wwizardry.client.content.AnimatedTextureMap;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A bit of a cheat to redirect all Wandering Wizardry entity textures to the animated folder.
 * */
@Mixin(Model.class)
public abstract class Mixin_Model {
	@Shadow
	public abstract RenderType renderType(ResourceLocation resourceLocation);

	@Inject(
		method = "renderType",
		at = @At("RETURN"),
		cancellable = true
	)
	private void wwizardry$checkEntityTextures(ResourceLocation old, CallbackInfoReturnable<RenderType> cir) {
		var _new = old.withPath(old.getPath().replaceFirst("^textures", "textures/wwizardry_animated"));
		if (AnimatedTextureMap.ANIMATED_TEXTURES.contains(_new))
			cir.setReturnValue(renderType(_new));
	}
}
