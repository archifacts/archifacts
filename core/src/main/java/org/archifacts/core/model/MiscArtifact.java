package org.archifacts.core.model;

import com.tngtech.archunit.core.domain.JavaClass;

public final class MiscArtifact extends Artifact {

	public MiscArtifact(final JavaClass javaClass) {
		super(javaClass);
	}

	@Override
	public String toString() {
		return "<MiscArtifact> " + getJavaClass().getName();
	}

}
