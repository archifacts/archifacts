package org.archifacts.core.model;

import static org.archifacts.core.model.ArchifactsCollectors.toUnmodifiableLinkedSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface HasArtifacts {

	Set<Artifact> getArtifacts();

	default Set<BuildingBlock> getBuildingBlocksOfType(final BuildingBlockType buildingBlockType) {
		return getBuildingBlocksOfTypes(buildingBlockType);
	}
	
	default Set<BuildingBlock> getBuildingBlocksOfTypes(final BuildingBlockType... buildingBlockTypes) {
		final List<BuildingBlockType> buildigBlockTypeList = Arrays.asList(buildingBlockTypes);
		return getArtifactsOfType(BuildingBlock.class)
				.stream()
				.filter(buildingBlock ->  buildigBlockTypeList.contains(buildingBlock.getType()))
				.collect(toUnmodifiableLinkedSet());
	}

	default Set<BuildingBlock> getBuildingBlocks() {
		return getArtifactsOfType(BuildingBlock.class);
	}

	default Set<MiscArtifact> getMiscArtifacts() {
		return getArtifactsOfType(MiscArtifact.class);
	}

	default Set<ExternalArtifact> getExternalArtifacts() {
		return getArtifactsOfType(ExternalArtifact.class);
	}

	private <T extends Artifact> Set<T> getArtifactsOfType(final Class<T> type) {
		return getArtifacts()
				.stream()
				.filter(type::isInstance)
				.map(type::cast)
				.collect(toUnmodifiableLinkedSet());
	}

}
