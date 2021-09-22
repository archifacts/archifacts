package org.archifacts.integration.asciidoc;

/**
 * Implementations of this interface are considered renderable AsciiDoc elements. The implementations  
 * are responsible themselves for providing valid AsciiDoc.
 */
@FunctionalInterface
public interface AsciiDocElement {

	/**
	 * Renders the current element as a String.
	 * 
	 * @return The AsciiDoc String representing the element. Should not be <code>null</code>.
	 */
	String render();
	
}
