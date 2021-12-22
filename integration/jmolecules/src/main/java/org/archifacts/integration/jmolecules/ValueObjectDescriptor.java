package org.archifacts.integration.jmolecules;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.jmolecules.ddd.types.ValueObject;

import com.tngtech.archunit.core.domain.JavaClass;

final class ValueObjectDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Value Object");

	ValueObjectDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isAssignableTo(ValueObject.class) || javaClass.isMetaAnnotatedWith(org.jmolecules.ddd.annotation.ValueObject.class);
	}

}
