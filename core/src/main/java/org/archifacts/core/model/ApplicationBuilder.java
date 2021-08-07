package org.archifacts.core.model;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;

/**
 * Offers methods to register descriptors and is capable of building the
 * {@link Application} by applying those descriptors.
 *
 * @author Oliver Libutzki
 *
 */
public final class ApplicationBuilder {

	private final Set<ArtifactContainerDescriptor> containerDescriptors = new LinkedHashSet<>();
	private final Set<BuildingBlockDescriptor> buildingBlockDescriptors = new LinkedHashSet<>();
	private final Set<SourceBasedArtifactRelationshipDescriptor> sourceBasedRelationshipDescriptors = new LinkedHashSet<>();
	private final Set<TargetBasedArtifactRelationshipDescriptor> targetBasedRelationshipDescriptors = new LinkedHashSet<>();

	/**
	 * Registers a {@link ArtifactContainerDescriptor}.
	 *
	 * @param artifactContainerDescriptor the descriptor to be added
	 */
	public void addContainerDescriptor(final ArtifactContainerDescriptor artifactContainerDescriptor) {
		this.containerDescriptors.add(artifactContainerDescriptor);
	}

	/**
	 * Registers a {@link BuildingBlockDescriptor}.
	 *
	 * @param buildingBlockDescriptor the descriptor to be added
	 */
	public void addBuildingBlockDescriptor(final BuildingBlockDescriptor buildingBlockDescriptor) {
		buildingBlockDescriptors.add(buildingBlockDescriptor);
	}

	/**
	 * Registers a {@link SourceBasedArtifactRelationshipDescriptor}.
	 *
	 * @param sourceBasedArtifactRelationshipDescriptor the descriptor to be added
	 */
	public void addSourceBasedRelationshipDescriptor(final SourceBasedArtifactRelationshipDescriptor sourceBasedArtifactRelationshipDescriptor) {
		sourceBasedRelationshipDescriptors.add(sourceBasedArtifactRelationshipDescriptor);
	}

	/**
	 * Registers a {@link TargetBasedArtifactRelationshipDescriptor}.
	 *
	 * @param targetBasedArtifactRelationshipDescriptor the descriptor to be added
	 */
	public void addTargetBasedRelationshipDescriptor(final TargetBasedArtifactRelationshipDescriptor targetBasedArtifactRelationshipDescriptor) {
		targetBasedRelationshipDescriptors.add(targetBasedArtifactRelationshipDescriptor);
	}

	/**
	 * Build the {@link Application} by applying the descriptors.
	 *
	 * @param javaClasses The application's scope. All the classes which are part of
	 *                    {@link JavaClasses} are classes which are contained in the
	 *                    application.
	 * @return the {@link Application}
	 */
	public Application buildApplication(final JavaClasses javaClasses) {

		validateBuildingBlockDescriptors();

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

	private void validateBuildingBlockDescriptors() {
		final Set<Entry<BuildingBlockType, List<BuildingBlockDescriptor>>> duplicateBuildingBlockTypes = buildingBlockDescriptors
				.stream()
				.collect(Collectors.groupingBy(BuildingBlockDescriptor::type))
				.entrySet()
				.stream()
				.filter(count -> count.getValue().size() > 1)
				.collect(toSet());

		if (!duplicateBuildingBlockTypes.isEmpty()) {
			final String message = duplicateBuildingBlockTypes.stream().map(this::toErrorMessage).collect(Collectors.joining(", "));

			throw new IllegalStateException(
					"For the following BuildingBlockTypes multiple descriptors have been registered: " + message);
		}
	}

	private String toErrorMessage(final Entry<BuildingBlockType, List<BuildingBlockDescriptor>> entry) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(entry.getKey()).append("(");
		stringBuilder.append(toErrorMessage(entry.getValue()));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	private String toErrorMessage(final List<BuildingBlockDescriptor> descriptors) {
		return descriptors.stream()
				.map(Object::getClass)
				.map(Class::getName)
				.collect(Collectors.joining(", "));
	}

	private void addArtifact(final Application application, final Artifact artifact) {
		containerDescriptors
				.stream()
				.map(
						moduleDescriptor -> moduleDescriptor.containerNameOf(artifact.getJavaClass())
								.<DescriptorBasedArtifactContainerDescription>map(containerName -> new DescriptorBasedArtifactContainerDescription(moduleDescriptor, containerName)))
				.filter(Optional::isPresent)
				.<DescriptorBasedArtifactContainerDescription>map(Optional::get)
				.distinct()
				.collect(collectingAndThen(toList(), descriptions -> {
					if (descriptions.size() == 1) {
						return Optional.of(descriptions.get(0));
					}
					if (descriptions.isEmpty()) {
						return Optional.<DescriptorBasedArtifactContainerDescription>empty();
					}
					throw new IllegalStateException(
							"For " + artifact.getName() + " multiple ContainerDecriptors match: " + descriptions.stream()
									.map(DescriptorBasedArtifactContainerDescription::getDescriptor)
									.map(ArtifactContainerDescriptor::getClass)
									.map(Class::getName)
									.collect(Collectors.joining(", ")));
				}))
				.ifPresentOrElse(
						archContainerDescription -> application.addArtifact(archContainerDescription, artifact),
						() -> application.addArtifact(artifact));
		;
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
				.collect(collectingAndThen(toList(), descriptors -> {
					if (descriptors.size() == 1) {
						return Optional.of(descriptors.get(0));
					}
					if (descriptors.isEmpty()) {
						return Optional.<BuildingBlockDescriptor>empty();
					}
					throw new IllegalStateException(
							"For " + javaClass.getName() + " multiple BuildingBlockDescriptors match: " + buildingBlockDescriptors.stream()
									.map(BuildingBlockDescriptor::getClass)
									.map(Class::getName)
									.collect(Collectors.joining(", ")));
				}))
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
