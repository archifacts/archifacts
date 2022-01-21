package org.archifacts.integration.spring;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.stereotype.Controller;

import com.tngtech.archunit.core.domain.JavaClass;

final class ControllerDescriptor implements BuildingBlockDescriptor {

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Controller");

	ControllerDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(Controller.class) || javaClass.isMetaAnnotatedWith(BasePathAwareController.class);
	}

}
