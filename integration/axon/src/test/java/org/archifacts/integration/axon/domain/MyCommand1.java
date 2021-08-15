package org.archifacts.integration.axon.domain;

public final class MyCommand1 {
	
	private final MyAggregateRootId id;

	public MyCommand1(final MyAggregateRootId id) {
		this.id = id;
	}
	
	public MyAggregateRootId getId() {
		return id;
	}

}
