package org.archifacts.integration.axon.domain;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.AggregateRoot;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

@AggregateRoot
public class MyAggregateRoot {

	@AggregateIdentifier
	private MyAggregateRootId id;
	
	@AggregateMember
	private MyAggregateMember myAggregateMember;
	
	@CommandHandler
	public MyAggregateRoot(final MyCommand1 command) {
		apply(new MyEvent1(command.getId()));
	}
	
	@EventSourcingHandler
	public void handle(final MyEvent1 event) {
		id = event.getId();
	}
	
	@CommandHandler
	public void handle(final MyCommand2 command) {
	}
	
}
