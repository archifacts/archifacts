package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.axonframework.messaging.Message;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

abstract class AbstractHandlerDescriptor implements SourceBasedArtifactRelationshipDescriptor {

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
				.map(javaType -> toJavaClass(javaType));
	}

	private boolean isValidHandler(final JavaCodeUnit codeUnit) {
		return codeUnit.isMetaAnnotatedWith(getAnnotationClass()) && !codeUnit.getParameterTypes().isEmpty();
	}

	private JavaClass toJavaClass(final JavaType javaType) {
		final JavaClass javaClass = javaType.toErasure();
		
		if (javaClass.isAssignableTo(Message.class) && javaType instanceof JavaParameterizedType) {
			final JavaParameterizedType javaParameterizedType = (JavaParameterizedType)javaType;
			return javaParameterizedType.getActualTypeArguments().get(0).toErasure();
		}
		
		return javaClass;
	}
	
	protected abstract Class<? extends Annotation> getAnnotationClass();

}
