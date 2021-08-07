package org.archifacts.core.model;

import java.util.Objects;

public final class ArtifactContainerType implements Named {

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
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ArtifactContainerType other = (ArtifactContainerType) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}

}
