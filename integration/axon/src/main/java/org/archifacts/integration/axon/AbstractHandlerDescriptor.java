package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.messaging.Message;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;

abstract class AbstractHandlerDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("handles");
	
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
				.map(method -> method.getRawParameterTypes().get(0));
	}
	
	private boolean isValidHandler(final JavaCodeUnit codeUnit) {
		return codeUnit.isMetaAnnotatedWith(getAnnotationClass()) && !codeUnit.getRawParameterTypes().isEmpty() && !codeUnit.getRawParameterTypes().get(0).isAssignableTo(Message.class);
	}
	
	protected abstract Class<? extends Annotation> getAnnotationClass();

}
