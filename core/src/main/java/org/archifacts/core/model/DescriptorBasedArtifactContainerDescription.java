package org.archifacts.core.model;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;

class DescriptorBasedArtifactContainerDescription extends ArtifactContainerDescription {

	private final ArtifactContainerDescriptor descriptor;

	DescriptorBasedArtifactContainerDescription(final ArtifactContainerDescriptor descriptor, final String name) {
		super(descriptor.type(), name);
		this.descriptor = descriptor;
	}

	ArtifactContainerDescriptor getDescriptor() {
		return descriptor;
	}

}
