package org.archifacts.integration.spring;

import static org.archifacts.integration.spring.SpringDescriptors.BuildingBlockDescriptors.ConfigurationDescriptor;
import static org.archifacts.integration.spring.SpringDescriptors.BuildingBlockDescriptors.ControllerDescriptor;
import static org.archifacts.integration.spring.SpringDescriptors.BuildingBlockDescriptors.RepositoryDescriptor;
import static org.archifacts.integration.spring.SpringDescriptors.BuildingBlockDescriptors.ServiceDescriptor;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.stereotype.Component;

import com.tngtech.archunit.core.domain.JavaClass;

final class ComponentDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockDescriptor[] EXCLUDES = {ConfigurationDescriptor, RepositoryDescriptor, ServiceDescriptor, ControllerDescriptor};
	private static final BuildingBlockType TYPE = BuildingBlockType.of("Component");

	ComponentDescriptor() {
	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(Component.class) && notAnExcludedBuildingBlock(javaClass);
	}

	private boolean notAnExcludedBuildingBlock(final JavaClass javaClass) {
		return Stream.of(EXCLUDES).noneMatch(b -> b.isBuildingBlock(javaClass));
	}

}
