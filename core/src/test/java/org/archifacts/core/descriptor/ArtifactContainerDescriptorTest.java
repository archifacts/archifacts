package org.archifacts.core.descriptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.regex.PatternSyntaxException;

import org.archifacts.core.descriptor.ArtifactContainerDescriptorTest.Classes.ClassA;
import org.archifacts.core.descriptor.ArtifactContainerDescriptorTest.Classes.ClassB;
import org.archifacts.core.model.ArtifactContainerType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration( ReplaceUnderscores.class )
final class ArtifactContainerDescriptorTest {

	final static class Classes {

		class ClassA {

		}

		class ClassB {

		}
		
	}

	@Test
	void assertThat_forFullQualifiedNameContaining_matches_simple_name_as_expected() {
		final ArtifactContainerDescriptor artifactContainerDescriptor = ArtifactContainerDescriptor.forFullQualifiedNameContaining(ArtifactContainerType.of("Test"), "ClassA", "A");
	
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassA.class))).contains("A");
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassB.class))).isEmpty();
	}
	
	@Test
	void assertThat_forFullQualifiedNameContaining_matches_full_qualified_name_as_expected() {
		final ArtifactContainerDescriptor artifactContainerDescriptor = ArtifactContainerDescriptor.forFullQualifiedNameContaining(ArtifactContainerType.of("Test"), ".descriptor.", "Descriptors");
	
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassA.class))).contains("Descriptors");
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassB.class))).contains("Descriptors");
	}
	
	@Test
	void assertThat_forFullQualifiedNameMatching_matches_as_expected() {
		final ArtifactContainerDescriptor artifactContainerDescriptor = ArtifactContainerDescriptor.forFullQualifiedNameMatching(ArtifactContainerType.of("Test"), ".*\\.descriptor\\..*ClassA", "A");
	
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassA.class))).contains("A");
		assertThat(artifactContainerDescriptor.containerNameOf(importClass(ClassB.class))).isEmpty();
	}
	
	@Test
	void assertThat_forFullQualifiedNameMatching_cannot_be_created_with_invalid_pattern() {
		assertThatExceptionOfType(PatternSyntaxException.class).isThrownBy(() -> ArtifactContainerDescriptor.forFullQualifiedNameMatching(ArtifactContainerType.of("Test"), "(", "A"));
	}
	
	private JavaClass importClass( Class<?> clazz ) {
		return new ClassFileImporter().importClass(clazz);
	}

}