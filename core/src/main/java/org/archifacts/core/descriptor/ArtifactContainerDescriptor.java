package org.archifacts.core.descriptor;

import java.util.Optional;

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
	 * Returns the order of this descriptor. It multiple descriptors match the same {@link JavaClass}, the order is used to break the
	 * tie (the descriptor with the lower order is then preferred). If multiple descriptors match <b>and</b> have the same order, an
	 * exception is thrown while building the application. The returned value <b>must</b> be a static value. Otherwise the behavior
	 * is not predictable. The default order is 0. 
	 * 
	 * @return The order of this descriptor. 
	 */
	default int getOrder() {
		return 0;
	}
	
}
