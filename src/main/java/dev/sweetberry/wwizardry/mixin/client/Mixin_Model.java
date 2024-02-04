package dev.sweetberry.wwizardry.mixin.client;

import dev.sweetberry.wwizardry.Mod;
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
		if (!old.getNamespace().equals(Mod.MODID))
			return;
		if (!old.getPath().startsWith("textures/entity"))
			return;

		cir.setReturnValue(
			renderType(
				Mod.id(
					old.getPath()
						.replace(
							"textures",
							"textures/wwizardry_animated"
						)
				)
			)
		);
	}
}
