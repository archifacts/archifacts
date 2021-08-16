package org.archifacts.integration.jmolecules.domain;

import java.util.List;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

public class MyAggregateRoot implements AggregateRoot<MyAggregateRoot, MyAggregateRootId> {

	private List<MyEntity> entities;
	private Association<MyAggregateRoot, MyAggregateRootId> aggregates;
	
	@Override
	public MyAggregateRootId getId() {
		return new MyAggregateRootId();
	}

	public List<MyEntity> getEntities() {
		return entities;
	}
	
	public Association<MyAggregateRoot, MyAggregateRootId> getAggregates() {
		return aggregates;
	}
	
}
