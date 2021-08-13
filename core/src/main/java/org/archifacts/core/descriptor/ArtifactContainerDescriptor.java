package org.archifacts.core.descriptor;

import java.util.Optional;
import java.util.regex.PatternSyntaxException;

import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Describes an {@link ArtifactContainer}.
 *
 * @author Oliver Libutzki
 * @see ArtifactContainer
 *
 */
public interface ArtifactContainerDescriptor {

	/**
	 * The type of the {@link ArtifactContainer} which this descriptor describes.
	 *
	 * @return the type of the {@link ArtifactContainer} which this descriptor
	 *         describes. Cannot be null.
	 */
	ArtifactContainerType type();

	/**
	 * Returns the name of the {@link ArtifactContainer}. Implementations are free
	 * to return {@link Optional#empty()} to indicate that they cannot determine a
	 * container for the given {@link JavaClass}.
	 *
	 * @param javaClass The {@link JavaClass} to be evaluated
	 * @return the name of the container (if present), otherwise
	 *         {@link Optional#empty()}. Cannot be null.
	 */
	Optional<String> containerNameOf(JavaClass javaClass);
	
	/**
	 * Convenient method to create a new descriptor matching all {@link JavaClass java classes} whose full qualified name contains the given sequence. The match is case sensitive.
	 * 
	 * @param artifactContainerType The type of the {@link ArtifactContainerType} the new descriptor should describe. Must not be <code>null</code>.
	 * @param sequence The sequence the matching java classes should contain in their full qualified name. Must not be <code>null</code>.
	 * @param containerName The name of the container for all matching java classes. Must not be <code>null</code>.
	 * 
	 * @return A new descriptor.
	 * 
	 * @throws NullPointerException If one of the parameter was <code>null</code>.
	 */
	static ArtifactContainerDescriptor forFullQualifiedNameContaining(final ArtifactContainerType artifactContainerType, final String sequence, final String containerName) {
		return new ArtifactContainerDescriptorForFullQualifiedNameContaining(artifactContainerType, sequence, containerName);
	}
	
	/**
	 * Convenient method to create a new descriptor matching all {@link JavaClass java classes} whose full qualified name matches the given regular expression.
	 * 
	 * @param artifactContainerType The type of the {@link ArtifactContainerType} the new descriptor should describe. Must not be <code>null</code>.
	 * @param regExp The regular expression the full qualified name of the java classes in question should match. Must not be <code>null</code> and must be a valid regular expression.
	 * @param containerName The name of the container for all matching java classes. Must not be <code>null</code>.
	 * 
	 * @return A new descriptor.
	 * 
	 * @throws NullPointerException If one of the parameter was <code>null</code>.
	 * @throws PatternSyntaxException If the given regular expression was not a valid pattern.
	 */
	static ArtifactContainerDescriptor forFullQualifiedNameMatching(final ArtifactContainerType artifactContainerType, final String regExp, final String containerName) {
		return new ArtifactContainerDescriptorForFullQualifiedNameMatching(artifactContainerType, regExp, containerName);
	}
	
}
