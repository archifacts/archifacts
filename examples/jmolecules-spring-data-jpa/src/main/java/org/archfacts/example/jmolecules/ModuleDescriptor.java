package org.archfacts.example.jmolecules;

import java.util.Optional;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.model.ArtifactContainerType;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaPackage;

final class ModuleDescriptor implements ArtifactContainerDescriptor {

	private static final ArtifactContainerType TYPE = ArtifactContainerType.of("Module");

	private final String basePackage;

	ModuleDescriptor(final String basePackage) {
		this.basePackage = basePackage;
	}

	@Override
	public Optional<String> containerNameOf(final JavaClass javaClass) {
		return moduleOf(javaClass.getPackage());
	}

	private Optional<String> moduleOf(final JavaPackage javaPackage) {
		return toJavaOptional(javaPackage.getParent()).map(parent -> {
			if (basePackage.equals(parent.getName())) {
				return Optional.of(javaPackage.getRelativeName());
			} else {
				return moduleOf(parent);
			}
		}).orElse(Optional.<String>empty());
	}

	private <T> Optional<T> toJavaOptional(final com.tngtech.archunit.base.Optional<T> optional) {
		return optional.transform(Optional::of).or(Optional.empty());
	}

	@Override
	public ArtifactContainerType type() {
		return TYPE;
	}
}
