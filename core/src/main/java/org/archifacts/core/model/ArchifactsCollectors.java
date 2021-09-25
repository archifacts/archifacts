package org.archifacts.core.model;

import static java.util.stream.Collectors.toCollection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class ArchifactsCollectors {

	private ArchifactsCollectors() {

	}

	/**
	 * Returns a {@code Collector} that accumulates the input elements into an <a href="../Set.html#unmodifiable">unmodifiable Set</a> which is baked by a {@link LinkedHashSet}. The returned Collector
	 * disallows null values and will throw {@code NullPointerException} if it is presented with a null value. If the input contains duplicate elements, an arbitrary element of the duplicates is
	 * preserved.
	 *
	 * @param <T> the type of the input elements
	 * @return a {@code Collector} that accumulates the input elements into an <a href="../Set.html#unmodifiable">unmodifiable Set</a>
	 */
	static <T> Collector<T, ?, Set<T>> toUnmodifiableLinkedSet() {
		return Collectors.collectingAndThen(toCollection(LinkedHashSet::new), Collections::unmodifiableSet);
	}
}
