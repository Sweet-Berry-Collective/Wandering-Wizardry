package dev.sweetberry.wwizardry.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@FunctionalInterface
public interface RegistryCallback<T> {
	void register(Registry<T> registry, ResourceLocation id, Supplier<T> item);
}
