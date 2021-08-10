package org.archifacts.integration.asciidoc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class AsciiDoc {

	private final List<AsciiDocElement> docElements = new ArrayList<>();
	private final Path outputFile;

	public AsciiDoc(final Path outputFile) {
		this.outputFile = outputFile;
	}

	public void addDocElement(final AsciiDocElement docElement) {
		docElements.add(docElement);
	}

	public void writeFile() throws IOException {
		Files.createDirectories(outputFile.getParent());
		Files.writeString(outputFile, render(), StandardCharsets.UTF_8);
	}

	private String render() {
		final StringBuilder stringBuilder = new StringBuilder();
		docElements
				.stream()
				.map(AsciiDocElement::render)
				.forEach(renderedDocElement -> stringBuilder.append(renderedDocElement).append('\n'));
		return stringBuilder.toString();
	}
}
