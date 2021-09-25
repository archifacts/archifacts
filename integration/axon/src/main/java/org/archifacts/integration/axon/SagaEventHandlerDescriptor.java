package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.modelling.saga.SagaEventHandler;

final class SagaEventHandlerDescriptor extends AbstractHandlerDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("handles");

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return SagaEventHandler.class;
	}

}
