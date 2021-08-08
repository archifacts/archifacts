package org.archifacts.integration.jmolecules;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;

public final class JMoleculesDescriptors {

	private JMoleculesDescriptors() {
	}

	public static final class BuildingBlockDescriptors {
		private BuildingBlockDescriptors() {
		}

		public static final BuildingBlockDescriptor AggregateRootDescriptor = new AggregateRootDescriptor();
		public static final BuildingBlockDescriptor EntityDescriptor = new EntityDescriptor();
		public static final BuildingBlockDescriptor EventDescriptor = new EventDescriptor();
		public static final BuildingBlockDescriptor IdentifierDescriptor = new IdentifierDescriptor();
		public static final BuildingBlockDescriptor RepositoryDescriptor = new RepositoryDescriptor();
		public static final BuildingBlockDescriptor ServiceDescriptor = new ServiceDescriptor();
	}

	public static final class RelationshipDescriptors {

		private RelationshipDescriptors() {
		}

		public static final SourceBasedArtifactRelationshipDescriptor AggregateRootAssociationDescriptor = new AggregateRootAssociationDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor ContainedEntityDescriptor = new ContainedEntityDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor EventHandlerDescriptor = new EventHandlerDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor IdentifiedByDescriptor = new IdentifiedByDescriptor();
		public static final SourceBasedArtifactRelationshipDescriptor ManagedByDescriptor = new ManagedByDescriptor();

	}

	public static final class ContainerDescriptors {
		private ContainerDescriptors() {
		}
	}
}
