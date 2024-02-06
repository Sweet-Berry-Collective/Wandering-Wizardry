package dev.sweetberry.wwizardry.api.registry;

import net.minecraft.core.Registry;

public record RegistryEntryWatcher<T>(Registry<T> registry) {
	public void apply(RegistryCallback<T> callback) {
		if (!(registry instanceof RegistryEventHolder<?> _holder))
			return;
		var holder = (RegistryEventHolder<T>) _holder;

		// Collect to a list so any updates don't cause an infinite loop.
		registry.holders().toList().forEach(it ->
			callback.register(registry, it.key().location(), it::value)
		);

		holder.wwizardry$getEvent().listen(callback);
	}
}
