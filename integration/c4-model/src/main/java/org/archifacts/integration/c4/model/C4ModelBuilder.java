package org.archifacts.integration.c4.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.archifacts.core.model.Archifact;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactRelationship;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.ExternalArtifact;
import org.archifacts.core.model.MiscArtifact;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy;
import com.structurizr.model.Model;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

public class C4ModelBuilder {

	private final List<ArtifactContainer> containers = new ArrayList<>();
	private final List<Artifact> artifacts = new ArrayList<>();
	private final List<ArtifactRelationship> relationships = new ArrayList<>();

	private final Lookup lookup = new Lookup();
	private final C4ModelComputer<ArtifactContainer> containerComputer = new C4ModelComputer<>(lookup, this::defaultTransformation);
	private final C4ModelComputer<Artifact> artifactComputer = new C4ModelComputer<>(lookup, this::defaultTransformation);
	private final C4ModelComputer<ArtifactRelationship> relationshipComputer = new C4ModelComputer<>(lookup, this::defaultTransformation);

	private final Workspace workspace;
	private final SoftwareSystem softwareSystem;

	private final Set<ModelItem> defaultTransformation(final Artifact artifact, final C4ModelLookup lookup) {
		return artifact.getContainer().map(
				container -> Collections.<ModelItem>singleton(lookup.container(container).addComponent(artifact.getName(), artifact.getJavaClass().getName(), null,
						getTypeName(artifact))))
				.orElseThrow(() -> new IllegalStateException("No container present for " + artifact));
	}

	private final Set<ModelItem> defaultTransformation(final ArtifactRelationship relationship, final C4ModelLookup lookup) {
		return Collections.<ModelItem>singleton(lookup.component(relationship.getSource())
				.uses(lookup.component(relationship.getTarget()), relationship.getRole().getName()));
	}

	private final Set<ModelItem> defaultTransformation(final ArtifactContainer container, final C4ModelLookup lookup) {
		return Collections.<ModelItem>singleton(lookup.softwareSystem().addContainer(container.getName(),
				null, container.getType().getName()));
	}

	C4ModelBuilder(final Workspace workspace) {
		this.workspace = workspace;
		this.softwareSystem = initSoftwareSystem(workspace);
	}

	private SoftwareSystem initSoftwareSystem(final Workspace workspace) {
		final Model model = workspace.getModel();
		model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy());
		return model.addSoftwareSystem(workspace.getName());
	}

	public void container(final ArtifactContainer artifactContainer) {
		containers.add(artifactContainer);
	}

	public void artifact(final Artifact artifact) {
		artifacts.add(artifact);
	}

	public void relationship(final ArtifactRelationship relationship) {
		relationships.add(relationship);
	}

	public ComputationRuleBuilderPredicateStage<ArtifactContainer> containerRule() {
		return new ComputationRuleBuilder<>(containerComputer);
	}

	public ComputationRuleBuilderPredicateStage<Artifact> artifactRule() {
		return new ComputationRuleBuilder<>(artifactComputer);
	}

	public ComputationRuleBuilderPredicateStage<ArtifactRelationship> relartionshipRule() {
		return new ComputationRuleBuilder<>(relationshipComputer);
	}

	public C4Model build() {
		containers.forEach(containerComputer::compute);
		artifacts.forEach(artifactComputer::compute);
		relationships.forEach(relationshipComputer::compute);

		final Map<Archifact, Set<ModelItem>> archifactMap = new HashMap<>();
		archifactMap.putAll(containerComputer.getMappings());
		archifactMap.putAll(artifactComputer.getMappings());
		archifactMap.putAll(relationshipComputer.getMappings());
		return new C4Model(workspace, softwareSystem, archifactMap);
	}

	public interface ComputationRuleBuilderPredicateStage<ARCHIFACT extends Archifact> {
		ComputationRuleBuilderBuildStage<ARCHIFACT> predicate(Predicate<ARCHIFACT> predicate);
	}

	public interface ComputationRuleBuilderBuildStage<ARCHIFACT extends Archifact> {
		C4ModelBuilder computation(ComputationFunction<ARCHIFACT> computation);
	}

	public final class ComputationRuleBuilder<ARCHIFACT extends Archifact> implements ComputationRuleBuilderPredicateStage<ARCHIFACT>, ComputationRuleBuilderBuildStage<ARCHIFACT> {
		private final C4ModelComputer<ARCHIFACT> c4ModelComputer;
		private Predicate<ARCHIFACT> predicate;

		private ComputationRuleBuilder(final C4ModelComputer<ARCHIFACT> c4ModelComputer) {
			this.c4ModelComputer = c4ModelComputer;
		}

		@Override
		public ComputationRuleBuilderBuildStage<ARCHIFACT> predicate(final Predicate<ARCHIFACT> predicate) {
			this.predicate = predicate;
			return this;
		}

		@Override
		public C4ModelBuilder computation(final ComputationFunction<ARCHIFACT> computation) {
			c4ModelComputer.addComputationRule(new ModelItemComputationRule<>(predicate, computation));
			return C4ModelBuilder.this;
		}

	}

	private class Lookup implements C4ModelLookup {

		@Override
		public SoftwareSystem softwareSystem() {
			return softwareSystem;
		}

		private <T extends Archifact, R extends ModelItem> R lookup(final T archifact, final C4ModelComputer<T> computer, final Class<R> returnType) {
			final Set<ModelItem> modelItems = computer.compute(archifact);
			if (modelItems.isEmpty()) {
				throw new IllegalStateException("No model item found for " + archifact);
			}
			if (modelItems.size() > 1) {
				throw new IllegalStateException("Too many model items found for " + archifact);
			}
			final ModelItem modelItem = modelItems.iterator().next();
			if (returnType.isInstance(modelItem)) {
				return returnType.cast(modelItem);
			}
			throw new IllegalStateException("Element is not of expected type: " + archifact);
		}

		@Override
		public Component component(final Artifact artifact) {
			return lookup(artifact, artifactComputer, Component.class);
		}

		@Override
		public Container container(final ArtifactContainer artifactContainer) {
			return lookup(artifactContainer, containerComputer, Container.class);
		}

		@Override
		public Relationship relationship(final ArtifactRelationship artifactRelationship) {
			return lookup(artifactRelationship, relationshipComputer, Relationship.class);
		}

	}

	private static String getTypeName(final Artifact artifact) {

		if (artifact instanceof BuildingBlock) {
			return ((BuildingBlock) artifact).getType().getName();
		}
		if (artifact instanceof ExternalArtifact) {
			return "External";
		}
		if (artifact instanceof MiscArtifact) {
			return "Misc";
		}
		throw new IllegalArgumentException("Unexpected type: " + artifact.getClass().getName());
	}
}
