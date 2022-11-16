package org.archifacts.integration.c4.asciidoc.graphviz;

import com.structurizr.export.Diagram;
import com.structurizr.view.ContainerView;

public final class ContainerViewGraphvizDocElement extends ViewGraphvizDocElement<ContainerView> {

	public ContainerViewGraphvizDocElement(final ContainerView containerView) {
		super(containerView);
	}

	@Override
	protected Diagram createDiagram() {
		return dotExporter.export(view);
	}

}
