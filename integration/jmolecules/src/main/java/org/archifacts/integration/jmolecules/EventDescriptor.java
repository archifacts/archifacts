package org.archifacts.integration.jmolecules;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.jmolecules.event.types.DomainEvent;

import com.tngtech.archunit.core.domain.JavaClass;

final class EventDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Event");

	EventDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isAssignableTo(DomainEvent.class) || javaClass.isMetaAnnotatedWith(org.jmolecules.event.annotation.DomainEvent.class);
	}

}
