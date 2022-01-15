package org.archifacts.integration.c4.model;

import java.util.Set;
import java.util.function.Predicate;

import org.archifacts.core.model.Archifact;

import com.structurizr.model.ModelItem;

public final class ModelItemComputationRule<ARCHIFACT extends Archifact> implements Predicate<ARCHIFACT>, ComputationFunction<ARCHIFACT> {

	private final Predicate<ARCHIFACT> predicate;
	private final ComputationFunction<ARCHIFACT> computation;

	ModelItemComputationRule(final Predicate<ARCHIFACT> predicate, final ComputationFunction<ARCHIFACT> computation) {
		this.predicate = predicate;
		this.computation = computation;
	}

	@Override
	public boolean test(final ARCHIFACT archifact) {
		return predicate.test(archifact);
	}

	@Override
	public Set<ModelItem> apply(final ARCHIFACT archifact, final C4ModelLookup lookup) {
		return computation.apply(archifact, lookup);
	}

}
