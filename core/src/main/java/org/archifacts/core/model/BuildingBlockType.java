package org.archifacts.core.model;

import java.util.Objects;

public final class BuildingBlockType implements Named {

	public static BuildingBlockType of(final String name) {
		return new BuildingBlockType(name);
	}

	private final String name;

	private BuildingBlockType(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BuildingBlockType other = (BuildingBlockType) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}

}
