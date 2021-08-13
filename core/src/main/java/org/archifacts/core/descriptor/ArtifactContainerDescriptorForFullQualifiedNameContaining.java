package org.archifacts.core.descriptor;

import java.util.Objects;
import java.util.Optional;

import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;

final class ArtifactContainerDescriptorForFullQualifiedNameContaining implements ArtifactContainerDescriptor {

	private final ArtifactContainerType artifactContainerType;
	private final String sequence;
	private final String containerName;

	ArtifactContainerDescriptorForFullQualifiedNameContaining(final ArtifactContainerType artifactContainerType, final String sequence, final String containerName) {
		this.artifactContainerType = Objects.requireNonNull(artifactContainerType, "The artifact container type must not be null.");
		this.sequence = Objects.requireNonNull(sequence, "The sequence must not be null.");
		this.containerName = Objects.requireNonNull(containerName, "The container name must not be null.");
	}

	@Override
	public ArtifactContainerType type() {
		return artifactContainerType;
	}

	@Override
	public Optional<String> containerNameOf(final JavaClass javaClass) {
		if (javaClass.getFullName().contains(sequence)) {
			return Optional.of(containerName);
		} else {
			return Optional.empty();
		}
	}

}
