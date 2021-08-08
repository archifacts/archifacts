package org.archifacts.integration.c4.asciidoc.graphviz;

import com.structurizr.io.Diagram;
import com.structurizr.view.ComponentView;

public final class ComponentViewGraphvizDocElement extends ViewGraphvizDocElement<ComponentView> {

	public ComponentViewGraphvizDocElement(final ComponentView componentView) {
		super(componentView);
	}

	@Override
	protected Diagram createDiagram() {
		return dotExporter.export(view);
	}

}
