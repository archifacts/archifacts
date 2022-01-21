package org.archifacts.integration.asciidoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A simple {@link AsciiDocElement} allowing to render a multi-column table based on given elements. The table does not make a defensive copy of the elements so changes in the underlying
 * {@link Iterable} will also reflect in the table.
 *
 * @param <T> The generic type of the elements within the table.
 */
public final class TableDocElement<T> implements AsciiDocElement {

	private final List<TableDocElementColumn<T>> columns;
	private final Iterable<T> elements;
	private final String title;

	private TableDocElement(final TableDocElementBuilder<T> builder) {
		// Make a defensive copy of the columns to avoid changes by using the builder a second time.
		columns = new ArrayList<>(builder.getColumns());
		elements = builder.getElements();
		title = builder.getTitle();
	}

	/**
	 * Creates a new builder for a table based on the given elements.
	 *
	 * @param <T>      The generic type of the elements within the table.
	 *
	 * @param elements The elements within the table. Must not be <code>null</code>.
	 *
	 * @return A new builder instance.
	 *
	 * @throws NullPointerException If the given elements were <code>null</code>.
	 */
	public static <T> TableDocElementBuilder<T> forElements(final Iterable<T> elements) {
		Objects.requireNonNull(elements, "The elements must not be null.");

		return new TableDocElementBuilder<>(elements);
	}

	@Override
	public String render() {
		final StringBuilder stringBuilder = new StringBuilder();

		renderTitle(stringBuilder);
		renderStartEndTag(stringBuilder);
		renderHeader(stringBuilder);
		renderElements(stringBuilder);
		renderStartEndTag(stringBuilder);

		return stringBuilder.toString();
	}

	private void renderTitle(final StringBuilder stringBuilder) {
		if (title != null) {
			stringBuilder.append('.').append(title).append('\n');
		}
	}

	private void renderStartEndTag(final StringBuilder stringBuilder) {
		stringBuilder.append("|===\n");
	}

	private void renderHeader(final StringBuilder stringBuilder) {
		final boolean atLeastOneColumnTitleIsNotNull = columns
				.stream()
				.map(TableDocElementColumn<T>::getTitle)
				.anyMatch(Objects::nonNull);

		if (atLeastOneColumnTitleIsNotNull) {
			for (final TableDocElementColumn<T> column : columns) {
				stringBuilder.append('|');
				if (column.title != null) {
					stringBuilder.append(column.getTitle());
				}
			}

			stringBuilder.append('\n');
		}
	}

	private void renderElements(final StringBuilder stringBuilder) {
		stringBuilder.append('\n');

		for (final T element : elements) {
			for (final TableDocElementColumn<T> column : columns) {
				final String label = column.getLabelExtractor().apply(element);
				renderElement(stringBuilder, label);
			}
			stringBuilder.append('\n');
		}

		stringBuilder.append('\n');
	}

	private void renderElement(final StringBuilder stringBuilder, final String label) {
		stringBuilder.append('|');
		if (label != null) {
			stringBuilder.append(label);
		}
		stringBuilder.append('\n');
	}

	private static final class TableDocElementColumn<T> {

		private final String title;
		private final Function<T, String> labelExtractor;

		private TableDocElementColumn(final String title, final Function<T, String> labelExtractor) {
			this.title = title;
			this.labelExtractor = labelExtractor;
		}

		private String getTitle() {
			return title;
		}

		private Function<T, String> getLabelExtractor() {
			return labelExtractor;
		}

	}

	public static class TableDocElementBuilder<T> {

		private final List<TableDocElementColumn<T>> columns = new ArrayList<>();
		private final Iterable<T> elements;
		private String title;

		private TableDocElementBuilder(final Iterable<T> elements) {
			this.elements = elements;
		}

		public TableDocElement<T> build() {
			return new TableDocElement<>(this);
		}

		/**
		 * Add another column to this builder.
		 *
		 * @param columnTitle    The title of the column.
		 * @param labelExtractor The function which is applied on the elements to extract the labels. Must not be <code>null</code>.
		 *
		 * @return The builder instance.
		 *
		 * @throws NullPointerException If the label extractor was <code>null</code>.
		 */
		public TableDocElementBuilder<T> column(final String columnTitle, final Function<T, String> labelExtractor) {
			Objects.requireNonNull(labelExtractor, "The label extractor must not be null.");

			columns.add(new TableDocElementColumn<>(columnTitle, labelExtractor));
			return this;
		}

		/**
		 * Add another column without label to this builder.
		 *
		 * @param labelExtractor The function which is applied on the elements to extract the labels. Must not be <code>null</code>.
		 *
		 * @return The builder instance.
		 *
		 * @throws NullPointerException If the label extractor was <code>null</code>.
		 */
		public TableDocElementBuilder<T> column(final Function<T, String> labelExtractor) {
			return column(null, labelExtractor);
		}

		/**
		 * Sets the optional title of the table.
		 *
		 * @param title The new title of the table.
		 *
		 * @return The builder instance.
		 */
		public TableDocElementBuilder<T> title(final String title) {
			this.title = title;
			return this;
		}

		private List<TableDocElementColumn<T>> getColumns() {
			return columns;
		}

		private Iterable<T> getElements() {
			return elements;
		}

		private String getTitle() {
			return title;
		}

	}

}
