package org.archifacts.core.model;

import java.util.Objects;

public final class ArtifactRelationshipRole {

	public static ArtifactRelationshipRole of(final String name) {
		return new ArtifactRelationshipRole(name);
	}

	private final String name;

	private ArtifactRelationshipRole(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
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
		final ArtifactRelationshipRole other = (ArtifactRelationshipRole) obj;
		return Objects.equals(name, other.name);
	}
}
