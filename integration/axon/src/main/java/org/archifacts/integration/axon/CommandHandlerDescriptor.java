package org.archifacts.integration.axon;

import java.util.stream.Stream;

import org.archifacts.core.descriptor.SourceBasedArtifactRelationshipDescriptor;
import org.archifacts.core.model.Artifact;
import org.archifacts.core.model.ArtifactRelationshipRole;
import org.axonframework.commandhandling.CommandHandler;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;

final class CommandHandlerDescriptor implements SourceBasedArtifactRelationshipDescriptor {

	private static final ArtifactRelationshipRole ROLE = ArtifactRelationshipRole.of("handles");

	CommandHandlerDescriptor() {
	}

	@Override
	public ArtifactRelationshipRole role() {
		return ROLE;
	}

	@Override
	public boolean isSource(final Artifact sourceCandidateArtifact) {
		return sourceCandidateArtifact.getJavaClass().getMethods()
				.stream()
				.anyMatch(this::isValidCommandHandler);
	}

	@Override
	public Stream<JavaClass> targets(final JavaClass sourceClass) {
		return sourceClass.getMethods()
				.stream()
				.filter(this::isValidCommandHandler)
				.map(method -> method.getRawParameterTypes().get(0));
	}

	private boolean isValidCommandHandler(final JavaMethod method) {
		return method.isAnnotatedWith(CommandHandler.class) && !method.getRawParameterTypes().isEmpty();
	}

}
