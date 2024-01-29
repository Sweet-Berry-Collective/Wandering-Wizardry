package dev.sweetberry.wwizardry.api.registry;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;

// TODO: Move to liberry
public record RegistryEntryWatcher<T>(Registry<T> registry) {
	public void apply(RegistryCallback<T> callback) {
		if (!(this.registry instanceof MutableRegistry<T>))
			return;

		// Collect to a list so any updates don't cause an infinite loop.
		registry.holders().toList().forEach(it ->
			callback.register(it.getRegistryKey().getValue(), it.value())
		);

		RegistryEntryAddedCallback.event(registry).register((rawId, id, object) ->
			callback.register(id, object)
		);
	}
}
