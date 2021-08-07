package org.archifacts.core.model;

import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Set;

public interface HasContainers {

	Set<ArtifactContainer> getContainers();

	default Set<ArtifactContainer> getContainersOfType(final ArtifactContainerType artifactContainerType) {
		return getContainers()
				.stream()
				.filter(container -> container.getType().equals(artifactContainerType))
				.collect(toCollection(LinkedHashSet::new));
	}

}
