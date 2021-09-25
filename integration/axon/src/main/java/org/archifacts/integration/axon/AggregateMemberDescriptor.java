package org.archifacts.integration.axon;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.modelling.command.AggregateMember;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

final class AggregateMemberDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("has aggregate member");

	AggregateMemberDescriptor() {

	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return getAggregateMembersOrFields(sourceCandidateArtifact.getJavaClass()).anyMatch(x -> true);
	}

	private Stream<JavaMember> getAggregateMembersOrFields(final JavaClass sourceClass) {
		return sourceClass.getMembers()
				.stream()
				.filter(m -> m.isMetaAnnotatedWith(AggregateMember.class) && isNotUntypedCollectionOrMap(getType(m)));
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return getAggregateMembersOrFields(sourceClass)
				.map(m -> getType(m))
				.map(t -> getJavaClass(t));
	}

	private JavaType getType(final JavaMember javaMember) {
		if (javaMember instanceof JavaField) {
			return ((JavaField) javaMember).getType();
		} else if (javaMember instanceof JavaMethod) {
			return ((JavaMethod) javaMember).getReturnType();
		} else {
			throw new IllegalArgumentException(String.format("A JavaMember (%s) annotated with '%s' is neither a field nor a method.", javaMember, AggregateMember.class.getSimpleName()));
		}
	}

	private JavaClass getJavaClass(final JavaType type) {
		final JavaClass erasure = type.toErasure();

		if (type instanceof JavaParameterizedType && erasure.isAssignableTo(Collection.class)) {
			final JavaType collectionType = ((JavaParameterizedType) type).getActualTypeArguments().get(0);
			return collectionType.toErasure();
		}
		if (type instanceof JavaParameterizedType && erasure.isAssignableTo(Map.class)) {
			final JavaType valueType = ((JavaParameterizedType) type).getActualTypeArguments().get(1);
			return valueType.toErasure();
		}

		return erasure;
	}

	private boolean isNotUntypedCollectionOrMap(final JavaType type) {
		final boolean isUntypedCollection = type.toErasure().isAssignableTo(Collection.class) && !(type instanceof JavaParameterizedType);
		final boolean isUntypedMap = type.toErasure().isAssignableTo(Map.class) && !(type instanceof JavaParameterizedType);
		return !(isUntypedCollection || isUntypedMap);
	}

}
