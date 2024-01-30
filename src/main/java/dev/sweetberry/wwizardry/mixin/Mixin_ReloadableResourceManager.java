package dev.sweetberry.wwizardry.mixin;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.datagen.AbstractDataGenerator;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.resource.pack.ResourcePack;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
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
		if (FabricLoader.getInstance().isModLoaded("quilt_resource_loader"))
			return;
		var temp = new MultiPackResourceManager(type, packs);
		AbstractDataGenerator.reloadPack(temp);
		temp.close();
	}

	@ModifyArg(
		method = "reload",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/MultiPackResourceManager;<init>(Lnet/minecraft/resource/ResourceType;Ljava/util/List;)V"
		)
	)
	private List<ResourcePack> wwizardry$getPacks(List<ResourcePack> old) {
		if (FabricLoader.getInstance().isModLoaded("quilt_resource_loader"))
			return old;
		var packs = new ArrayList<>(old);
		packs.add(DatagenInitializer.pack);
		return packs;
	}
}
