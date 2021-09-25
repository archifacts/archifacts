package org.archifacts.core.model;

import static java.util.stream.Collectors.toCollection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public interface HasRelationships {

	Set<ArtifactRelationship> getRelationships();

	default Set<ArtifactRelationship> getRelationshipsOfRole(final ArtifactRelationshipRole role) {
		return Collections.unmodifiableSet(
				(Set<ArtifactRelationship>) getRelationships()
						.stream()
						.filter(relationship -> relationship.getRole().equals(role))
						.collect(toCollection(LinkedHashSet::new)));
	}

}
