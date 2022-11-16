package org.archifacts.integration.c4.asciidoc.plantuml;

import com.structurizr.export.Diagram;
import com.structurizr.view.ComponentView;

public final class ComponentViewPlantUMLDocElement extends ViewPlantUMLDocElement<ComponentView> {

	public ComponentViewPlantUMLDocElement(final ComponentView componentView) {
		super(componentView);
	}

	@Override
	protected Diagram createDiagram() {
		return c4PlantUMLExporter.export(view);
	}

}
