package dev.sweetberry.wwizardry.api.registry;

import dev.sweetberry.wwizardry.api.event.Event;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

// TODO: PastAwareEvent? I don't know how it'd work.
public final class RegistryContext<T> extends Event<RegistryCallback<T>> {
	private final Registry<T> registry;
	private List<RegistryObject<T>> pastRegistrations = new ArrayList<>();

	public RegistryContext(Registry<T> registry) {
		super(listeners -> ((registry1, id, item) -> {
			for (var listener : listeners)
				listener.register(registry1, id, item);
		}));
		this.registry = registry;
	}

	public T register(ResourceLocation id, T object) {
		invoker().register(registry, id, object);
		pastRegistrations.add(new RegistryObject<>(id, object));
		return object;
	}

	@Override
	public void listen(RegistryCallback<T> listener) {
		super.listen(listener);
		for (var registration : pastRegistrations)
			listener.register(registry, registration.id(), registration.object());
	}

	private record RegistryObject<T>(ResourceLocation id, T object) {}
}
