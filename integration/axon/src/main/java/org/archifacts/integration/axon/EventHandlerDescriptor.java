package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.eventhandling.EventHandler;

final class EventHandlerDescriptor extends AbstractHandlerDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("handles");

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return EventHandler.class;
	}

}
