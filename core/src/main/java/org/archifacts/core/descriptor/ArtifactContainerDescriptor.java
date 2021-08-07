package org.archifacts.core.descriptor;

import java.util.Optional;

import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Describes a {@link ArtifactContainer}.
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
	 * Returns the name of the {@link ArtifactContainer}. Implementations are free to
	 * return {@link Optional#empty()} to indicate that they cannot determine a
	 * container for the given {@link JavaClass}.
	 *
	 * @param javaClass The {@link JavaClass} to be evaluated
	 * @return the name of the container (if present), otherwise
	 *         {@link Optional#empty()}. Cannot be null.
	 */
	Optional<String> containerNameOf(JavaClass javaClass);
}
