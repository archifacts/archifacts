package org.archifacts.integration.axon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.stream.Stream;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

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
import org.archifacts.integration.axon.domain.MyEvent2;
import org.archifacts.integration.axon.domain.MyEventHandler;
import org.archifacts.integration.axon.domain.MyQuery1;
import org.archifacts.integration.axon.domain.MyQuery2;
import org.archifacts.integration.axon.domain.MyQueryHandler;
import org.archifacts.integration.axon.domain.MySagaEventHandler;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
	void assertThat_source_based_artifact_relationship_descriptor_are_recognized(final SourceBasedArtifactRelationshipDescriptor sourceBasedArtifactRelationshipDescriptor, final Tuple... expectedTuples) {
		final Application application = Application
				.builder()
				.addSourceBasedRelationshipDescriptor(sourceBasedArtifactRelationshipDescriptor)
				.buildApplication(DOMAIN);

		assertThat(application.getRelationshipsOfType(sourceBasedArtifactRelationshipDescriptor.role()))
				.extracting(r -> r.getSource().getJavaClass().reflect(), r -> r.getTarget().getJavaClass().reflect())
				.containsExactlyInAnyOrder(expectedTuples);
	}
	
	private static Stream<Arguments> getSourceBasedArtifactRelationshipDescriptors() {
		return Stream.of(
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateIdentifiedByDescriptor, new Tuple[] {tuple(MyAggregateRoot.class, MyAggregateRootId.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EntityIdentifiedByDescriptor, new Tuple[] {tuple(MyAggregateMember1.class, String.class), tuple(MyAggregateMember2.class, String.class), tuple(MyAggregateMember3.class, String.class), tuple(MyAggregateRoot.class, MyAggregateRootId.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventHandlerDescriptor, new Tuple[] {tuple(MyEventHandler.class, MyEvent1.class), tuple(MyEventHandler.class, MyEvent2.class), tuple(MyAggregateRoot.class, MyEvent1.class), tuple(MySagaEventHandler.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventSourcingHandlerDescriptor, new Tuple[] {tuple(MyAggregateRoot.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.SagaEventHandlerDescriptor, new Tuple[] {tuple(MySagaEventHandler.class, MyEvent1.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateMemberDescriptor, new Tuple[] {tuple(MyAggregateRoot.class, MyAggregateMember1.class), tuple(MyAggregateRoot.class, MyAggregateMember2.class), tuple(MyAggregateRoot.class, MyAggregateMember3.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.QueryHandlerDescriptor, new Tuple[] {tuple(MyQueryHandler.class, MyQuery1.class), tuple(MyQueryHandler.class, MyQuery2.class)}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.CommandHandlerDescriptor, new Tuple[] {tuple(MyAggregateRoot.class, MyCommand1.class), tuple(MyAggregateRoot.class, MyCommand2.class)}));
	}
	
}
