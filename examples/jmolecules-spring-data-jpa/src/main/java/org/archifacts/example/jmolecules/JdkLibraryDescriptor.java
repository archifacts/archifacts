package org.archifacts.example.jmolecules;

import java.util.Optional;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;

final class JdkLibraryDescriptor implements ArtifactContainerDescriptor {

	static final ArtifactContainerType TYPE = ArtifactContainerType.of("Library");

	static final JdkLibraryDescriptor INSTANCE = new JdkLibraryDescriptor();

	private JdkLibraryDescriptor() {

	}

	@Override
	public Optional<String> containerNameOf(final JavaClass javaClass) {
		if (javaClass.getPackageName().startsWith("java.")) {
			return Optional.of("jdk");
		} else {
			return Optional.empty();
		}
	}

	@Override
	public ArtifactContainerType type() {
		return TYPE;
	}

}