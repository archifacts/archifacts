package org.archifacts.integration.asciidoc;

public class PlantUMLDocElement implements AsciiDocElement {

	private final String filename;
	private final String plantUMLString;

	public PlantUMLDocElement(final String filename, final String plantUMLString) {
		this.filename = filename;
		this.plantUMLString = plantUMLString;
	}

	@Override
	public String render() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[plantuml, " + filename + ", svg] \n");
		stringBuilder.append("....\n");
		stringBuilder.append(plantUMLString).append("\n");
		stringBuilder.append("....\n");
		return stringBuilder.toString();
	}

}
