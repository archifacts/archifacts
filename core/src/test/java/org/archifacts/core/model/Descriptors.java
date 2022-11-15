package org.archifacts.core.model;

import java.util.Optional;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.descriptor.TargetBasedArtifactRelationshipDescriptor;

import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;

final class Descriptors {

	static final class BuildingBlockType1Descriptor implements BuildingBlockDescriptor {

		static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType1");

		@Override
		public BuildingBlockType type() {
			return TYPE;
		}

		@Override
		public boolean isBuildingBlock(final JavaClass javaClass) {
			return javaClass.getSimpleName().endsWith("_BuildingBlockType1");
		}
	}

	static final class SecondBuildingBlockType1Descriptor implements BuildingBlockDescriptor {

		private static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType1-Duplicate");

		@Override
		public BuildingBlockType type() {
			return TYPE;
		}

		@Override
		public boolean isBuildingBlock(final JavaClass javaClass) {
			return javaClass.getSimpleName().endsWith("_BuildingBlockType1");
		}
	}

	static final class BuildingBlockType2Descriptor implements BuildingBlockDescriptor {

		static final BuildingBlockType TYPE = BuildingBlockType.of("BuildingBlockType2");

		@Override
		public BuildingBlockType type() {
			return TYPE;
		}

		@Override
		public boolean isBuildingBlock(final JavaClass javaClass) {
			return javaClass.getSimpleName().endsWith("_BuildingBlockType2");
		}
	}

	static final class ContainerType1Descriptor implements ArtifactContainerDescriptor {

		static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType1");

		@Override
		public ArtifactContainerType type() {
			return TYPE;
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			final String[] splittedName = javaClass.getSimpleName().split("_");
			if (splittedName.length > 1) {
				final String containerName = splittedName[0];
				if ("ContainerType1".equals(containerName)) {
					return Optional.of("Container1");
				}
			}
			return Optional.empty();
		}
	}

	static final class SecondContainerType1Descriptor implements ArtifactContainerDescriptor {

		private static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType1");

		@Override
		public ArtifactContainerType type() {
			return TYPE;
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			final String[] splittedName = javaClass.getSimpleName().split("_");
			if (splittedName.length > 1) {
				final String containerName = splittedName[0];
				if ("ContainerType1".equals(containerName)) {
					return Optional.of("SecondContainer1");
				}
			}
			return Optional.empty();
		}
	}

	static final class ContainerType2Descriptor implements ArtifactContainerDescriptor {

		static final ArtifactContainerType TYPE = ArtifactContainerType.of("ContainerType2");

		@Override
		public ArtifactContainerType type() {
			return TYPE;
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			final String[] splittedName = javaClass.getSimpleName().split("_");
			if (splittedName.length > 1) {
				final String containerName = splittedName[0];
				if ("ContainerType2".equals(containerName)) {
					return Optional.of("Container2");
				}
			}
			return Optional.empty();
		}
	}

	static final class NonMatchingBuildingBlockDescriptor implements BuildingBlockDescriptor {

		private static final BuildingBlockType TYPE = BuildingBlockType.of("Non-matching");

		@Override
		public BuildingBlockType type() {
			return TYPE;
		}

		@Override
		public boolean isBuildingBlock(final JavaClass javaClass) {
			return false;
		}
	}

	static final class ExternalArtifactRelationshipDescriptor implements SourceBasedArtifactRelationshipDescriptor {

		static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("extref");

		@Override
		public ArtifactRelationshipRole role() {
			return ROLE;
		}

		@Override
		public boolean isSource(final Artifact sourceCandidateArtifact) {
			return sourceCandidateArtifact.getJavaClass().getFields().stream()
					.anyMatch(field -> field.getName().equals("externalArtifact"));
		}

		@Override
		public Stream<JavaClass> targets(final JavaClass sourceClass) {
			return sourceClass.getFields().stream().filter(field -> field.getName().equals("externalArtifact"))
					.map(JavaField::getRawType);
		}
	}

	static final class MiscArtifactRelationshipDescriptor implements TargetBasedArtifactRelationshipDescriptor {

		static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("miscref");

		@Override
		public ArtifactRelationshipRole role() {
			return ROLE;
		}

		@Override
		public boolean isTarget(final Artifact targetCandidateArtifact) {
			return !targetCandidateArtifact.getJavaClass().getDirectDependenciesToSelf().isEmpty();
		}

		@Override
		public Stream<JavaClass> sources(final JavaClass targetClass) {
			return targetClass.getDirectDependenciesToSelf()
				.stream().map(Dependency::getOriginClass);
		}

	}

}