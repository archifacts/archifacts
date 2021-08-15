package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.modelling.saga.SagaEventHandler;

final class SagaEventHandlerDescriptor extends AbstractHandlerDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return SagaEventHandler.class;
	}

}
