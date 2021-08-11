package org.archifacts.integration.c4.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.archifacts.core.descriptor.ArtifactContainerDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.core.model.ArtifactContainerType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class C4ModelTransformerTest {

	static final class Classes1 {
		
		static final class UniqueClass {
		}
		
		static final class AmbiguousClass {
		}
		
		static Object createAnonymousClass() {
			return new Object() {
			};
		}
		
	}
	
	static final class Classes2 {
		
		static final class AmbiguousClass {
		}
		
		static Object createAnonymousClass() {
			return new Object() {
			};
		}
		
	}
	
	static final class ClassesSeparatingArtifactContainerDescriptor implements ArtifactContainerDescriptor {

		@Override
		public ArtifactContainerType type() {
			return ArtifactContainerType.of("Test");
		}

		@Override
		public Optional<String> containerNameOf(final JavaClass javaClass) {
			if (javaClass.getFullName().contains(Classes1.class.getSimpleName())) {
				return Optional.of("Classes-1");
			} else {
				return Optional.of("Classes-2");
			}
		}
		
	}
	
	@Test
	void assert_that_container_can_contain_classes_with_ambigous_names() {
		final Application application = Application
				.builder()
				.buildApplication(importClasses(Classes1.AmbiguousClass.class, Classes2.AmbiguousClass.class));
		
		final C4ModelTransformer transformer = createTransformer( application);
		assertThat(transformer.getComponents(a -> true)).map(Component::getName).containsExactlyInAnyOrder(Classes1.AmbiguousClass.class.getName(), Classes2.AmbiguousClass.class.getName());
	}
	
	@Test
	void assert_that_different_containers_can_contain_classes_with_ambigous_names() {
		final Application application = Application
				.builder()
				.addContainerDescriptor(new ClassesSeparatingArtifactContainerDescriptor())
				.buildApplication(importClasses(Classes1.AmbiguousClass.class, Classes2.AmbiguousClass.class));
		
		final C4ModelTransformer transformer = createTransformer( application);
		assertThat(transformer.getComponents(a -> true)).map(Component::getName).containsExactlyInAnyOrder(Classes1.AmbiguousClass.class.getSimpleName(), Classes2.AmbiguousClass.class.getSimpleName());
	}
	
	@Test
	void assert_that_container_can_contain_classes_with_unique_names() {
		final Application application = Application
				.builder()
				.buildApplication(importClasses(Classes1.UniqueClass.class));
		
		final C4ModelTransformer transformer = createTransformer( application);
		assertThat(transformer.getComponents(a -> true)).map(Component::getName).containsExactlyInAnyOrder(Classes1.UniqueClass.class.getSimpleName());
	}
	
	@Test
	void assert_that_transformer_can_handle_anonymous_classes() {
		final Application application = Application
				.builder()
				.buildApplication(importClasses(Classes1.createAnonymousClass().getClass()));
		
		final C4ModelTransformer transformer = createTransformer(application);
		assertThat(transformer.getComponents(a -> true)).map(Component::getName).containsExactly("$1");
	}
	
	@Test
	void assert_that_container_can_contain_anonymous_classes_with_ambigous_names() {
		final Application application = Application
				.builder()
				.buildApplication(importClasses(Classes1.createAnonymousClass().getClass(), Classes2.createAnonymousClass().getClass()));
		
		final C4ModelTransformer transformer = createTransformer( application);
		assertThat(transformer.getComponents(a -> true)).map(Component::getName).containsExactlyInAnyOrder(Classes1.class.getName() + "$1", Classes2.class.getName() + "$1");
	}
	
	private JavaClasses importClasses(final Class<?> ...classes) {
		return new ClassFileImporter().importClasses(classes);
	}
	
	private C4ModelTransformer createTransformer( final Application application) {
		final Workspace workspace = new Workspace("Test", null);
		final Model model = workspace.getModel();
		final SoftwareSystem softwareSystem = model.addSoftwareSystem("Test");
		return new C4ModelTransformer(application, softwareSystem);
	}
	
}
