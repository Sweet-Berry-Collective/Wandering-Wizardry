package dev.sweetberry.wwizardry.api.registry;

import dev.sweetberry.wwizardry.api.event.Event;

public interface RegistryEventHolder<T> {
	Event<RegistryCallback<T>> wwizardry$getEvent();
}
