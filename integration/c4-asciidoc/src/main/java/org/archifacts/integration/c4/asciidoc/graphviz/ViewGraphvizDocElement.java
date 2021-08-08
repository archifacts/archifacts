package org.archifacts.integration.c4.asciidoc.graphviz;

import org.archifacts.integration.asciidoc.GraphvizDocElement;
import org.archifacts.integration.c4.asciidoc.ViewDocElement;

import com.structurizr.io.Diagram;
import com.structurizr.io.dot.DOTExporter;
import com.structurizr.view.View;

abstract class ViewGraphvizDocElement<V extends View> extends ViewDocElement<V> {
	protected final DOTExporter dotExporter = new DOTExporter();

	ViewGraphvizDocElement(final V view) {
		super(view);
	}

	protected abstract Diagram createDiagram();

	@Override
	public String render() {
		final Diagram diagram = createDiagram();
		return new GraphvizDocElement(diagram.getKey(), diagram.getDefinition()).render();
	}

}
