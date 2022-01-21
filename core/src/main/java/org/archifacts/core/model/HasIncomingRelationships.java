package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface HasIncomingRelationships {

	Set<ArtifactRelationship> getIncomingRelationships();

	default Set<ArtifactRelationship> getIncomingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getIncomingRelationshipsOfRoles(role);
	}

	default Set<ArtifactRelationship> getIncomingRelationshipsOfRoles(final ArtifactRelationshipRole... roles) {
		final List<ArtifactRelationshipRole> roleList = Arrays.asList(roles);
		return getIncomingRelationships()
				.stream()
				.filter(r -> roleList.contains(r.getRole()))
				.collect(toUnmodifiableLinkedSet());
	}

}
