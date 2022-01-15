package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface HasContainers {

	Set<ArtifactContainer> getContainers();

	default Set<ArtifactContainer> getContainersOfType(final ArtifactContainerType artifactContainerType) {
		return getContainersOfTypes(artifactContainerType);
	}

	default Set<ArtifactContainer> getContainersOfTypes(final ArtifactContainerType... artifactContainerTypes) {
		final List<ArtifactContainerType> artifactContainerTypeList = Arrays.asList(artifactContainerTypes);
		return getContainers()
				.stream()
				.filter(container -> artifactContainerTypeList.contains(container.getType()))
				.collect(toUnmodifiableLinkedSet());
	}

}
