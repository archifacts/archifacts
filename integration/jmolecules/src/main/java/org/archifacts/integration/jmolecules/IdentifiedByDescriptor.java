package org.archifacts.integration.jmolecules;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.jmolecules.ddd.types.Identifiable;

import com.tngtech.archunit.core.domain.JavaClass;

final class IdentifiedByDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("identified by");

	IdentifiedByDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact.getJavaClass().isAssignableTo(Identifiable.class);
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		final JavaClass rawReturnType = sourceClass.getMethod("getId").getRawReturnType();
		return Stream.of(rawReturnType);
	}

}
