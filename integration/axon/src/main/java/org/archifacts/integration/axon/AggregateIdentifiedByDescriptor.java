package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.modelling.command.AggregateIdentifier;

final class AggregateIdentifiedByDescriptor extends AbstractIdentifiedByDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return AggregateIdentifier.class;
	}

}

