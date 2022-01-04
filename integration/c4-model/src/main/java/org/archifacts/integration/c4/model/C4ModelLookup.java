package org.archifacts.integration.c4.model;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactContainer;
import org.archifacts.core.model.ArtifactRelationship;

public interface C4ModelLookup {

	SoftwareSystem softwareSystem();
	Component component(Artifact artifact);
	Container container(ArtifactContainer artifactContainer);
	Relationship relationship(ArtifactRelationship artifactRelationship);
}
