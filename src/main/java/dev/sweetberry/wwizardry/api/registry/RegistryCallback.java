package dev.sweetberry.wwizardry.api.registry;

import net.minecraft.util.Identifier;

// TODO: Move to liberry
@FunctionalInterface
public interface RegistryCallback<T> {
	void register(Identifier id, T item);
}
