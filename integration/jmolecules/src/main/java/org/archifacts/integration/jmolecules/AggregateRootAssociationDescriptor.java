package org.archifacts.integration.jmolecules;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.jmolecules.ddd.types.Association;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaParameterizedType;

final class AggregateRootAssociationDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("associates");

	AggregateRootAssociationDescriptor() {
	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact.getJavaClass().getFields()
				.stream()
				.anyMatch(this::isAssociation);
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return sourceClass.getFields()
				.stream()
				.filter(this::isAssociation)
				.map(field -> field.getType())
				.filter(JavaParameterizedType.class::isInstance)
				.map(JavaParameterizedType.class::cast)
				.map(type -> type.getActualTypeArguments().get(0).toErasure());
	}

	private boolean isAssociation(final JavaField field) {
		return field.getRawType().getName().equals(Association.class.getName());
	}

}
