package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.datagen.AbstractDataGenerator;
import net.minecraft.resource.*;
import net.minecraft.resource.pack.ResourcePack;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class Mixin_ReloadableResourceManager {
	@Shadow
	private AutoCloseableResourceManager resources;

	@Shadow
	@Final
	private ResourceType type;

	@Inject(
		method = "reload",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/MultiPackResourceManager;<init>(Lnet/minecraft/resource/ResourceType;Ljava/util/List;)V",
			shift = At.Shift.BEFORE
		)
	)
	private void wwizardry$reloadGeneratedData(
		Executor prepareExecutor,
		Executor applyExecutor,
		CompletableFuture<Unit> initialStage,
		List<ResourcePack> packs,
		CallbackInfoReturnable<ResourceReload> cir
	) {
		var temp = new MultiPackResourceManager(type, packs);
		AbstractDataGenerator.reloadPack(temp);
		temp.close();
	}
}
