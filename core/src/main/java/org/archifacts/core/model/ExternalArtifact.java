package org.archifacts.core.model;

import com.tngtech.archunit.core.domain.JavaClass;

public final class ExternalArtifact extends Artifact {

	public ExternalArtifact(final JavaClass javaClass) {
		super(javaClass);
	}

	@Override
	public String toString() {
		return "<ExternalArtifact> " + getJavaClass().getName();
	}

}
