package org.archifacts.integration.jmolecules;

import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.RepositoryDescriptor;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.archifacts.core.model.BuildingBlock;
import org.jmolecules.ddd.types.Repository;
import org.jmolecules.spring.AssociationResolver;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

final class ManagedByDescriptor implements TargetBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("managed by");

	ManagedByDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isTarget(final Artifact targetCandidateArtifact) {
		return targetCandidateArtifact instanceof BuildingBlock && ((BuildingBlock) targetCandidateArtifact).getType().equals(RepositoryDescriptor.type());
	}

	@Override
	public Stream<JavaClass> sources(final JavaClass sourceClass) {
		return sourceClass.getInterfaces()
				.stream()
				.filter(candidate -> candidate.toErasure().isAssignableTo(AssociationResolver.class) || candidate.toErasure().isAssignableTo(Repository.class))
				.filter(JavaParameterizedType.class::isInstance)
				.map(JavaParameterizedType.class::cast)
				.map(type -> type.getActualTypeArguments().get(0))
				.map(JavaType::toErasure);

	}

}