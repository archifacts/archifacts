package org.archifacts.integration.axon;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.eventsourcing.EventSourcingHandler;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;

final class EventSourcingHandlerDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("handles");

	EventSourcingHandlerDescriptor() {
	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact.getJavaClass().getMethods()
				.stream()
				.anyMatch(this::isValidEventHandler);
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return sourceClass.getMethods()
				.stream()
				.filter(this::isValidEventHandler)
				.map(method -> method.getRawParameterTypes().get(0));
	}

	private boolean isValidEventHandler(final JavaMethod method) {
		return method.isAnnotatedWith(EventSourcingHandler.class) && !method.getRawParameterTypes().isEmpty();
	}

}
