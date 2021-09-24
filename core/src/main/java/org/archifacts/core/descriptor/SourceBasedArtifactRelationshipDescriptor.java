package org.archifacts.core.descriptor;

import java.util.stream.Stream;

import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationship;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Describes an {@link ArtifactRelationship}. As it is source-based the relationship's source side is the artifact which is able to provide all the information to build the
 * {@link ArtifactRelationship}.
 *
 * @author Oliver Libutzki
 * @see ArtifactRelationship
 *
 */
public interface SourceBasedArtifactRelationshipDescriptor extends ArtifactRelationshipDescriptor {

	/**
	 * Evaluates if the given {@link Artifact} is the source of the described {@link ArtifactRelationship}.
	 *
	 * @param sourceCandidateArtifact The {@link Artifact} to be evaluated
	 * @return true, if the given {@link Artifact} is the source of the described {@link ArtifactRelationship}, otherwise false.
	 */
	boolean isSource(Artifact sourceCandidateArtifact);

	/**
	 * For every {@link Artifact} which is considered to be a relationship source by {@link #isSource(Artifact)}, this method is called by passing {@link Artifact#getJavaClass()}. Returns all the
	 * targets. For each target a {@link ArtifactRelationship} is built.
	 *
	 * @param sourceClass the {@link JavaClass} of the {@link Artifact} for which {@link #isSource(Artifact)} returned true.
	 * @return all the targets from the given {@link JavaClass} which are described by this descriptor.
	 */
	Stream<JavaClass> targets(JavaClass sourceClass);

}
