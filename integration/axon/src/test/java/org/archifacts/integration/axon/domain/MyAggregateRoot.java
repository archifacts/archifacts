package org.archifacts.integration.axon.domain;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.Map;
import java.util.Set;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.AggregateRoot;

@AggregateRoot
public class MyAggregateRoot {

	@AggregateIdentifier
	private MyAggregateRootId id;
	
	@AggregateMember
	private MyAggregateMember1 myAggregateMember1;
	
	@AggregateMember
	private Map<String, MyAggregateMember2> myAggregateMember2;
	
	@AggregateMember
	private Set<MyAggregateMember3> myAggregateMember3;
	
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
