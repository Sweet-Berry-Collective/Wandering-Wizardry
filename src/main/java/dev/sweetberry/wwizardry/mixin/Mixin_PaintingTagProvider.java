package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.data.DataPackOutput;
import net.minecraft.data.server.tag.AbstractTagProvider;
import net.minecraft.data.server.tag.PaintingTagProvider;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.HolderLookup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.PaintingTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(PaintingTagProvider.class)
public abstract class Mixin_PaintingTagProvider extends AbstractTagProvider<PaintingVariant> {
	protected Mixin_PaintingTagProvider(DataPackOutput output, RegistryKey<? extends Registry<PaintingVariant>> key, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, key, lookupProvider);
		throw new IllegalCallerException();
	}

	@Inject(
		method = "configure",
		at = @At("RETURN")
	)
	private void addAltarPainting(HolderLookup.Provider lookup, CallbackInfo ci) {
		getOrCreateTagBuilder(PaintingTags.PLACEABLE)
			.add(WanderingMod.ALTAR_PAINTING);
	}
}
