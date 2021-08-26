package org.archifacts.integration.axon.domain;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;

public final class MyEventHandler {

	@EventHandler
	public void handle(final MyEvent1 event) {
	}
	
	@EventHandler
	public void handle(final EventMessage<MyEvent2> event) {
	}
	
}
