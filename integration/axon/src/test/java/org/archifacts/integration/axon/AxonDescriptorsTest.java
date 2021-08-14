package org.archifacts.integration.axon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.integration.axon.domain.MyAggregateMember1;
import org.archifacts.integration.axon.domain.MyAggregateMember2;
import org.archifacts.integration.axon.domain.MyAggregateMember3;
import org.archifacts.integration.axon.domain.MyAggregateRoot;
import org.archifacts.integration.axon.domain.MyAggregateRootId;
import org.archifacts.integration.axon.domain.MyCommand1;
import org.archifacts.integration.axon.domain.MyCommand2;
import org.archifacts.integration.axon.domain.MyEvent1;
import org.archifacts.integration.axon.domain.MyEventHandler;
import org.archifacts.integration.axon.domain.MyQuery1;
import org.archifacts.integration.axon.domain.MyQueryHandler;
import org.archifacts.integration.axon.domain.MySagaEventHandler;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class AxonDescriptorsTest {

	private static final JavaClasses DOMAIN = new ClassFileImporter().importPackages(MyAggregateRoot.class.getPackageName());

	@ParameterizedTest
	@MethodSource("getBuildingBlocks")
	void assertThat_building_blocks_are_recognized(final BuildingBlockDescriptor buildingBlockDescriptor, final Class<?> matchingClass) {
		final Application application = Application
				.builder()
				.addBuildingBlockDescriptor(buildingBlockDescriptor)
				.buildApplication(DOMAIN);
		
		assertThat(application.getArtifactsOfType(buildingBlockDescriptor.type()))
				.map(b -> b.getJavaClass())
				.allMatch(j -> j.isEquivalentTo(matchingClass));
	}
	
	private static Stream<Arguments> getBuildingBlocks() {
		return Stream.of(Arguments.of(AxonDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor, MyAggregateRoot.class));
	}
	
	@ParameterizedTest
	@MethodSource("getSourceBasedArtifactRelationshipDescriptors")
	void assertThat_source_based_artifact_relationship_descriptor_are_recognized(final SourceBasedArtifactRelationshipDescriptor sourceBasedArtifactRelationshipDescriptor, final RelationshipPair... expectedRelationshipPairs) {
		final Application application = Application
				.builder()
				.addSourceBasedRelationshipDescriptor(sourceBasedArtifactRelationshipDescriptor)
				.buildApplication(DOMAIN);
		
		final Set<RelationshipPair> actualRelationshipPairs = application.getRelationshipsOfType(sourceBasedArtifactRelationshipDescriptor.role())
				.stream()
				.map(r -> new RelationshipPair(r.getSource().getJavaClass().reflect(), r.getTarget().getJavaClass().reflect()))
				.collect(Collectors.toSet());
		assertThat(actualRelationshipPairs).containsExactlyInAnyOrder(expectedRelationshipPairs);
	}
	
	private static Stream<Arguments> getSourceBasedArtifactRelationshipDescriptors() {
		return Stream.of(
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateIdentifiedByDescriptor, new RelationshipPair[] {pair(MyAggregateRoot.class, MyAggregateRootId.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EntityIdentifiedByDescriptor, new RelationshipPair[] {pair(MyAggregateMember1.class, String.class), pair(MyAggregateMember2.class, String.class), pair(MyAggregateMember3.class, String.class), pair(MyAggregateRoot.class, MyAggregateRootId.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventHandlerDescriptor, new RelationshipPair[] {pair(MyEventHandler.class, MyEvent1.class), pair(MyAggregateRoot.class, MyEvent1.class), pair(MySagaEventHandler.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventSourcingHandlerDescriptor, new RelationshipPair[] {pair(MyAggregateRoot.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.SagaEventHandlerDescriptor, new RelationshipPair[] {pair(MySagaEventHandler.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateMemberDescriptor, new RelationshipPair[] {pair(MyAggregateRoot.class, MyAggregateMember1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.QueryHandlerDescriptor, new RelationshipPair[] {pair(MyQueryHandler.class, MyQuery1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.CommandHandlerDescriptor, new RelationshipPair[] {pair(MyAggregateRoot.class, MyCommand1.class), pair(MyAggregateRoot.class, MyCommand2.class)}));
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
		public int hashCode( ) {
			return Objects.hash(sourceClass, targetClass);
		}

		@Override
		public boolean equals(final Object obj) {
			// Simplified implementation for the test
			final RelationshipPair other = ( RelationshipPair ) obj;
			return Objects.equals(sourceClass, other.sourceClass) && Objects.equals(targetClass, other.targetClass);
		}

		@Override
		public String toString( ) {
			return "RelationshipPair [sourceClass=" + sourceClass + ", targetClass=" + targetClass + "]";
		}
		
		
	}
	
}
