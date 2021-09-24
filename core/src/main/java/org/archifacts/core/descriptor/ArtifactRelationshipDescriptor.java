package org.archifacts.core.descriptor;

import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactRelationship;
import org.archifacts.core.model.ArtifactRelationshipRole;

/**
 * Describes an {@link ArtifactRelationship}.
 *
 * @author Oliver Libutzki
 * @see ArtifactRelationship
 *
 */
public interface ArtifactRelationshipDescriptor {

	/**
	 * The role of the {@link ArtifactContainer} which this descriptor describes.
	 *
	 * @return the role of the {@link ArtifactContainer} which this descriptor describes. Cannot be null.
	 */
	ArtifactRelationshipRole role();

}