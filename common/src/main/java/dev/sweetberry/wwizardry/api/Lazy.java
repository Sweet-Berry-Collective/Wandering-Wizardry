package dev.sweetberry.wwizardry.api;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
	private T t;
	private final Supplier<T> supplier;

	private Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> Lazy<T> create(Supplier<T> supplier) {
		if (supplier instanceof Lazy<T> lazy)
			return lazy;
		return new Lazy<>(supplier);
	}

	@Override
	public T get() {
		if (t != null)
			return t;
		t = supplier.get();
		return t;
	}
}
