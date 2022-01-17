package org.archifacts.integration.c4.model;

import static java.util.Collections.emptySet;

import java.util.Map;
import java.util.Set;

import org.archifacts.core.model.Archifact;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

public class C4Model {

	private final Map<Archifact, Set<ModelItem>> archifactMap;
	private final Workspace workspace;
	private final SoftwareSystem softwareSystem;

	public C4Model(final Workspace workspace, final SoftwareSystem softwareSystem, final Map<Archifact, Set<ModelItem>> archifactMap) {
		this.workspace = workspace;
		this.softwareSystem = softwareSystem;
		this.archifactMap = archifactMap;
	}

	public Component component(final Archifact archifact) {
		return modelElement(archifact, Component.class);
	}

	public Relationship relationship(final Archifact archifact) {
		return modelElement(archifact, Relationship.class);
	}

	public Container container(final Archifact archifact) {
		return modelElement(archifact, Container.class);
	}

	private <T extends ModelItem> T modelElement(final Archifact archifact, final Class<T> elementType) {
		return archifactMap.getOrDefault(archifact, emptySet())
				.stream()
				.filter(elementType::isInstance)
				.map(elementType::cast)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException(String.format("%s is not mapped to a C4 model item of type %s", archifact, elementType.getName())));
	}

	public Workspace workspace() {
		return workspace;
	}

	public SoftwareSystem softwareSystem() {
		return softwareSystem;
	}

	public static C4ModelBuilder builder(final Workspace workspace) {
		return new C4ModelBuilder(workspace);
	}

}
