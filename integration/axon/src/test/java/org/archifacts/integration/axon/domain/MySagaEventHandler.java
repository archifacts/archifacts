package org.archifacts.integration.axon.domain;

import org.axonframework.modelling.saga.SagaEventHandler;

public final class MySagaEventHandler {

	@SagaEventHandler(associationProperty = "id")
	public void handle(final MyEvent1 event) {
	}

}
