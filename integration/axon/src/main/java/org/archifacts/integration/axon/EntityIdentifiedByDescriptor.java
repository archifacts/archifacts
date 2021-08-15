package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.modelling.command.EntityId;

final class EntityIdentifiedByDescriptor extends AbstractIdentifiedByDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return EntityId.class;
	}

}
