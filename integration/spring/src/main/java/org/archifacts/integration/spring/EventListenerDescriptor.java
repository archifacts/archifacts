package org.archifacts.integration.spring;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.springframework.context.event.EventListener;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaType;

final class EventListenerDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("listens to");

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact.getJavaClass().getCodeUnits()
				.stream()
				.anyMatch(this::isValidHandler);
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return sourceClass.getCodeUnits()
				.stream()
				.filter(this::isValidHandler)
				.map(method -> method.getParameterTypes().get(0))
				.map(JavaType::toErasure);
	}

	private boolean isValidHandler(final JavaCodeUnit codeUnit) {
		return codeUnit.isMetaAnnotatedWith(EventListener.class) && !codeUnit.getParameterTypes().isEmpty();
	}

}
