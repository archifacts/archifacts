package org.archifacts.integration.jmolecules;

import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.AggregateRootDescriptor;
import static org.archifacts.integration.jmolecules.JMoleculesDescriptors.BuildingBlockDescriptors.EntityDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.BuildingBlockType;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

final class ContainedEntityDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("contains");

	ContainedEntityDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		if (!(sourceCandidateArtifact instanceof BuildingBlock)) {
			return false;
		}
		final BuildingBlock buildingBlock = (BuildingBlock) sourceCandidateArtifact;
		final BuildingBlockType buildingBlockType = buildingBlock.getType();
		return buildingBlockType.equals(AggregateRootDescriptor.type()) || buildingBlockType.equals(EntityDescriptor.type());
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return sourceClass.getFields()
				.stream()
				.map(this::referencedEntity)
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	private boolean isCollection(final JavaField field) {
		return field.getRawType().isAssignableTo(Collection.class);
	}

	private boolean isMap(final JavaField field) {
		return field.getRawType().isAssignableTo(Map.class);
	}

	private Optional<JavaClass> referencedEntity(final JavaField field) {
		if (EntityDescriptor.isBuildingBlock(field.getRawType())) {
			return Optional.of(field.getRawType());
		}

		if (isCollection(field)) {
			final JavaType fieldType = field.getType();
			if (fieldType instanceof JavaParameterizedType) {
				final List<JavaType> actualTypeArguments = ((JavaParameterizedType) fieldType).getActualTypeArguments();
				if (!actualTypeArguments.isEmpty()) {
					final JavaClass javaClass = actualTypeArguments.get(0).toErasure();
					if (EntityDescriptor.isBuildingBlock(javaClass)) {
						return Optional.of(javaClass);
					}
					;
				}
			}
		}
		if (isMap(field)) {
			final JavaType fieldType = field.getType();
			if (fieldType instanceof JavaParameterizedType) {
				final List<JavaType> actualTypeArguments = ((JavaParameterizedType) fieldType).getActualTypeArguments();
				if (actualTypeArguments.size() > 1) {
					final JavaClass javaClass = actualTypeArguments.get(1).toErasure();
					if (EntityDescriptor.isBuildingBlock(javaClass)) {
						return Optional.of(javaClass);
					}
					;
				}
			}
		}

		return Optional.empty();
	}

}
