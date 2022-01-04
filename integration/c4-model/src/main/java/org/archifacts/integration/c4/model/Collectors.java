package org.archifacts.integration.c4.model;

import java.util.Optional;
import java.util.stream.Collector;

public final class Collectors {
	private Collectors() {
	}

	public static <T> Collector<T, ?, Optional<T>> toOptionalSingleton() {
		return java.util.stream.Collectors.collectingAndThen(
				java.util.stream.Collectors.toList(),
				list -> {
					if (list.isEmpty()) {
						return Optional.empty();
					}
					if (list.size() > 1) {
						throw new IllegalStateException();
					}
					return Optional.of(list.get(0));
				});
	}
}
