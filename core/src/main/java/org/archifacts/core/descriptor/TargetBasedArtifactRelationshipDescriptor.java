package org.archifacts.core.descriptor;

import java.util.stream.Stream;

import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationship;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Describes an {@link ArtifactRelationship}. As it is target-based the relationship's target side is the artifact which is able to provide all the information to build the
 * {@link ArtifactRelationship}.
 *
 * @author Oliver Libutzki
 * @see ArtifactRelationship
 *
 */
public interface TargetBasedArtifactRelationshipDescriptor extends ArtifactRelationshipDescriptor {

	/**
	 * Evaluates if the given {@link Artifact} is the target of the described {@link ArtifactRelationship}.
	 *
	 * @param targetCandidateArtifact The {@link Artifact} to be evaluated
	 * @return true, if the given {@link Artifact} is the target of the described {@link ArtifactRelationship}, otherwise false.
	 */
	boolean isTarget(Artifact targetCandidateArtifact);

	/**
	 * For every {@link Artifact} which is considered to be a relationship source by {@link #isTarget(Artifact)}, this method is called by passing {@link Artifact#getJavaClass()}. Returns all the
	 * sources. For each source a {@link ArtifactRelationship} is built.
	 *
	 * @param targetClass the {@link JavaClass} of the {@link Artifact} for which {@link #isTarget(Artifact)} returned true.
	 * @return all the sources for the given {@link JavaClass} which are described by this descriptor.
	 */
	Stream<JavaClass> sources(JavaClass targetClass);

}
