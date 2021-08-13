package org.archifacts.integration.axon;

import java.util.Optional;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.modelling.command.EntityId;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaMethod;

final class EntityIdentifiedByDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("identified by");

	EntityIdentifiedByDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return getAggregateIdentifierFieldOrMethod(sourceCandidateArtifact.getJavaClass()).isPresent();
	}

	private Optional<JavaMember> getAggregateIdentifierFieldOrMethod(final JavaClass sourceClass) {
		return sourceClass.getMembers()
				.stream()
				.filter(f -> f.isAnnotatedWith(EntityId.class))
				.findFirst();
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return getAggregateIdentifierFieldOrMethod(sourceClass)
				.map(m -> getType(m))
				.stream();
	}

	private JavaClass getType(final JavaMember javaMember) {
		if (javaMember instanceof JavaField) {
			return ((JavaField) javaMember).getRawType();
		} else if (javaMember instanceof JavaMethod) {
			return ((JavaMethod) javaMember).getRawReturnType();
		} else {
			return null;
		}
	}

}
