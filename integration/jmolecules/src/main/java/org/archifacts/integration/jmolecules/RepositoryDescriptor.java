package org.archifacts.integration.jmolecules;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.jmolecules.ddd.types.Repository;

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
		return javaClass.isAssignableTo(Repository.class) || javaClass.isMetaAnnotatedWith(org.jmolecules.ddd.annotation.Repository.class);
	}

}
