package org.archifacts.integration.asciidoc;

public class TextDocElement implements AsciiDocElement {

	private final String text;

	public TextDocElement(final String text) {
		this.text = text;
	}

	@Override
	public String render() {
		return text;
	}

}
