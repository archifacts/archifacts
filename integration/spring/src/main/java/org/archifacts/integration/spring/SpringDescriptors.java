package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public final class SpringDescriptors {

	private SpringDescriptors() {
	}

	public static final class BuildingBlockDescriptors {

		private BuildingBlockDescriptors() {
		}

		public static final BuildingBlockDescriptor RepositoryDescriptor = BuildingBlockDescriptor.forMetaAnnotatedWith(BuildingBlockType.of("Repository"), Repository.class);
		public static final BuildingBlockDescriptor ServiceDescriptor = BuildingBlockDescriptor.forMetaAnnotatedWith(BuildingBlockType.of("Service"), Service.class);
		public static final BuildingBlockDescriptor ConfigurationDescriptor = BuildingBlockDescriptor.forMetaAnnotatedWith(BuildingBlockType.of("Configuration"), Configuration.class);
		public static final BuildingBlockDescriptor ControllerDescriptor = BuildingBlockDescriptor.forMetaAnnotatedWith(BuildingBlockType.of("Controller"), Controller.class);
		public static final BuildingBlockDescriptor ComponentDescriptor = BuildingBlockDescriptor.forMetaAnnotatedWith(BuildingBlockType.of("Component"), Component.class);

	}

	public static final class RelationshipDescriptors {

		private RelationshipDescriptors() {
		}
		
		public static final SourceBasedArtifactRelationshipDescriptor EventListenerDescriptor = new EventListenerDescriptor();
		public static final TargetBasedArtifactRelationshipDescriptor ManagedByDescriptor = new ManagedByDescriptor();

	}

	public static final class ContainerDescriptors {

		private ContainerDescriptors() {
		}

	}
}
