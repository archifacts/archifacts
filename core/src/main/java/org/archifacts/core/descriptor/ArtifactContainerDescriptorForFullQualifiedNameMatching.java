package org.archifacts.core.descriptor;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;

final class ArtifactContainerDescriptorForFullQualifiedNameMatching implements ArtifactContainerDescriptor {

	private final ArtifactContainerType artifactContainerType;
	private final Pattern pattern;
	private final String containerName;

	ArtifactContainerDescriptorForFullQualifiedNameMatching(final ArtifactContainerType artifactContainerType, final String regExp, final String containerName) {
		this.artifactContainerType = Objects.requireNonNull(artifactContainerType, "The artifact container type must not be null.");
		this.containerName = Objects.requireNonNull(containerName, "The container name must not be null.");
		Objects.requireNonNull(regExp, "The regular expression must not be null.");
		pattern = Pattern.compile(regExp);
	}

	@Override
	public ArtifactContainerType type() {
		return artifactContainerType;
	}

	@Override
	public Optional<String> containerNameOf(final JavaClass javaClass) {
		if (pattern.matcher(javaClass.getFullName()).matches()) {
			return Optional.of(containerName);
		} else {
			return Optional.empty();
		}
	}

}
