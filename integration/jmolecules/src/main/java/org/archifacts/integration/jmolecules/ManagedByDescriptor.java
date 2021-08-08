package org.archifacts.integration.jmolecules;

import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.RepositoryDescriptor;

import java.util.Set;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.archifacts.core.model.BuildingBlock;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;

final class ManagedByDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("managed by");

	ManagedByDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact instanceof BuildingBlock && ((BuildingBlock) sourceCandidateArtifact).getType().equals(AggregateRootDescriptor.type());
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		final Set<Dependency> directDependenciesToSelf = sourceClass.getDirectDependenciesToSelf();

		return directDependenciesToSelf.stream()
				.filter(dependency -> RepositoryDescriptor.isBuildingBlock(dependency.getOriginClass()))
				.map(Dependency::getOriginClass);

	}
}