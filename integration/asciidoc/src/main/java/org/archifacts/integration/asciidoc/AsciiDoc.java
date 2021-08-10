package org.archifacts.integration.asciidoc;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AsciiDoc {

	private final List<AsciiDocElement> docElements = new ArrayList<>();

	public void addDocElement(final AsciiDocElement docElement) {
		docElements.add(docElement);
	}

	/**
	 * Writes the current AsciiDoc state into the given writer. The writer is neither flushed nor closed.
	 * 
	 * @param writer The writer. Must not be <code>null</code>.
	 * 
	 * @throws IOException If an I/O error occurs
	 * @throws NullPointerException If the writer was <code>null</code>.
	 */
	public void writeToWriter(final Writer writer) throws IOException {
		Objects.requireNonNull(writer, "The writer must not be null.");
		writer.write(render());
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
