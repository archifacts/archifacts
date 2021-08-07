package org.archifacts.core.model;

import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Set;

public interface HasArtifacts {

	Set<Artifact> getArtifacts();

	default Set<BuildingBlock> getArtifactsOfType(final BuildingBlockType buildingBlockType) {
		return getArtifactsOfType(BuildingBlock.class)
				.stream()
				.filter(buildingBlock -> buildingBlock.getType().equals(buildingBlockType))
				.collect(toCollection(LinkedHashSet::new));
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
				.collect(toCollection(LinkedHashSet::new));
	}

}
