package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface HasOutgoingRelationships {

	Set<ArtifactRelationship> getOutgoingRelationships();

	default Set<ArtifactRelationship> getOutgoingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getOutgoingRelationshipsOfRoles(role);
	}

	default Set<ArtifactRelationship> getOutgoingRelationshipsOfRoles(final ArtifactRelationshipRole... roles) {
		final List<ArtifactRelationshipRole> roleList = Arrays.asList(roles);
		return getOutgoingRelationships()
				.stream()
				.filter(r -> roleList.contains(r.getRole()))
				.collect(toUnmodifiableLinkedSet());
	}
}
