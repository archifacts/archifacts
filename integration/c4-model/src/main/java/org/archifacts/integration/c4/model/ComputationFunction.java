package org.archifacts.integration.c4.model;

import java.util.Set;
import java.util.function.BiFunction;

import org.archifacts.core.model.Archifact;

import com.structurizr.model.ModelItem;

public interface ComputationFunction<ARCHIFACT extends Archifact> extends BiFunction<ARCHIFACT, C4ModelLookup, Set<ModelItem>>{

}
