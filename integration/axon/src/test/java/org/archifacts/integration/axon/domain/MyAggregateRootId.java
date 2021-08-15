package org.archifacts.integration.axon.domain;

public final class MyAggregateRootId {

	private final String value;

	public MyAggregateRootId(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
