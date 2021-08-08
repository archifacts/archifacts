package org.archifacts.integration.c4.asciidoc.plantuml;

import org.archifacts.integration.asciidoc.PlantUMLDocElement;
import org.archifacts.integration.c4.asciidoc.ViewDocElement;

import com.structurizr.io.Diagram;
import com.structurizr.io.plantuml.C4PlantUMLExporter;
import com.structurizr.view.View;

abstract class ViewPlantUMLDocElement<V extends View> extends ViewDocElement<V> {
	protected final C4PlantUMLExporter c4PlantUMLExporter = new C4PlantUMLExporter();

	ViewPlantUMLDocElement(final V view) {
		super(view);
	}

	protected abstract Diagram createDiagram();

	@Override
	public String render() {
		final Diagram diagram = createDiagram();
		return new PlantUMLDocElement(diagram.getKey(), diagram.getDefinition()).render();
	}

}
