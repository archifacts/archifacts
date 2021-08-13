package org.archifacts.integration.axon.domain;

import org.axonframework.eventhandling.EventHandler;

public final class MyEventHandler {

	@EventHandler
	public void handle(MyEvent1 event) {
	}
	
}
