package org.archifacts.integration.axon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.integration.axon.domain.MyAggregateMember;
import org.archifacts.integration.axon.domain.MyAggregateRoot;
import org.archifacts.integration.axon.domain.MyAggregateRootId;
import org.archifacts.integration.axon.domain.MyCommand1;
import org.archifacts.integration.axon.domain.MyCommand2;
import org.archifacts.integration.axon.domain.MyEvent1;
import org.archifacts.integration.axon.domain.MyEventHandler;
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
	void assertThat_source_based_artifact_relationship_descriptor_are_recognized(final SourceBasedArtifactRelationshipDescriptor sourceBasedArtifactRelationshipDescriptor, final Class<?> matchingSourceClass, final Class<?> ...matchingTargetClasses) {
		final Application application = Application
				.builder()
				.addSourceBasedRelationshipDescriptor(sourceBasedArtifactRelationshipDescriptor)
				.buildApplication(DOMAIN);
		
		assertThat(application.getRelationshipsOfType(sourceBasedArtifactRelationshipDescriptor.role()))
				.map(r -> r.getSource().getJavaClass())
				.allMatch(j -> j.isEquivalentTo(matchingSourceClass));
		assertThat(application.getRelationshipsOfType(sourceBasedArtifactRelationshipDescriptor.role()))
				.map(r -> r.getTarget().getJavaClass())
				.allMatch(j -> Stream.of(matchingTargetClasses).anyMatch(c -> j.isEquivalentTo(c)));
	}
	
	private static Stream<Arguments> getSourceBasedArtifactRelationshipDescriptors() {
		return Stream.of(
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateIdentifiedByDescriptor, MyAggregateRoot.class, new Class<?>[] {MyAggregateRootId.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EntityIdentifiedByDescriptor, MyAggregateMember.class, new Class<?>[] {String.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventHandlerDescriptor, MyEventHandler.class, new Class<?>[] {MyEvent1.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.EventSourcingHandlerDescriptor, MyAggregateRoot.class, new Class<?>[] {MyEvent1.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.SagaEventHandlerDescriptor, MySagaEventHandler.class, new Class<?>[] {MyEvent1.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.AggregateMemberDescriptor, MyAggregateRoot.class, new Class<?>[] {MyAggregateMember.class}),
				Arguments.of(AxonDescriptors.RelationshipDescriptors.CommandHandlerDescriptor, MyAggregateRoot.class, new Class<?>[] {MyCommand1.class, MyCommand2.class}));
	}
	
}
