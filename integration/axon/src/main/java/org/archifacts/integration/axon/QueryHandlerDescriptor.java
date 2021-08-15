package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.queryhandling.QueryHandler;

final class QueryHandlerDescriptor extends AbstractHandlerDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return QueryHandler.class;
	}

}
