package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.axonframework.commandhandling.CommandHandler;

final class CommandHandlerDescriptor extends AbstractHandlerDescriptor {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return CommandHandler.class;
	}

}
