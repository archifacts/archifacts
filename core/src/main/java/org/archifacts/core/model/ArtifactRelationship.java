package org.archifacts.core.model;

import java.util.Objects;

public final class ArtifactRelationship {

	private final Artifact source;
	private final Artifact target;
	private final ArtifactRelationshipRole role;

	ArtifactRelationship(final Artifact source, final Artifact target, final ArtifactRelationshipRole role) {
		this.source = source;
		this.target = target;
		this.role = role;
	}

	public Artifact getSource() {
		return source;
	}

	public Artifact getTarget() {
		return target;
	}

	public ArtifactRelationshipRole getRole() {
		return role;
	}

	@Override
	public String toString() {
		return source + " " + role + " " + target;
	}

	@Override
	public int hashCode() {
		return Objects.hash(role, source, target);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ArtifactRelationship other = (ArtifactRelationship) obj;
		return Objects.equals(role, other.role) && Objects.equals(source, other.source) && Objects.equals(target, other.target);
	}

}
