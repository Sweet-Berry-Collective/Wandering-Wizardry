package dev.sweetberry.wwizardry.mixin;

import com.mojang.serialization.Lifecycle;
import dev.sweetberry.wwizardry.api.event.Event;
import dev.sweetberry.wwizardry.api.registry.RegistryCallback;
import dev.sweetberry.wwizardry.api.registry.RegistryEventHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class Mixin_MappedRegistry<T> implements RegistryEventHolder<T> {
	@Unique
	private final Event<RegistryCallback<T>> wwizardry$EVENT = new Event<>(listeners -> (registry, id, object) -> {
        for (var listener : listeners) {
			listener.register(registry, id, object);
		}
    });

	@Override
	public Event<RegistryCallback<T>> wwizardry$getEvent() {
		return wwizardry$EVENT;
	}

	@Inject(
		method = "registerMapping",
		at = @At("RETURN")
	)
	private void wwizardry$triggerEvent(int i, ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir) {
		wwizardry$EVENT.invoker().register((Registry<T>)(Object)this, key.location(), value);
	}
}
