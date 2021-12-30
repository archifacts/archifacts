package org.archifacts.core.model;

import java.util.Comparator;

/**
 * The type of an artifact container block. Two instances are considered equal if and only if they are the same instance. Two newly created types with the same names are not considered equal. This is
 * a necessary limitation to avoid ambiguity when querying for elements.
 */
public final class ArtifactContainerType implements Named, Comparable<ArtifactContainerType> {

	public static ArtifactContainerType of(final String name) {
		return new ArtifactContainerType(name);
	}

	private final String name;

	private ArtifactContainerType(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(ArtifactContainerType o) {
		 return Comparator.comparing(ArtifactContainerType::getName)
	              .compare(this, o);
	}

}
