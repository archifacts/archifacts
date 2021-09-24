package org.archifacts.core.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Represents the whole application which contains {@link ArtifactContainer
 * ArtifactContainers}, {@link Artifact Artifacts} and
 * {@link ArtifactRelationship ArtifactRelationships}.
 *
 * Note: Adding elements is not thread-safe!
 *
 * @author Oliver Libutzki
 *
 */
public final class Application implements HasContainers, HasArtifacts, HasRelationships {

	private static final Logger LOG = LogManager.getLogger(Application.class);

	private final Map<ArtifactContainerDescription, ArtifactContainer> containers = new LinkedHashMap<>();
	private final Map<JavaClass, Artifact> artifacts = new LinkedHashMap<>();
	private final Set<ArtifactRelationship> relationships = new LinkedHashSet<>();

	/**
	 * returns an {@link ApplicationBuilder} in order to initialze the
	 * {@link Application}.
	 *
	 * @return an {@link ApplicationBuilder}
	 */
	public static ApplicationBuilder builder() {
		return new ApplicationBuilder();
	}

	Application() {
	}

	@Override
	public Set<Artifact> getArtifacts() {
		return Set.copyOf(artifacts.values());
	}

	@Override
	public Set<ArtifactContainer> getContainers() {
		return Set.copyOf(containers.values());
	}

	@Override
	public Set<ArtifactRelationship> getRelationships() {
		return Collections.unmodifiableSet(relationships);
	}

	void addContainer(final ArtifactContainer container) {
		LOG.debug(() -> "Adding container: " + container);
		containers.put(ArtifactContainerDescription.fromContainer(container), container);
		container.setApplication(this);
	}

	void addArtifact(final ArtifactContainerDescription containerDescription, final Artifact artifact) {
		final ArtifactContainer container;
		if (!containers.containsKey(containerDescription)) {
			container = new ArtifactContainer(containerDescription);
			addContainer(container);
		} else {
			container = containers.get(containerDescription);
		}
		container.addArtifact(artifact);
		artifact.setContainer(container);
		addArtifact(artifact);
	}

	void addArtifact(final Artifact artifact) {
		LOG.debug(() -> "Adding artifact: " + artifact);
		artifacts.put(artifact.getJavaClass(), artifact);
	}

	void addRelationship(final ArtifactRelationship relationship) {
		LOG.debug(() -> "Adding relationship: " + relationship);
		final Artifact source = relationship.getSource();
		final Artifact target = relationship.getTarget();

		if (!artifacts.containsKey(source.getJavaClass())) {
			throw new IllegalStateException(String.format("The artifact %s has not been added.", source));
		}

		if (!artifacts.containsKey(target.getJavaClass())) {
			throw new IllegalStateException(String.format("The artifact %s has not been added.", target));
		}

		relationships.add(relationship);
		source.addOutgoingRelationship(relationship);
		target.addIncomingRelationship(relationship);

	}

	Artifact getArtifactForClass(final JavaClass javaClass) {
		return artifacts.get(javaClass);
	}
}
