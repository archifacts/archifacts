package org.archifacts.integration.axon;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.axonframework.modelling.command.AggregateRoot;

import com.tngtech.archunit.core.domain.JavaClass;

final class AggregateRootDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Aggregate Root");

	AggregateRootDescriptor() {
	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(AggregateRoot.class);
	}

}
