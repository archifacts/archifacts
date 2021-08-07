package org.archifacts.core.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class ArtifactContainer implements HasArtifacts, Named {
	private final ArtifactContainerDescription description;
	private Application application;

	private final Set<Artifact> artifacts = new LinkedHashSet<>();

	ArtifactContainer(final ArtifactContainerDescription description) {
		this.description = description;
	}

	void setApplication(final Application application) {
		this.application = application;
	}

	void addArtifact(final Artifact artifact) {
		artifacts.add(artifact);
	}

	public Application getApplication() {
		return application;
	}

	@Override
	public Set<Artifact> getArtifacts() {
		return Collections.unmodifiableSet(artifacts);
	}

	@Override
	public String getName() {
		return description.getName();
	}

	public ArtifactContainerType getType() {
		return description.getType();
	}

	@Override
	public String toString() {
		return "<" + description.getType().getName() + "> " + description.getName();
	}

	@Override
	public int hashCode() {
		return Objects.hash(description);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ArtifactContainer other = (ArtifactContainer) obj;
		return Objects.equals(description, other.description);
	}

}
