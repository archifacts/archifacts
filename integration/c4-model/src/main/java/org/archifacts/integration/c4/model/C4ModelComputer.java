package org.archifacts.integration.c4.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.archifacts.core.model.Archifact;

import com.structurizr.model.ModelItem;

public final class C4ModelComputer<ARCHIFACT extends Archifact> {

	private final Set<ModelItemComputationRule<ARCHIFACT>> computationRules = new HashSet<>();
	
	private final Map<ARCHIFACT, Set<ModelItem>> mappings = new ConcurrentHashMap<>();
	
	private final C4ModelLookup lookup;
	
	private final ComputationFunction<ARCHIFACT> defaultComputation;
	
	
	public C4ModelComputer(C4ModelLookup lookup, ComputationFunction<ARCHIFACT> defaultComputation) {
		this.lookup = lookup;
		this.defaultComputation = defaultComputation;
	}
	
	public void addComputationRule(ModelItemComputationRule<ARCHIFACT> computationRule) {
		computationRules.add(computationRule);
	}
	
	public Set<ModelItem> compute(ARCHIFACT archifact) {
		return mappings.computeIfAbsent(archifact, a -> 
			 computationRules.stream()
					.filter(rule -> rule.test(a))
					.collect(Collectors.<ComputationFunction<ARCHIFACT>>toOptionalSingleton())
					.orElse(defaultComputation)
					.apply(a, lookup)
		);

	}
	
	public Map<ARCHIFACT, Set<ModelItem>> getMappings() {
		return mappings;
	}
}
