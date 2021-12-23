package org.archifacts.integration.asciidoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CompositeAsciiDocElement implements AsciiDocElement{

	private List<AsciiDocElement> elements;

	private CompositeAsciiDocElement(Builder builder) {
		this.elements = Collections.unmodifiableList(builder.elements);
	}
	
	@Override
	public String render() {
		return elements
			.stream()
			.map(AsciiDocElement::render)
			.collect(Collectors.joining("\n"));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private List<AsciiDocElement> elements = new ArrayList<>();

		private Builder() {
		}

		public Builder element(AsciiDocElement element) {
			this.elements.add(element);
			return this;
		}

		public CompositeAsciiDocElement build() {
			return new CompositeAsciiDocElement(this);
		}
	}

}
