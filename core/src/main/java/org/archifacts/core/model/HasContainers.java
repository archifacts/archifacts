package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Set;

public interface HasContainers {

	Set<ArtifactContainer> getContainers();

	default Set<ArtifactContainer> getContainersOfType(final ArtifactContainerType artifactContainerType) {
		return getContainers()
				.stream()
				.filter(container -> container.getType().equals(artifactContainerType))
				.collect(toUnmodifiableLinkedSet());
	}

}
