package org.archifacts.integration.axon;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;

public final class AxonDescriptors {

	private AxonDescriptors() {
	}

	public static final class BuildingBlockDescriptors {

		private BuildingBlockDescriptors() {
		}

		public static final BuildingBlockDescriptor AggregateRootDescriptor = new AggregateRootDescriptor();
		public static final BuildingBlockDescriptor EntityDescriptor = new EntityDescriptor();
		public static final BuildingBlockDescriptor SagaDescriptor = new SagaDescriptor();

	}

	public static final class RelationshipDescriptors {

		private RelationshipDescriptors() {
		}

		public static final SourceBasedArtifactRelationshipDescriptor CommandHandlerDescriptor = new CommandHandlerDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor EventHandlerDescriptor = new EventHandlerDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor EventSourcingHandlerDescriptor = new EventSourcingHandlerDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor AggregateIdentifiedByDescriptor = new AggregateIdentifiedByDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor EntityIdentifiedByDescriptor = new EntityIdentifiedByDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor SagaEventHandlerDescriptor = new SagaEventHandlerDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor AggregateMemberDescriptor = new AggregateMemberDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor QueryHandlerDescriptor = new QueryHandlerDescriptor();

	}

	public static final class ContainerDescriptors {

		private ContainerDescriptors() {
		}

	}

}
