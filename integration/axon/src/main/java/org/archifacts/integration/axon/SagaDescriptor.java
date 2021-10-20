package org.archifacts.integration.axon;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.axonframework.modelling.saga.StartSaga;

import com.tngtech.archunit.core.domain.JavaClass;

final class SagaDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Saga");

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(JavaClass javaClass) {
		return javaClass.getMethods()
				.stream()
				.anyMatch(method -> method.isMetaAnnotatedWith(StartSaga.class));
	}

}
