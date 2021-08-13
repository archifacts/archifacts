package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;

public final class SpringDescriptors {

	private SpringDescriptors() {
	}

	public static final class BuildingBlockDescriptors {
		
		private BuildingBlockDescriptors() {
		}

		public static final BuildingBlockDescriptor RepositoryDescriptor = new RepositoryDescriptor();
		public static final BuildingBlockDescriptor ServiceDescriptor = new ServiceDescriptor();
		public static final BuildingBlockDescriptor ComponentDescriptor = new ComponentDescriptor();
		public static final BuildingBlockDescriptor ConfigurationDescriptor = new ConfigurationDescriptor();
		public static final BuildingBlockDescriptor ControllerDescriptor = new ControllerDescriptor();
		
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
