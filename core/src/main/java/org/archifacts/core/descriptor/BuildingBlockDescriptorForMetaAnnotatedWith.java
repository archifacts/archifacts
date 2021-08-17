package org.archifacts.core.descriptor;

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.archifacts.core.model.BuildingBlockType;

import com.tngtech.archunit.core.domain.JavaClass;

final class BuildingBlockDescriptorForMetaAnnotatedWith implements BuildingBlockDescriptor {

	private final BuildingBlockType buildingBlockType;
	private final Class<? extends Annotation> type;

	BuildingBlockDescriptorForMetaAnnotatedWith(final BuildingBlockType buildingBlockType, final Class<? extends Annotation> type) {
		this.buildingBlockType = Objects.requireNonNull(buildingBlockType, "The building block type must not be null.");
		this.type = Objects.requireNonNull(type, "The type must not be null.");
	}

	@Override
	public BuildingBlockType type() {
		return buildingBlockType;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(type);
	}

}
