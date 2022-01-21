package org.archifacts.core.model;

import java.util.Objects;

import com.tngtech.archunit.core.domain.JavaClass;

public final class BuildingBlock extends Artifact {
	private final BuildingBlockType type;

	BuildingBlock(final JavaClass javaClass, final BuildingBlockType type) {
		super(javaClass);
		this.type = type;
	}

	public BuildingBlockType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "<" + type.getName() + "> " + getJavaClass().getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(type);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BuildingBlock other = (BuildingBlock) obj;
		return Objects.equals(type, other.type);
	}
}
