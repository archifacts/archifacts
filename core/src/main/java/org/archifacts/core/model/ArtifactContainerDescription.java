package org.archifacts.core.model;

import java.util.Objects;

final class ArtifactContainerDescription {
	private final ArtifactContainerType type;
	private final String name;

	ArtifactContainerDescription(final ArtifactContainerType type, final String name) {
		this.type = type;
		this.name = name;
	}

	ArtifactContainerType getType() {
		return type;
	}

	String getName() {
		return name;
	}

	static ArtifactContainerDescription fromContainer(final ArtifactContainer container) {
		return new ArtifactContainerDescription(container.getType(), container.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ArtifactContainerDescription other = (ArtifactContainerDescription) obj;
		return Objects.equals(name, other.name) && Objects.equals(type, other.type);
	}

}
