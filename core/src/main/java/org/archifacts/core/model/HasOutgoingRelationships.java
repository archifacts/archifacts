package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Set;

public interface HasOutgoingRelationships {

	Set<ArtifactRelationship> getOutgoingRelationships();

	default Set<ArtifactRelationship> getOutgoingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getOutgoingRelationships()
				.stream()
				.filter(r -> r.getRole().equals(role))
				.collect(toUnmodifiableLinkedSet());
	}
}
