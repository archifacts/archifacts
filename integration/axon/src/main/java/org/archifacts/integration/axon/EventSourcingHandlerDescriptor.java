package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.eventsourcing.EventSourcingHandler;

final class EventSourcingHandlerDescriptor extends AbstractHandlerDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return EventSourcingHandler.class;
	}

}
