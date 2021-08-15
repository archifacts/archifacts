package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.eventhandling.EventHandler;

final class EventHandlerDescriptor extends AbstractHandlerDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return EventHandler.class;
	}

}
