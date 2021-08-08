package org.archifacts.integration.c4.asciidoc.plantuml;

import com.structurizr.io.Diagram;
import com.structurizr.view.ContainerView;

public final class ContainerViewPlantUMLDocElement extends ViewPlantUMLDocElement<ContainerView> {

	public ContainerViewPlantUMLDocElement(final ContainerView containerView) {
		super(containerView);
	}

	@Override
	protected Diagram createDiagram() {
		return c4PlantUMLExporter.export(view);
	}

}
