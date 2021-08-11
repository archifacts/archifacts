package org.archifacts.core.descriptor;

import java.util.Objects;

import org.archifacts.core.model.BuildingBlockType;

import com.tngtech.archunit.core.domain.JavaClass;

final class BuildingBlockDescriptorForSimpleNameEndingWith implements BuildingBlockDescriptor {

	private final BuildingBlockType buildingBlockType;
	private final String suffix;

	BuildingBlockDescriptorForSimpleNameEndingWith(final BuildingBlockType buildingBlockType, final String suffix) {
		this.buildingBlockType = Objects.requireNonNull(buildingBlockType, "The building block type must not be null.");
		this.suffix = Objects.requireNonNull(suffix, "The suffix must not be null.");
	}

	@Override
	public BuildingBlockType type() {
		return buildingBlockType;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.getSimpleName().endsWith(suffix);
	}

}
