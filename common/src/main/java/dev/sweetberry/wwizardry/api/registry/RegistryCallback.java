package dev.sweetberry.wwizardry.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegistryCallback<T> {
	void register(Registry<T> registry, ResourceLocation id, T item);
}
