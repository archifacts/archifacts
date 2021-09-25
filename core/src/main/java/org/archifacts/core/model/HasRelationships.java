package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Set;

public interface HasRelationships {

	Set<ArtifactRelationship> getRelationships();

	default Set<ArtifactRelationship> getRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getRelationships()
				.stream()
				.filter(relationship -> relationship.getRole().equals(role))
				.collect(toUnmodifiableLinkedSet());
	}

}
