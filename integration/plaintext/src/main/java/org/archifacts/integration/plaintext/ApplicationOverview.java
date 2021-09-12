package org.archifacts.integration.plaintext;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import org.archifacts.core.model.Application;

public final class ApplicationOverview {
    
    private final Application application;

	public ApplicationOverview(final Application application) {
		this.application = application;
	}
    
    /**
	 * Writes the current application overview into the given writer. The writer is neither flushed nor closed.
	 * 
	 * @param writer The writer. Must not be <code>null</code>.
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	public void writeToWriter(final Writer writer) throws IOException {
		Objects.requireNonNull(writer, "The writer must not be null.");
		writer.write(render());
	}

	public String render() {
		final StringBuilder stringBuilder = new StringBuilder();
		application.getContainers().forEach(container -> {
			stringBuilder.append(container).append('\n');
			stringBuilder.append("  Building Blocks:").append('\n');
			container.getBuildingBlocks().forEach(buildingBlock -> {
				stringBuilder.append("    ").append(buildingBlock).append('\n');
			});
			stringBuilder.append("  Miscellaneous Artifacts:").append('\n');
			container.getMiscArtifacts().forEach(miscArtifact -> {
				stringBuilder.append("    ").append(miscArtifact).append('\n');
			});
			stringBuilder.append("  External Artifacts:").append('\n');
			container.getExternalArtifacts().forEach(externalArtifact -> {
				stringBuilder.append("    ").append(externalArtifact).append('\n');
			});
		});
		stringBuilder.append("Artifacts without module:").append('\n');
		application.getArtifacts().stream()
				.filter(artifact -> artifact.getContainer().isEmpty())
				.forEach(artifact -> stringBuilder.append("    ").append(artifact).append('\n'));
		stringBuilder.append("Relationships:").append('\n');
		application.getRelationships().forEach(relationship -> {
			stringBuilder.append("  ").append(relationship.getSource()).append(" ").append(relationship.getRole()).append(" ").append(relationship.getTarget()).append('\n');
		});
		return stringBuilder.toString();
	}
}
