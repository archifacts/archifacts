package org.archifacts.integration.c4.asciidoc;

import org.archifacts.integration.asciidoc.AsciiDocElement;

import com.structurizr.view.View;

public abstract class ViewDocElement<V extends View> implements AsciiDocElement {
	protected final V view;

	protected ViewDocElement(final V view) {
		this.view = view;
	}
}
