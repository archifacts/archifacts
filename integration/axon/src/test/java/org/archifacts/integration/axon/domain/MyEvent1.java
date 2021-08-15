package org.archifacts.integration.axon.domain;

public final class MyEvent1 {
	
	private final MyAggregateRootId id;

	public MyEvent1(final MyAggregateRootId id) {
		this.id = id;
	}
	
	public MyAggregateRootId getId() {
		return id;
	}

}
