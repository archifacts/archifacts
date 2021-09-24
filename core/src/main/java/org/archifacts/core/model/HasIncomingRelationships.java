package org.archifacts.core.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface HasIncomingRelationships {

	Set<ArtifactRelationship> getIncomingRelationships();

	default Set<ArtifactRelationship> getIncomingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return getIncomingRelationships()
				.stream()
				.filter(r -> r.getRole().equals(role))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

}
