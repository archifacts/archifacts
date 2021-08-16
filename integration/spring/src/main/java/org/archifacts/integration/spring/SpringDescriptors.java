package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public final class SpringDescriptors {

	private SpringDescriptors() {
	}

	public static final class BuildingBlockDescriptors {
		
		private BuildingBlockDescriptors() {
		}

		public static final BuildingBlockDescriptor RepositoryDescriptor = BuildingBlockDescriptor.forMetaAnnatatedWith(BuildingBlockType.of("Repository"), Repository.class);
		public static final BuildingBlockDescriptor ServiceDescriptor = BuildingBlockDescriptor.forMetaAnnatatedWith(BuildingBlockType.of("Service"), Service.class);
		public static final BuildingBlockDescriptor ConfigurationDescriptor = BuildingBlockDescriptor.forMetaAnnatatedWith(BuildingBlockType.of("Configuration"), Configuration.class);
		public static final BuildingBlockDescriptor ControllerDescriptor = BuildingBlockDescriptor.forMetaAnnatatedWith(BuildingBlockType.of("Controller"), Controller.class);
		public static final BuildingBlockDescriptor ComponentDescriptor = new ComponentDescriptor();
		
	}

	public static final class RelationshipDescriptors {

		private RelationshipDescriptors() {
		}

	}

	public static final class ContainerDescriptors {
		
		private ContainerDescriptors() {
		}
		
	}
}
