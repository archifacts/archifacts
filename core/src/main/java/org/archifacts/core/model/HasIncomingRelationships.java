package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Set;

public interface HasIncomingRelationships {

	Set<ArtifactRelationship> getIncomingRelationships();

	default Set<ArtifactRelationship> getIncomingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getIncomingRelationships()
				.stream()
				.filter(r -> r.getRole().equals(role))
				.collect(toUnmodifiableLinkedSet());
	}

}
