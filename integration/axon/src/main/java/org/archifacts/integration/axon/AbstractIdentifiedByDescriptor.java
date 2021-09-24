package org.archifacts.integration.axon;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaMethod;

abstract class AbstractIdentifiedByDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return getIdentifierFieldOrMethod(sourceCandidateArtifact.getJavaClass()).findAny().isPresent();
	}

	private Stream<JavaMember> getIdentifierFieldOrMethod(final JavaClass sourceClass) {
		return sourceClass.getMembers()
				.stream()
				.filter(f -> f.isMetaAnnotatedWith(getAnnotationClass()));
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return getIdentifierFieldOrMethod(sourceClass)
				.map(m -> getType(m));
	}

	private JavaClass getType(final JavaMember javaMember) {
		if (javaMember instanceof JavaField) {
			return ((JavaField) javaMember).getRawType();
		} else if (javaMember instanceof JavaMethod) {
			return ((JavaMethod) javaMember).getRawReturnType();
		} else {
			throw new IllegalArgumentException(String.format("A JavaMember (%s) annotated with '%s' is neither a field nor a method.", javaMember, getAnnotationClass().getSimpleName()));
		}
	}

	protected abstract Class<? extends Annotation> getAnnotationClass();

}
