package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.stereotype.Repository;

import com.tngtech.archunit.core.domain.JavaClass;

final class RepositoryDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Repository");

	RepositoryDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(Repository.class);
	}

}
