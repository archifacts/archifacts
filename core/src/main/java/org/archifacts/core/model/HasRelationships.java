package org.archifacts.core.model;

import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Set;

public interface HasRelationships {

	Set<ArtifactRelationship> getRelationships();

	default Set<ArtifactRelationship> getRelationshipsOfType(final ArtifactRelationshipRole artifactRelationshipRole) {
		return getRelationships()
				.stream()
				.filter(relationship -> relationship.getRole().equals(artifactRelationshipRole))
				.collect(toCollection(LinkedHashSet::new));
	}

}
