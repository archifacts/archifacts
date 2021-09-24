package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;

import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.modelling.command.AggregateIdentifier;

final class AggregateIdentifiedByDescriptor extends AbstractIdentifiedByDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("identified by");

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return AggregateIdentifier.class;
	}

}
