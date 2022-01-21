package org.archifacts.integration.jmolecules;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifiable;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

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
		return sourceClass.getInterfaces()
				.stream()
				.filter(candidate -> candidate.toErasure().isAssignableTo(Entity.class))
				.filter(JavaParameterizedType.class::isInstance)
				.map(JavaParameterizedType.class::cast)
				.map(type -> type.getActualTypeArguments().get(1))
				.map(JavaType::toErasure);
	}

}
