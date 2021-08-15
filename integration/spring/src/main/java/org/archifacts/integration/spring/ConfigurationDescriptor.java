package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.context.annotation.Configuration;

import com.tngtech.archunit.core.domain.JavaClass;

final class ConfigurationDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Configuration");

	ConfigurationDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(Configuration.class);
	}

}
