package org.archifacts.integration.c4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.archifacts.core.model.Application;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactRelationship;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.Named;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

public final class C4ModelTransformer {

	private static final class CachingSupplier<T> implements Supplier<T> {

		private T cachedValue;
		private final Supplier<T> originalSupplier;

		public CachingSupplier(final Supplier<T> originalSupplier) {
			this.originalSupplier = originalSupplier;
		}

		@Override
		public T get() {
			if (cachedValue == null) {
				cachedValue = originalSupplier.get();
			}
			return cachedValue;
		}
	}

	private static <T> Supplier<T> memoize(final Supplier<T> valueSupplier) {
		return new CachingSupplier<T>(valueSupplier);
	}

	private final Map<ArtifactContainer, Container> containerMap = new HashMap<>();
	private final Map<Artifact, Component> componentMap = new HashMap<>();
	private final Map<ArtifactRelationship, Relationship> relationshipMap = new HashMap<>();
	private Optional<Container> noContainerContainer = Optional.empty();

	private final Application application;
	private final SoftwareSystem softwareSystem;

	public C4ModelTransformer(final Application application, final SoftwareSystem softwareSystem) {
		this.application = application;
		this.softwareSystem = softwareSystem;
		transform();
	}

	private void transform() {
		transformContainers();
		transformRelationships();
	}

	private void transformContainers() {
		final Supplier<Container> noContainerContainerSupplier = memoize(() -> {
			final Container noContainerContainer = softwareSystem.addContainer("no-container");
			this.noContainerContainer = Optional.of(noContainerContainer);
			return noContainerContainer;
		});
		
		final Map<Container, List<Artifact>> containerArtifactsMap = new HashMap<>();
		application.getArtifacts().forEach(artifact -> {
			final Container container = artifact.getContainer()
					.map(artifactContainer -> containerMap.computeIfAbsent(artifactContainer, m -> softwareSystem.addContainer(m.getName(), null, artifactContainer.getType().getName())))
					.orElseGet(noContainerContainerSupplier);
			final List<Artifact> artifacts = containerArtifactsMap.computeIfAbsent(container, x -> new ArrayList<>());
			artifacts.add(artifact);
		});
		containerArtifactsMap.entrySet().forEach(entry -> {
			final Container container = entry.getKey();
			final List<Artifact> artifacts = entry.getValue();
			final boolean containsAmbiguousNames = containsAmbiguousNames(artifacts);
			artifacts.forEach(artifact -> {
				final String componentName = containsAmbiguousNames ? artifact.getJavaClass().getFullName() : artifact.getName();
				final Component component = container.addComponent(componentName, artifact.getJavaClass().getName(), null, getTechnology(artifact));
				componentMap.put(artifact, component);
			});
		});
	}

	private void transformRelationships() {
		application.getRelationships().forEach(artifactRelationship -> {
			final Component sourceComponent = componentMap.get(artifactRelationship.getSource());
			final Component targetComponent = componentMap.get(artifactRelationship.getTarget());
			final Relationship relationship = sourceComponent.uses(targetComponent, artifactRelationship.getRole().getName());
			relationshipMap.put(artifactRelationship, relationship);
		});
	}

	private boolean containsAmbiguousNames(final List<? extends Named> named) {
		return named
				.stream()
				.map(Named::getName)
				.distinct()
				.count() < named.size();
	} 

	private String getTechnology(final Artifact artifact) {
		if (artifact instanceof BuildingBlock) {
			return ((BuildingBlock) artifact).getType().getName();
		}
		return null;
	}

	public Set<Container> getContainers(final Predicate<ArtifactContainer> predicate) {
		return containerMap.entrySet().stream()
				.filter(entry -> predicate.test(entry.getKey()))
				.map(Entry::getValue)
				.collect(Collectors.toUnmodifiableSet());
	}

	public Set<Component> getComponents(final Predicate<Artifact> predicate) {
		return componentMap.entrySet().stream()
				.filter(entry -> predicate.test(entry.getKey()))
				.map(Entry::getValue)
				.collect(Collectors.toUnmodifiableSet());
	}

	public Set<Relationship> getRelationships(final Predicate<ArtifactRelationship> predicate) {
		return relationshipMap.entrySet().stream()
				.filter(entry -> predicate.test(entry.getKey()))
				.map(Entry::getValue)
				.collect(Collectors.toUnmodifiableSet());
	}

	public Optional<Container> getNoContainerContainer() {
		return noContainerContainer;
	}
}
