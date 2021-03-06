package org.archifacts.integration.jmolecules;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.integration.jmolecules.domain.MyAggregateRoot;
import org.archifacts.integration.jmolecules.domain.MyAggregateRootId;
import org.archifacts.integration.jmolecules.domain.MyEntity;
import org.archifacts.integration.jmolecules.domain.MyEvent;
import org.archifacts.integration.jmolecules.domain.MyEventHandler;
import org.archifacts.integration.jmolecules.domain.MyRepository;
import org.archifacts.integration.jmolecules.domain.MyService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class JMoleculesDescriptorsTest {

	private static final JavaClasses DOMAIN = new ClassFileImporter().importPackages(MyAggregateRoot.class.getPackageName());

	@ParameterizedTest
	@MethodSource("getBuildingBlocks")
	void assertThat_building_blocks_are_recognized(final BuildingBlockDescriptor buildingBlockDescriptor, @AggregateWith(VarargsAggregator.class) final Class<?>... matchingClasses) {
		final Application application = Application
				.builder()
				.descriptor(buildingBlockDescriptor)
				.buildApplication(DOMAIN);
		final Set<String> expectedClasses = Arrays.stream(matchingClasses)
				.map(Class::getName)
				.collect(Collectors.toSet());
		assertThat(application.getBuildingBlocksOfType(buildingBlockDescriptor.type()))
				.map(BuildingBlock::getJavaClass)
				.map(JavaClass::getName)
				.containsExactlyInAnyOrderElementsOf(expectedClasses);
	}

	private static Stream<Arguments> getBuildingBlocks() {
		return Stream.of(
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor, MyAggregateRoot.class),
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.EntityDescriptor, MyAggregateRoot.class, MyEntity.class),
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.EventDescriptor, MyEvent.class),
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.IdentifierDescriptor, MyAggregateRootId.class),
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.RepositoryDescriptor, MyRepository.class),
				Arguments.of(JMoleculesDescriptors.BuildingBlockDescriptors.ServiceDescriptor, MyService.class));
	}

	static class VarargsAggregator implements ArgumentsAggregator {
		@Override
		public Object aggregateArguments(final ArgumentsAccessor accessor, final ParameterContext context) throws ArgumentsAggregationException {
			return accessor.toList().stream()
					.skip(context.getIndex())
					.toArray(Class[]::new);
		}
	}

	@ParameterizedTest
	@MethodSource("getSourceBasedArtifactRelationshipDescriptors")
	void assertThat_source_based_artifact_relationship_descriptor_are_recognized(final ArtifactRelationshipDescriptor artifactRelationshipDescriptor,
			final RelationshipPair... expectedRelationshipPairs) {
		final Application application = Application
				.builder()
				.descriptor(JMoleculesDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor)
				.descriptor(JMoleculesDescriptors.BuildingBlockDescriptors.RepositoryDescriptor)

				.descriptor(artifactRelationshipDescriptor)
				.buildApplication(DOMAIN);

		final Set<RelationshipPair> actualRelationshipPairs = application.getRelationshipsOfRole(artifactRelationshipDescriptor.role())
				.stream()
				.map(r -> new RelationshipPair(r.getSource().getJavaClass().reflect(), r.getTarget().getJavaClass().reflect()))
				.collect(Collectors.toSet());
		assertThat(actualRelationshipPairs).containsExactlyInAnyOrder(expectedRelationshipPairs);
	}

	private static Stream<Arguments> getSourceBasedArtifactRelationshipDescriptors() {
		return Stream.of(
				Arguments.of(JMoleculesDescriptors.RelationshipDescriptors.AggregateRootAssociationDescriptor, new RelationshipPair[] { pair(MyAggregateRoot.class, MyAggregateRoot.class) }),
				Arguments.of(JMoleculesDescriptors.RelationshipDescriptors.ContainedEntityDescriptor, new RelationshipPair[] { pair(MyAggregateRoot.class, MyEntity.class) }),
				Arguments.of(JMoleculesDescriptors.RelationshipDescriptors.EventHandlerDescriptor, new RelationshipPair[] { pair(MyEventHandler.class, MyEvent.class) }),
				Arguments.of(JMoleculesDescriptors.RelationshipDescriptors.IdentifiedByDescriptor, new RelationshipPair[] { pair(MyAggregateRoot.class, MyAggregateRootId.class) }),
				Arguments.of(JMoleculesDescriptors.RelationshipDescriptors.ManagedByDescriptor, new RelationshipPair[] { pair(MyAggregateRoot.class, MyRepository.class) }));
	}

	private static RelationshipPair pair(final Class<?> sourceClass, final Class<?> targetClass) {
		return new RelationshipPair(sourceClass, targetClass);
	}

	private static final class RelationshipPair {

		private final Class<?> sourceClass;
		private final Class<?> targetClass;

		RelationshipPair(final Class<?> sourceClass, final Class<?> targetClass) {
			this.sourceClass = sourceClass;
			this.targetClass = targetClass;
		}

		@Override
		public int hashCode() {
			return Objects.hash(sourceClass, targetClass);
		}

		@Override
		public boolean equals(final Object obj) {
			// Simplified implementation for the test
			final RelationshipPair other = (RelationshipPair) obj;
			return Objects.equals(sourceClass, other.sourceClass) && Objects.equals(targetClass, other.targetClass);
		}

		@Override
		public String toString() {
			return "RelationshipPair [sourceClass=" + sourceClass + ", targetClass=" + targetClass + "]";
		}

	}

}
