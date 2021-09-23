package org.archifacts.integration.asciidoc;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Arrays;
import java.util.Collections;

import org.archifacts.integration.asciidoc.TableDocElement.TableDocElementBuilder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class TableDocElementTest {

	@Test
	void normal_table_should_be_renderable() {
		final Iterable<FruitColor> fruits = Arrays.asList(
				new FruitColor("Apple", "Red"),
				new FruitColor("Banana", "Yellow"),
				new FruitColor("Pear", "Green")
		);
		final TableDocElement<FruitColor> tableDocElement = TableDocElement
			.forElements(fruits)
			.title("Fruits")
			.column("Name", fp -> fp.name)
			.column("Color", fp -> fp.color)
			.build();
		
		assertThatCode(() -> tableDocElement.render()).doesNotThrowAnyException();
	}
	
	@Test
	void null_as_elements_should_raise_exception() {
		final Iterable<FruitColor> fruits = null;
		assertThatNullPointerException().isThrownBy(() -> TableDocElement.forElements(fruits));
	}
	
	@Test
	void null_as_extractor_function_should_raise_exception() {
		final Iterable<FruitColor> fruits = Collections.emptyList( );
		final TableDocElementBuilder<FruitColor> tableDocElementBuilder = TableDocElement
			.forElements(fruits)
			.title("Fruits");
		
		assertThatNullPointerException().isThrownBy(() -> tableDocElementBuilder.column("Name", null));
	}
	
	@Test
	void table_should_be_renderable_when_labels_are_null() {
		final Iterable<FruitColor> fruits = Arrays.asList(
				new FruitColor("Apple", "Red"),
				new FruitColor("Banana", "Yellow"),
				new FruitColor("Pear", "Green")
		);
		final TableDocElement<FruitColor> tableDocElement = TableDocElement
			.forElements(fruits)
			.column(fp -> null)
			.build();
		
		assertThatCode(() -> tableDocElement.render()).doesNotThrowAnyException();
	}
	
	private static class FruitColor {
		
		private final String name;
		private final String color;
		
		private FruitColor(final String name, final String color) {
			this.name = name;
			this.color = color;
		}
		
	}
	
}
