package org.archifacts.core.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface HasOutgoingRelationships {

	Set<ArtifactRelationship> getOutgoingRelationships();

	default Set<ArtifactRelationship> getOutgoingRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return Collections.unmodifiableSet(
				(Set<ArtifactRelationship>)getOutgoingRelationships()
				.stream()
				.filter(r -> r.getRole().equals(role))
				.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
}
