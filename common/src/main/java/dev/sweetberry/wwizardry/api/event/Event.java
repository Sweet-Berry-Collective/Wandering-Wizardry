package dev.sweetberry.wwizardry.api.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a set of lambdas that are all triggered when the event is dispatched
 * */
public class Event<T> {
	private final List<T> listeners = new ArrayList<>();
	private final Invoker<T> invoker;

	public Event(Invoker<T> invoker) {
		this.invoker = invoker;
	}

	/**
	 * Get the invoker
	 * */
	public T invoker() {
		return invoker.onInvoke(listeners);
	}

	/**
	 * Listen to the event
	 * */
	public void listen(T listener) {
		listeners.add(listener);
	}

	/**
	 * Called to get a method that calls all held listeners
	 * */
	@FunctionalInterface
	public interface Invoker<T> {
		T onInvoke(List<T> listeners);
	}
}
