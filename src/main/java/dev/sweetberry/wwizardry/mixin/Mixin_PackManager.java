package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.datagen.ModdedPackProvider;
import net.minecraft.resource.pack.PackManager;
import net.minecraft.resource.pack.PackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(PackManager.class)
public class Mixin_PackManager {
	@Shadow @Final @Mutable
	private Set<PackProvider> providers;

	@Inject(
		method = "<init>",
		at = @At("RETURN")
	)
	private void wwizardry$addPacks(PackProvider[] providers, CallbackInfo ci) {
		HashSet<PackProvider> set = new HashSet<>(this.providers);
		set.add(new ModdedPackProvider());
		this.providers = set;
	}
}
