package dev.sweetberry.wwizardry.api.event;

import java.util.ArrayList;
import java.util.List;

public class Event<T> {
	private final List<T> listeners = new ArrayList<>();
	private final Invoker<T> invoker;

	public Event(Invoker<T> invoker) {
		this.invoker = invoker;
	}

	public T invoker() {
		return invoker.onInvoke(listeners);
	}

	public void listen(T listener) {
		listeners.add(listener);
	}

	@FunctionalInterface
	public interface Invoker<T> {
		T onInvoke(List<T> listeners);
	}
}
