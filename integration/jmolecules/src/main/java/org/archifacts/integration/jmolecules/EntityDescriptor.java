package org.archifacts.integration.jmolecules;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.jmolecules.ddd.types.Entity;

import com.tngtech.archunit.core.domain.JavaClass;

final class EntityDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Entity");

	EntityDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isAssignableTo(Entity.class) || javaClass.isMetaAnnotatedWith(org.jmolecules.ddd.annotation.Entity.class);
	}

}
