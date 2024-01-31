package dev.sweetberry.wwizardry.api.registry;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegistryCallback<T> {
	void register(ResourceLocation id, T item);
}
