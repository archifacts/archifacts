package org.archifacts.integration.c4.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactContainerType;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.BuildingBlockType;
import org.archifacts.core.model.MiscArtifact;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
public class C4ModelBuilderTest {

	private static final ArtifactContainerType containerType1 = ArtifactContainerType.of("ContainerType1");
	private static final ArtifactRelationshipRole referencesRelationshipRole = ArtifactRelationshipRole.of("references");
	private static final BuildingBlockType buildingBlockType1 = BuildingBlockType.of("BuildingBlockType1");

	private static final BuildingBlockDescriptor buildingBlockType1Descriptor = BuildingBlockDescriptor.forSimpleNameEndingWith(buildingBlockType1, "BuildingBlockType1");

	private static final ArtifactContainerDescriptor defaultContainerDescriptor = new ArtifactContainerDescriptor() {
		@Override
		public ArtifactContainerType type() {
			return containerType1;
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			return Optional.of("DefaultContainer");
		}
	};

	private static final SourceBasedArtifactRelationshipDescriptor referenceDescriptor = new SourceBasedArtifactRelationshipDescriptor() {

		@Override
		public ArtifactRelationshipRole role() {
			return referencesRelationshipRole;
		}

		@Override
		public boolean isSource(final Artifact sourceCandidateArtifact) {
			return true;
		}

		@Override
		public Stream<JavaClass> targets(final JavaClass sourceClass) {
			return sourceClass.getFields()
					.stream()
					.map(JavaField::getRawType);
		}
	};

	@Test
	void assert_that_an_empty_model_does_not_contain_any_container() {

		final C4Model c4Model = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null)).build();

		assertThat(c4Model.softwareSystem().getContainers()).isEmpty();

	}

	@Test
	void assert_that_an_IllegalStateException_is_thrown_if_a_building_block_does_not_have_a_container() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");
		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application.getBuildingBlocksOfType(buildingBlockType1).forEach(c4ModelBuilder::artifact);

		assertThatIllegalStateException().isThrownBy(() -> c4ModelBuilder.build())
				.withMessage("No container present for <BuildingBlockType1> org.archifacts.integration.c4.model.domain.Class1ForBuildingBlockType1");
	}

	@Test
	void assert_that_containers_are_added_to_the_c4_model() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(defaultContainerDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.map(Artifact::getContainer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.distinct()
				.forEach(c4ModelBuilder::container);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components).isEmpty();
	}

	@Test
	void assert_that_containers_are_added_automatically() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application.getBuildingBlocksOfType(buildingBlockType1).forEach(c4ModelBuilder::artifact);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components)
				.hasSize(1)
				.extracting(Component::getName, Component::getTechnology)
				.containsExactly(tuple("Class1ForBuildingBlockType1", "BuildingBlockType1"));
	}

	@Test
	void assert_that_referenced_artifacts_are_not_added_automatically() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application.getBuildingBlocksOfType(buildingBlockType1).forEach(c4ModelBuilder::artifact);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components)
				.hasSize(1)
				.extracting(Component::getName, Component::getTechnology)
				.containsExactly(tuple("Class1ForBuildingBlockType1", "BuildingBlockType1"));
	}

	@Test
	void assert_that_relationships_are_not_added_automatically() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.forEach(c4ModelBuilder::artifact);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components)
				.hasSize(2)
				.extracting(Component::getName, Component::getTechnology)
				.containsExactlyInAnyOrder(
						tuple("Class1ForBuildingBlockType1", "BuildingBlockType1"),
						tuple("MiscArtifact1", "Misc"));

		assertThat(components)
				.flatMap(Component::getRelationships)
				.isEmpty();
	}

	@Test
	void assert_that_misc_artifacts_are_added_to_the_c4_model() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.filter(MiscArtifact.class::isInstance)
				.forEach(c4ModelBuilder::artifact);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components)
				.hasSize(1)
				.extracting(Component::getName, Component::getTechnology)
				.containsExactly(tuple("MiscArtifact1", "Misc"));
	}

	@Test
	void assert_that_relationships_are_added_to_the_c4_model() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.forEach(c4ModelBuilder::artifact);
		application.getRelationships()
				.stream()
				.forEach(c4ModelBuilder::relationship);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Container container = containers.iterator().next();

		final Component class1ForBuildingBlockType1 = container.getComponentWithName("Class1ForBuildingBlockType1");
		assertThat(class1ForBuildingBlockType1).isNotNull();

		final Component miscArtifact1 = container.getComponentWithName("MiscArtifact1");
		assertThat(miscArtifact1).isNotNull();

		assertThat(container.getComponents())
				.flatMap(Component::getRelationships)
				.hasSize(1)
				.extracting(Relationship::getDescription, Relationship::getSource, Relationship::getDestination)
				.containsExactly(tuple("references", class1ForBuildingBlockType1, miscArtifact1));
	}

	@Test
	void assert_that_relationship_source_and_target_is_automatically_added_to_the_c4_model() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application.getRelationships()
				.stream()
				.forEach(c4ModelBuilder::relationship);
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Container container = containers.iterator().next();

		final Component class1ForBuildingBlockType1 = container.getComponentWithName("Class1ForBuildingBlockType1");
		assertThat(class1ForBuildingBlockType1).isNotNull();

		final Component miscArtifact1 = container.getComponentWithName("MiscArtifact1");
		assertThat(miscArtifact1).isNotNull();

		assertThat(container.getComponents())
				.flatMap(Component::getRelationships)
				.hasSize(1)
				.extracting(Relationship::getDescription, Relationship::getSource, Relationship::getDestination)
				.containsExactly(tuple("references", class1ForBuildingBlockType1, miscArtifact1));
	}

	@Test
	void assert_that_computation_rule_is_applied_to_artifact() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		final Set<ArtifactContainer> artifactContainers = application.getContainersOfType(containerType1);

		assertThat(artifactContainers).hasSize(1);
		final ArtifactContainer defaultContainer = artifactContainers.iterator().next();
		application.getBuildingBlocksOfType(buildingBlockType1).forEach(c4ModelBuilder::artifact);
		c4ModelBuilder.artifactRule()
				.predicate(artifact -> artifact.getName().equals("Class1ForBuildingBlockType1"))
				.computation((artifact, lookup) -> Set.of(
						lookup.container(defaultContainer).addComponent("CustomName1", null, "CustomTechnology1"),
						lookup.container(defaultContainer).addComponent("CustomName2", null, "CustomTechnology2")));
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Set<Component> components = containers.iterator().next().getComponents();

		assertThat(components)
				.hasSize(2)
				.extracting(Component::getName, Component::getTechnology)
				.containsExactlyInAnyOrder(
						tuple("CustomName1", "CustomTechnology1"),
						tuple("CustomName2", "CustomTechnology2"));
	}

	@Test
	void assert_that_computation_rule_is_applied_to_container() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(defaultContainerDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.map(Artifact::getContainer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.distinct()
				.forEach(c4ModelBuilder::container);
		application.getBuildingBlocksOfType(buildingBlockType1).forEach(c4ModelBuilder::artifact);
		c4ModelBuilder.containerRule()
				.predicate(a -> true)
				.computation((container, lookup) -> Set.of(
						lookup.softwareSystem().addContainer("CustomName1", null, "CustomTechnology1"),
						lookup.softwareSystem().addContainer("CustomName2", null, "CustomTechnology2")));
		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(2)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactlyInAnyOrder(
						tuple("CustomName1", "CustomTechnology1"),
						tuple("CustomName2", "CustomTechnology2"));
	}

	@Test
	void assert_that_computation_rule_is_applied_to_relationship() {
		final JavaClasses javaClasses = new ClassFileImporter().importPackages("org.archifacts.integration.c4.model.domain");

		final Application application = Application
				.builder()
				.descriptor(buildingBlockType1Descriptor)
				.descriptor(defaultContainerDescriptor)
				.descriptor(referenceDescriptor)
				.buildApplication(javaClasses);

		final C4ModelBuilder c4ModelBuilder = C4Model.builder(new Workspace(this.getClass().getSimpleName(), null));
		application
				.getArtifacts()
				.stream()
				.forEach(c4ModelBuilder::artifact);
		application.getRelationships()
				.stream()
				.forEach(c4ModelBuilder::relationship);

		final BuildingBlock source = application.getBuildingBlocksOfType(buildingBlockType1).iterator().next();
		final Artifact target = application.getArtifacts().stream().filter(MiscArtifact.class::isInstance).findFirst().get();

		c4ModelBuilder.relartionshipRule()
				.predicate(a -> true)
				.computation((relationship, lookup) -> Set.of(
						lookup.component(source).uses(lookup.component(target), "uses"),
						lookup.component(target).uses(lookup.component(source), "is used by")

				));

		final C4Model c4Model = c4ModelBuilder.build();
		final Set<Container> containers = c4Model.softwareSystem().getContainers();

		assertThat(containers)
				.hasSize(1)
				.extracting(Container::getName, Container::getTechnology)
				.containsExactly(tuple("DefaultContainer", "ContainerType1"));
		final Container container = containers.iterator().next();

		final Component class1ForBuildingBlockType1 = container.getComponentWithName("Class1ForBuildingBlockType1");
		assertThat(class1ForBuildingBlockType1).isNotNull();

		final Component miscArtifact1 = container.getComponentWithName("MiscArtifact1");
		assertThat(miscArtifact1).isNotNull();

		assertThat(container.getComponents())
				.flatMap(Component::getRelationships)
				.hasSize(2)
				.extracting(Relationship::getDescription, Relationship::getSource, Relationship::getDestination)
				.containsExactlyInAnyOrder(
						tuple("uses", class1ForBuildingBlockType1, miscArtifact1),
						tuple("is used by", miscArtifact1, class1ForBuildingBlockType1));
	}
}
