package org.archifacts.core.model;

/**
 * The role of a relationship. Two instances are considered equal if and only if
 * they are the same instance. Two newly created types with the same names are not
 * considered equal. This is a necessary limitation to avoid ambiguity when
 * querying for elements.
 */
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

}
