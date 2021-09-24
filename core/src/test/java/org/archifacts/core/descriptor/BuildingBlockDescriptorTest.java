package org.archifacts.core.descriptor;

import static org.assertj.core.api.Assertions.assertThat;

import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.AnnotatedClass;
import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.Annotation;
import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.AnotherClass;
import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.ClassExtendingSuperClass;
import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.ClassWithSuffix;
import org.archifacts.core.descriptor.BuildingBlockDescriptorTest.Classes.SuperClass;
import org.archifacts.core.model.BuildingBlockType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class BuildingBlockDescriptorTest {

	final static class Classes {

		class ClassWithSuffix {

		}

		class AnotherClass {

		}

		class SuperClass {

		}

		class ClassExtendingSuperClass extends SuperClass {

		}

		@Annotation
		class AnnotatedClass {

		}

		@interface Annotation {

		}

	}

	@Test
	void assertThat_forSimpleNameEndingWith_matches_as_expected() {
		final BuildingBlockDescriptor buildingBlockDescriptor = BuildingBlockDescriptor.forSimpleNameEndingWith(BuildingBlockType.of("Test"), "Suffix");

		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(ClassWithSuffix.class))).isTrue();
		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(AnotherClass.class))).isFalse();
	}

	@Test
	void assertThat_forAssignableTo_matches_as_expected() {
		final BuildingBlockDescriptor buildingBlockDescriptor = BuildingBlockDescriptor.forAssignableTo(BuildingBlockType.of("Test"), SuperClass.class);

		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(ClassExtendingSuperClass.class))).isTrue();
		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(AnotherClass.class))).isFalse();
	}

	@Test
	void assertThat_forAssignableTo_matches_with_multiple_types_as_expected() {
		final BuildingBlockDescriptor buildingBlockDescriptor = BuildingBlockDescriptor.forAssignableTo(BuildingBlockType.of("Test"), SuperClass.class, ClassWithSuffix.class);

		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(ClassExtendingSuperClass.class))).isTrue();
		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(ClassWithSuffix.class))).isTrue();
		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(AnotherClass.class))).isFalse();
	}

	@Test
	void assertThat_forAnnatatedWith_matches_as_expected() {
		final BuildingBlockDescriptor buildingBlockDescriptor = BuildingBlockDescriptor.forAnnotatedWith(BuildingBlockType.of("Test"), Annotation.class);

		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(AnnotatedClass.class))).isTrue();
		assertThat(buildingBlockDescriptor.isBuildingBlock(importClass(AnotherClass.class))).isFalse();
	}

	private JavaClass importClass(Class<?> clazz) {
		return new ClassFileImporter().importClass(clazz);
	}

}
