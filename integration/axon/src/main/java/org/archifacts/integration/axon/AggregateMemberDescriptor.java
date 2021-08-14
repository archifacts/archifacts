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
				.filter(m -> m.isMetaAnnotatedWith(AggregateMember.class) && !getType(m).isAssignableTo(Collection.class) && !getType(m).isAssignableTo(Map.class));
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return getAggregateMembersOrFields(sourceClass)
				.map(m -> getType(m));
	}

	private JavaClass getType(final JavaMember javaMember) {
		if (javaMember instanceof JavaField) {
			return ((JavaField) javaMember).getRawType();
		} else if (javaMember instanceof JavaMethod) {
			return ((JavaMethod) javaMember).getRawReturnType();
		} else {
			throw new IllegalArgumentException(String.format("A JavaMember (%s) annotated with '%s' is neither a field nor a method.", javaMember, AggregateMember.class.getSimpleName()));
		}
	}

}
