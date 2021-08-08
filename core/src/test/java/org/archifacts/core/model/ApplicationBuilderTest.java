package org.archifacts.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Optional;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)

class ApplicationBuilderTest {

	static class TestEvent {

	}

	static class AnyClass {

	}

	static class EventDescriptor implements BuildingBlockDescriptor {

		@Override
		public BuildingBlockType type() {
			return BuildingBlockType.of("Event");
		}

		@Override
		public boolean isBuildingBlock(final JavaClass javaClass) {
			return javaClass.getSimpleName().endsWith("Event");
		}

	}

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class NullParameters {

		@Test
		void assert_that_null_cannot_be_added_as_BuildingBlockDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatIllegalArgumentException().isThrownBy(() -> applicationBuilder.addBuildingBlockDescriptor(null))
					.withMessage("The BuildingBlockDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_ContainerDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatIllegalArgumentException().isThrownBy(() -> applicationBuilder.addContainerDescriptor(null))
					.withMessage("The ArtifactContainerDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_SourceBasedRelationshipDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatIllegalArgumentException().isThrownBy(() -> applicationBuilder.addSourceBasedRelationshipDescriptor(null))
					.withMessage("The SourceBasedArtifactRelationshipDescriptor cannot be null");
		}

		@Test
		void assert_that_null_cannot_be_added_as_TargetBasedRelationshipDescriptor() {
			final ApplicationBuilder applicationBuilder = Application.builder();
			assertThatIllegalArgumentException().isThrownBy(() -> applicationBuilder.addTargetBasedRelationshipDescriptor(null))
					.withMessage("The TargetBasedArtifactRelationshipDescriptor cannot be null");
		}
	}

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class Artifacts {
		private JavaClasses javaClasses;

		@BeforeAll
		void init() {
			javaClasses = new ClassFileImporter().importClasses(TestEvent.class, AnyClass.class);
		}

		class Event2Descriptor implements BuildingBlockDescriptor {

			@Override
			public BuildingBlockType type() {
				return BuildingBlockType.of("Event2");
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return javaClass.getSimpleName().endsWith("Event");
			}

		}

		class ArbitraryDescriptor implements BuildingBlockDescriptor {

			@Override
			public BuildingBlockType type() {
				return BuildingBlockType.of("Arbitrary");
			}

			@Override
			public boolean isBuildingBlock(final JavaClass javaClass) {
				return false;
			}

		}

		@Test
		void assert_that_matching_BuildingBlockDescriptor_creates_a_BuildingBlock() {
			final ApplicationBuilder applicationBuilder = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor());
			final Application application = applicationBuilder.buildApplication(javaClasses);
			assertThat(application.getArtifacts())
					.extracting(Artifact::getClass, Artifact::getName, artifact -> artifact.getJavaClass().getName())
					.contains(
							tuple(BuildingBlock.class, TestEvent.class.getSimpleName(), TestEvent.class.getName()));
			assertThat(application.getBuildingBlocks()).extracting(BuildingBlock::getType).containsExactly(BuildingBlockType.of("Event"));
		}

		@Test
		void assert_that_MiscArtifact_is_created_if_no_BuildingBlockDescriptor_matches() {
			final ApplicationBuilder applicationBuilder = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor());
			final Application application = applicationBuilder.buildApplication(javaClasses);
			assertThat(application.getArtifacts())
					.extracting(Artifact::getClass, Artifact::getName, artifact -> artifact.getJavaClass().getName())
					.contains(
							tuple(MiscArtifact.class, AnyClass.class.getSimpleName(), AnyClass.class.getName()));
		}

		@Test
		void assert_that_a_artifact_is_created_for_each_class() {
			final ApplicationBuilder applicationBuilder = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor());
			final Application application = applicationBuilder.buildApplication(javaClasses);
			assertThat(application.getArtifacts()).hasSize(2);
			assertThat(application.getArtifacts())
					.extracting(artifact -> artifact.getJavaClass().getName())
					.containsExactlyInAnyOrder(TestEvent.class.getName(), AnyClass.class.getName());
		}

		@Test
		void assert_that_multiple_matching_BuildingBlockDescriptors_cannot_handle_the_same_type() {
			final ApplicationBuilder applicationBuilder = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addBuildingBlockDescriptor(new ArbitraryDescriptor());
			assertThatIllegalStateException().isThrownBy(() -> applicationBuilder.buildApplication(javaClasses))
					.withMessage(
							"For the following BuildingBlockTypes multiple descriptors have been registered: Event(" + EventDescriptor.class.getName() + ", " + EventDescriptor.class.getName() + ")");
		}

		@Test
		void assert_that_multiple_matching_BuildingBlockDescriptors_returning_the_different_results_lead_to_an_error() {
			final ApplicationBuilder applicationBuilder = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addBuildingBlockDescriptor(new Event2Descriptor())
					.addBuildingBlockDescriptor(new ArbitraryDescriptor());
			assertThatIllegalStateException().isThrownBy(() -> applicationBuilder.buildApplication(javaClasses))
					.withMessage(
							"For " + TestEvent.class.getName() + " multiple BuildingBlockDescriptors match: " + EventDescriptor.class.getName() + ", " + Event2Descriptor.class.getName());
		}
	}

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class Containers {

		class NoContainerTestEvent {

		}

		class NoContainerAnyClass {

		}

		private JavaClasses javaClasses;

		@BeforeAll
		void init() {
			javaClasses = new ClassFileImporter().importClasses(TestEvent.class, AnyClass.class, NoContainerTestEvent.class, NoContainerAnyClass.class);
		}

		class TestContainerDescriptor implements ArtifactContainerDescriptor {

			@Override
			public ArtifactContainerType type() {
				return ArtifactContainerType.of("Concept");
			}

			@Override
			public Optional<String> containerNameOf(final JavaClass javaClass) {
				if (javaClass.getSimpleName().startsWith("NoContainer")) {
					return Optional.empty();
				}
				return Optional.of("TestContainer");
			}
		}

		@Test
		void assert_that_BuildingBlock_can_have_a_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addContainerDescriptor(new TestContainerDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts()
					.stream()
					.filter(artifact -> artifact.getJavaClass().getName().equals(TestEvent.class.getName()))
					.findFirst()
					.orElseThrow();
			assertThat(testEventArtifact.getContainer())
					.isPresent()
					.get()
					.extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.contains("TestContainer", ArtifactContainerType.of("Concept"));
		}

		@Test
		void assert_that_MiscArtifact_can_have_a_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addContainerDescriptor(new TestContainerDescriptor())
					.buildApplication(javaClasses);
			final Artifact anyClassArtifact = application.getArtifacts()
					.stream()
					.filter(artifact -> artifact.getJavaClass().getName().equals(AnyClass.class.getName()))
					.findFirst()
					.orElseThrow();
			assertThat(anyClassArtifact.getContainer())
					.isPresent()
					.get()
					.extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.contains("TestContainer", ArtifactContainerType.of("Concept"));
		}

		@Test
		void assert_that_BuildingBlock_can_have_no_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addContainerDescriptor(new TestContainerDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts()
					.stream()
					.filter(artifact -> artifact.getJavaClass().getName().equals(NoContainerTestEvent.class.getName()))
					.findFirst()
					.orElseThrow();
			assertThat(testEventArtifact.getContainer())
					.isEmpty();
		}

		@Test
		void assert_that_MiscArtifact_can_have_no_container() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addContainerDescriptor(new TestContainerDescriptor())
					.buildApplication(javaClasses);
			final Artifact testEventArtifact = application.getArtifacts()
					.stream()
					.filter(artifact -> artifact.getJavaClass().getName().equals(NoContainerAnyClass.class.getName()))
					.findFirst()
					.orElseThrow();
			assertThat(testEventArtifact.getContainer())
					.isEmpty();
		}

		@Test
		void assert_that_containers_are_present() {
			final Application application = Application.builder()
					.addBuildingBlockDescriptor(new EventDescriptor())
					.addContainerDescriptor(new TestContainerDescriptor())
					.buildApplication(javaClasses);

			assertThat(application.getContainers())
					.hasSize(1)
					.first()
					.extracting(ArtifactContainer::getName, ArtifactContainer::getType)
					.contains("TestContainer", ArtifactContainerType.of("Concept"));
		}
	}

}
