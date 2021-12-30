package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface HasRelationships {

	Set<ArtifactRelationship> getRelationships();

	default Set<ArtifactRelationship> getRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getRelationshipsOfRoles(role);
	}
	
	default Set<ArtifactRelationship> getRelationshipsOfRoles(final ArtifactRelationshipRole... roles) {
		final List<ArtifactRelationshipRole> roleList = Arrays.asList(roles);
		return getRelationships()
				.stream()
				.filter(r -> roleList.contains(r.getRole()))
				.collect(toUnmodifiableLinkedSet());
	}

}
