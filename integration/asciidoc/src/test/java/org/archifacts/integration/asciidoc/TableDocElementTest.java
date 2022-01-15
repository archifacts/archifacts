package org.archifacts.integration.asciidoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.archifacts.integration.asciidoc.TableDocElement.TableDocElementBuilder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class TableDocElementTest {

	@Test
	void normal_table_should_be_renderable() {
		final Collection<FruitColor> fruits = Arrays.asList(
				new FruitColor("Apple", "Red"),
				new FruitColor("Banana", "Yellow"),
				new FruitColor("Pear", "Green"));
		final TableDocElement<FruitColor> tableDocElement = TableDocElement
				.forElements(fruits)
				.title("Fruits")
				.column("Name", FruitColor::getName)
				.column("Color", FruitColor::getColor)
				.build();

		final String renderedTable = tableDocElement.render();
		assertThat(renderedTable).contains("Fruits", "Name", "Color");
		assertThat(renderedTable).contains(getNames(fruits));
		assertThat(renderedTable).contains(getColors(fruits));
	}

	@Test
	void null_as_elements_should_raise_exception() {
		final Iterable<FruitColor> fruits = null;
		assertThatNullPointerException().isThrownBy(() -> TableDocElement.forElements(fruits));
	}

	@Test
	void null_as_extractor_function_should_raise_exception() {
		final Iterable<FruitColor> fruits = Collections.emptyList();
		final TableDocElementBuilder<FruitColor> tableDocElementBuilder = TableDocElement
				.forElements(fruits)
				.title("Fruits");

		assertThatNullPointerException().isThrownBy(() -> tableDocElementBuilder.column("Name", null));
	}

	@Test
	void table_should_be_renderable_when_labels_are_null() {
		final Collection<FruitColor> fruits = Arrays.asList(
				new FruitColor("Apple", "Red"),
				new FruitColor("Banana", "Yellow"),
				new FruitColor("Pear", "Green"));
		final TableDocElement<FruitColor> tableDocElement = TableDocElement
				.forElements(fruits)
				.column(fp -> null)
				.build();

		final String renderedTable = tableDocElement.render();
		assertThat(renderedTable).doesNotContain("Fruits", "Name", "Color");
		assertThat(renderedTable).doesNotContain(getNames(fruits));
		assertThat(renderedTable).doesNotContain(getColors(fruits));
	}

	@Test
	void table_should_be_renderable_when_titles_are_null() {
		final Collection<FruitColor> fruits = Arrays.asList(
				new FruitColor("Apple", "Red"),
				new FruitColor("Banana", "Yellow"),
				new FruitColor("Pear", "Green"));
		final TableDocElement<FruitColor> tableDocElement = TableDocElement
				.forElements(fruits)
				.column(FruitColor::getName)
				.column(FruitColor::getColor)
				.build();

		final String renderedTable = tableDocElement.render();
		assertThat(renderedTable).doesNotContain("Fruits", "Name", "Color");
		assertThat(renderedTable).contains(getNames(fruits));
		assertThat(renderedTable).contains(getColors(fruits));
	}

	private Iterable<String> getNames(final Collection<FruitColor> fruits) {
		return fruits
				.stream()
				.map(FruitColor::getName)
				.collect(Collectors.toList());
	}

	private Iterable<String> getColors(final Collection<FruitColor> fruits) {
		return fruits
				.stream()
				.map(FruitColor::getColor)
				.collect(Collectors.toList());
	}

	private static class FruitColor {

		private final String name;
		private final String color;

		private FruitColor(final String name, final String color) {
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public String getColor() {
			return color;
		}

	}

}
