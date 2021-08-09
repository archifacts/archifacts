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
		stringBuilder.append( "[plantuml, " ).append( filename ).append( ", svg] " ).append( System.lineSeparator() );
		stringBuilder.append( "...." ).append( System.lineSeparator() );
		stringBuilder.append(plantUMLString).append(System.lineSeparator());
		stringBuilder.append( "...." ).append( System.lineSeparator() );
		return stringBuilder.toString();
	}

}
