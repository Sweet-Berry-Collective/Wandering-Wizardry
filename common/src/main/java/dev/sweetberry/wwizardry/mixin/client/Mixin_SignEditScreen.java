package dev.sweetberry.wwizardry.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignEditScreen.class)
public class Mixin_SignEditScreen {
	@Unique
	private static ResourceLocation wwizardry$type = null;

	@Inject(
		method = "renderSignBackground",
		at = @At("HEAD")
	)
	private void wwizardry$captureWoodType(GuiGraphics guiGraphics, BlockState state, CallbackInfo ci) {
		var block = state.getBlock();
		if (block instanceof ModdedSignBlock moddedSign) {
			wwizardry$type = moddedSign.getSignId();
			return;
		}
		wwizardry$type = null;
	}

	@WrapOperation(
		method = "renderSignBackground",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/Sheets;getSignMaterial(Lnet/minecraft/world/level/block/state/properties/WoodType;)Lnet/minecraft/client/resources/model/Material;"
		)
	)
	private Material wwizardry$getSignMaterial(WoodType woodType, Operation<Material> original) {
		if (wwizardry$type == null)
			return original.call(woodType);
		return WanderingWizardryClient.SIGN_MATERIALS.get(wwizardry$type);
	}
}
