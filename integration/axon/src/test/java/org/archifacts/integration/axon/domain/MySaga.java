package org.archifacts.integration.axon.domain;

import org.axonframework.modelling.saga.StartSaga;

public final class MySaga {
	
	@StartSaga
	public void handle(final MyEvent1 event) {
	}

}
