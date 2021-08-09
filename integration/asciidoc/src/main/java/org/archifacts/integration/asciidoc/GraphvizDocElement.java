package org.archifacts.integration.asciidoc;

public class GraphvizDocElement implements AsciiDocElement {

	private final String filename;
	private final String graphvizString;

	public GraphvizDocElement(final String filename, final String graphvizString) {
		this.filename = filename;
		this.graphvizString = graphvizString;
	}

	@Override
	public String render() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[graphviz, ").append(filename).append(", svg] \n");
		stringBuilder.append("....\n");
		stringBuilder.append(graphvizString).append('\n');
		stringBuilder.append("....\n");
		return stringBuilder.toString();
	}

}
