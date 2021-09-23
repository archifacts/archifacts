package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.modelling.command.EntityId;

final class EntityIdentifiedByDescriptor extends AbstractIdentifiedByDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("identified by");

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}
	
	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return EntityId.class;
	}

}
