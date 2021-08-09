package org.archifacts.integration.asciidoc;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AsciiDoc {

	private final List<AsciiDocElement> docElements = new ArrayList<>();

	public void addDocElement(final AsciiDocElement docElement) {
		docElements.add(docElement);
	}

	/**
	 * Writes the current AsciiDoc state into the given file. The parent directories are created if necessary.
	 * 
	 * @param outputFile The output file. Must not be <code>null</code>.
	 * 
	 * @throws IOException If an I/O error occurs
	 * @throws NullPointerException If the output file was <code>null</code>.
	 */
	public void writeToFile(final Path outputFile) throws IOException {
		Objects.requireNonNull( outputFile, "The outputFile must not be null." );
		Files.createDirectories(outputFile.getParent());
		Files.writeString(outputFile, render(), StandardCharsets.UTF_8);
	}
	
	/**
	 * Writes the current AsciiDoc state into the given writer.
	 * 
	 * @param writer The writer. Must not be <code>null</code>.
	 * 
	 * @throws IOException If an I/O error occurs
	 * @throws NullPointerException If the writer was <code>null</code>.
	 */
	public void writeToWriter(final Writer writer) throws IOException {
		Objects.requireNonNull( writer, "The writer must not be null." );
		writer.write(render());
	}

	private String render() {
		final StringBuilder stringBuilder = new StringBuilder();
		docElements
				.stream()
				.map(AsciiDocElement::render)
				.map(renderedDocElement -> renderedDocElement + "\n")
				.forEach(stringBuilder::append);
		return stringBuilder.toString();
	}
}
