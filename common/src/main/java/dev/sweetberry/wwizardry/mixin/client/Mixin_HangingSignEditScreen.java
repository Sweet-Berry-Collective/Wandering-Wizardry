package dev.sweetberry.wwizardry.mixin.client;

import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingSignEditScreen.class)
public class Mixin_HangingSignEditScreen {
	@Shadow
	@Final
	@Mutable
	private ResourceLocation texture;

	@Inject(
		method = "<init>",
		at = @At("RETURN")
	)
	private void wwizardry$captureSignType(SignBlockEntity signEntity, boolean $$1, boolean $$2, CallbackInfo ci) {
		var block = signEntity.getBlockState().getBlock();
		if (block instanceof ModdedSignBlock moddedSign)
			texture = moddedSign.getSignId().withPrefix("textures/gui/hanging_signs/").withSuffix(".png");
	}
}
