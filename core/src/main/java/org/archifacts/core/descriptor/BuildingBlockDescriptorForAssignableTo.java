package org.archifacts.core.descriptor;

import java.util.Objects;
import java.util.stream.Stream;

import org.archifacts.core.model.BuildingBlockType;

import com.tngtech.archunit.core.domain.JavaClass;

final class BuildingBlockDescriptorForAssignableTo implements BuildingBlockDescriptor {

	private final BuildingBlockType buildingBlockType;
	private final Class<?>[] types;

	BuildingBlockDescriptorForAssignableTo(final BuildingBlockType buildingBlockType, final Class<?> ...types) {
		this.buildingBlockType = Objects.requireNonNull(buildingBlockType, "The building block type must not be null.");
		this.types = Objects.requireNonNull(types, "The types must not be null.");
	}

	@Override
	public BuildingBlockType type() {
		return buildingBlockType;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return Stream.of(types).anyMatch(javaClass::isAssignableTo);
	}

}
