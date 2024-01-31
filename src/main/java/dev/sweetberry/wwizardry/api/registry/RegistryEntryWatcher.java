package dev.sweetberry.wwizardry.api.registry;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;

public record RegistryEntryWatcher<T>(Registry<T> registry) {
	public void apply(RegistryCallback<T> callback) {
		if (!(this.registry instanceof WritableRegistry<T>))
			return;

		// Collect to a list so any updates don't cause an infinite loop.
		registry.holders().toList().forEach(it ->
			callback.register(it.key().location(), it.value())
		);

		RegistryEntryAddedCallback.event(registry).register((rawId, id, object) ->
			callback.register(id, object)
		);
	}
}
