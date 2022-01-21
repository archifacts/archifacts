package org.archifacts.core.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.Descriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

/**
 * Offers methods to register descriptors and is capable of building the {@link Application} by applying those descriptors.
 *
 * An instance of {@link ApplicationBuilder} can be obtained by calling {@link Application#builder()}.
 *
 * Note: Adding descriptors is not thread-safe!
 *
 * @author Oliver Libutzki
 *
 */
public final class ApplicationBuilder {

	private final List<ArtifactContainerDescriptor> containerDescriptors = new ArrayList<>();
	private final List<BuildingBlockDescriptor> buildingBlockDescriptors = new ArrayList<>();
	private final Set<SourceBasedArtifactRelationshipDescriptor> sourceBasedRelationshipDescriptors = new LinkedHashSet<>();
	private final Set<TargetBasedArtifactRelationshipDescriptor> targetBasedRelationshipDescriptors = new LinkedHashSet<>();

	ApplicationBuilder() {
	}

	/**
	 * Registers a {@link Descriptor}.
	 *
	 * Supported descriptors are:
	 * <ul>
	 * <li>{@link ArtifactContainerDescriptor}</li>
	 * <li>{@link BuildingBlockDescriptor}</li>
	 * <li>{@link SourceBasedArtifactRelationshipDescriptor}</li>
	 * <li>{@link TargetBasedArtifactRelationshipDescriptor}</li>
	 * </ul>
	 *
	 * @param descriptor the descriptor to be added, cannot be null
	 * @return this instance for method-chaining
	 */
	public ApplicationBuilder descriptor(final Descriptor descriptor) {
		Objects.requireNonNull(descriptor, "The Descriptor cannot be null");
		if (descriptor instanceof ArtifactContainerDescriptor) {
			this.containerDescriptors.add((ArtifactContainerDescriptor) descriptor);
		} else if (descriptor instanceof BuildingBlockDescriptor) {
			this.buildingBlockDescriptors.add((BuildingBlockDescriptor) descriptor);
		} else if (descriptor instanceof SourceBasedArtifactRelationshipDescriptor) {
			this.sourceBasedRelationshipDescriptors.add((SourceBasedArtifactRelationshipDescriptor) descriptor);
		} else if (descriptor instanceof TargetBasedArtifactRelationshipDescriptor) {
			this.targetBasedRelationshipDescriptors.add((TargetBasedArtifactRelationshipDescriptor) descriptor);
		} else {
			throw new IllegalArgumentException("The descriptor has an unexpected type: " + descriptor.getClass().getName());
		}
		return this;
	}

	/**
	 * Build the {@link Application} by applying the descriptors.
	 *
	 * @param javaClasses The application's scope. All the classes which are part of {@link JavaClasses} are classes which are contained in the application. Cannot be null.
	 * @return the {@link Application}
	 */
	public Application buildApplication(final JavaClasses javaClasses) {
		Objects.requireNonNull(javaClasses, "JavaClasses cannot be null");

		final Application application = new Application();

		toArtifacts(javaClasses)
				.forEach(artifact -> addArtifact(application, artifact));

		targetBasedRelationshipDescriptors
				.stream()
				.forEach(targetBasedRelationshipDescriptor -> {
					application.getArtifacts()
							.stream()
							.forEach(artifact -> {
								if (targetBasedRelationshipDescriptor.isTarget(artifact)) {
									targetBasedRelationshipDescriptor.sources(artifact.getJavaClass())
											.forEach(sourceJavaClass -> {
												Artifact sourceArtifact = application.getArtifactForClass(sourceJavaClass);
												if (sourceArtifact == null) {
													sourceArtifact = new ExternalArtifact(sourceJavaClass);
													addArtifact(application, sourceArtifact);
												}
												application.addRelationship(new ArtifactRelationship(sourceArtifact, artifact, targetBasedRelationshipDescriptor.role()));
											});
								}
							});
				});

		sourceBasedRelationshipDescriptors
				.stream()
				.forEach(sourceBasedRelationshipDescriptor -> {
					application.getArtifacts()
							.stream()
							.forEach(artifact -> {
								if (sourceBasedRelationshipDescriptor.isSource(artifact)) {
									sourceBasedRelationshipDescriptor.targets(artifact.getJavaClass())
											.forEach(targetJavaClass -> {
												Artifact targetArtifact = application.getArtifactForClass(targetJavaClass);
												if (targetArtifact == null) {
													targetArtifact = new ExternalArtifact(targetJavaClass);
													addArtifact(application, targetArtifact);
												}
												application.addRelationship(new ArtifactRelationship(artifact, targetArtifact, sourceBasedRelationshipDescriptor.role()));
											});
								}
							});
				});

		return application;
	}

	private void addArtifact(final Application application, final Artifact artifact) {
		containerDescriptors
				.stream()
				.map(
						containerDescriptor -> containerDescriptor.containerNameOf(artifact.getJavaClass())
								.map(containerName -> new ArtifactContainerDescription(containerDescriptor.type(), containerName)))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.distinct()
				.findFirst()
				.ifPresentOrElse(
						archContainerDescription -> application.addArtifact(archContainerDescription, artifact),
						() -> application.addArtifact(artifact));

	}

	private Stream<Artifact> toArtifacts(final JavaClasses javaClasses) {
		return javaClasses
				.stream()
				.map(javaClass -> toArtifact(javaClass));
	}

	private Artifact toArtifact(final JavaClass javaClass) {
		return buildingBlockDescriptors
				.stream()
				.filter(buildingBlockDescriptor -> buildingBlockDescriptor.isBuildingBlock(javaClass))
				.findFirst()
				.map(buildingBlockDescriptor -> toBuildingBlock(javaClass, buildingBlockDescriptor))
				.orElseGet(() -> toMiscArtifact(javaClass));
	}

	private Artifact toBuildingBlock(final JavaClass javaClass, final BuildingBlockDescriptor buildingBlockDescriptor) {
		return new BuildingBlock(javaClass, buildingBlockDescriptor.type());
	}

	private Artifact toMiscArtifact(final JavaClass javaClass) {
		return new MiscArtifact(javaClass);
	}
}
