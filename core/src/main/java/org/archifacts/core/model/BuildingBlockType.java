package org.archifacts.core.model;

/**
 * The type of a building block. Two instances are considered equal if and only if they are the same instance. Two newly created types with the same names are not considered equal. This is a necessary
 * limitation to avoid ambiguity when querying for elements.
 */
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
	public String toString() {
		return name;
	}

}
