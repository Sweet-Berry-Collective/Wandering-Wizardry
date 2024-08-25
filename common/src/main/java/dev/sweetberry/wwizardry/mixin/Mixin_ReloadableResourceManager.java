package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
public class Mixin_ReloadableResourceManager {
	@Shadow
	@Final
	private PackType type;

	@Inject(
		method = "createReload",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/packs/resources/MultiPackResourceManager;<init>(Lnet/minecraft/server/packs/PackType;Ljava/util/List;)V",
			shift = At.Shift.BEFORE
		)
	)
	private void wwizardry$reloadGeneratedData(
		Executor prepareExecutor,
		Executor applyExecutor,
		CompletableFuture<Unit> initialStage,
		List<PackResources> packs,
		CallbackInfoReturnable<ReloadInstance> cir
	) {
		var temp = new MultiPackResourceManager(type, packs);
		try {
			DatagenInitializer.reloadPack(temp);
		} catch (IOException ignored) {}
		temp.close();
	}

	@ModifyArg(
		method = "createReload",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/packs/resources/MultiPackResourceManager;<init>(Lnet/minecraft/server/packs/PackType;Ljava/util/List;)V"
		)
	)
	private List<PackResources> wwizardry$getPacks(List<PackResources> old) {
		var packs = new ArrayList<>(old);
		packs.add(DatagenInitializer.pack);
		return packs;
	}
}
