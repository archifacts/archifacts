package org.archifacts.core.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClass;

public abstract class Artifact implements Named {

	private final JavaClass javaClass;

	private Optional<ArtifactContainer> container = Optional.empty();
	private final Set<ArtifactRelationship> outgoingRelationships = new LinkedHashSet<>();
	private final Set<ArtifactRelationship> incomingRelationships = new LinkedHashSet<>();

	Artifact(final JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	public Optional<ArtifactContainer> getContainer() {
		return container;
	}

	void setContainer(final ArtifactContainer container) {
		if (this.container.isPresent()) {
			throw new IllegalStateException(String.format("The container of the artifact %s has already been set.", this));
		}
		this.container = Optional.of(container);
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	void addOutgoingRelationship(final ArtifactRelationship relationship) {
		if (relationship.getSource() != this) {
			throw new IllegalStateException(String.format("The source of the given relationship does not match this artifact. Actual: %s, Expected: %s", relationship.getSource(), this));
		}
		outgoingRelationships.add(relationship);
	}

	void addIncomingRelationship(final ArtifactRelationship relationship) {
		if (relationship.getTarget() != this) {
			throw new IllegalStateException(String.format("The target of the given relationship does not match this artifact. Actual: %s, Expected: %s", relationship.getTarget(), this));
		}
		incomingRelationships.add(relationship);
	}

	@Override
	public String getName() {
		String name = javaClass.getSimpleName();
		if (name.isEmpty()) {
			final String fullyQualifiedName = javaClass.getFullName();
			final int indexOfDollarSign = fullyQualifiedName.lastIndexOf('$');
	        if (indexOfDollarSign > -1) {
	        	name = fullyQualifiedName.substring(indexOfDollarSign);
	        } else {
	        	name = fullyQualifiedName;
	        }
		}
		return name;
	}

	public Set<ArtifactRelationship> getIncomingRelationships() {
		return Collections.unmodifiableSet(incomingRelationships);
	}

	public Set<ArtifactRelationship> getOutgoingRelationships() {
		return Collections.unmodifiableSet(outgoingRelationships);
	}

	@Override
	public int hashCode() {
		return Objects.hash(javaClass.getName());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Artifact other = (Artifact) obj;
		return Objects.equals(javaClass.getName(), other.javaClass.getName());
	}
}
