package org.archifacts.integration.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.Application;
import org.archifacts.integration.spring.domain.MyComponent;
import org.archifacts.integration.spring.domain.MyConfiguration;
import org.archifacts.integration.spring.domain.MyController;
import org.archifacts.integration.spring.domain.MyRepository;
import org.archifacts.integration.spring.domain.MyService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

@DisplayNameGeneration(ReplaceUnderscores.class)
final class SpringDescriptorTest {

	private static final JavaClasses DOMAIN = new ClassFileImporter().importPackages(MyConfiguration.class.getPackageName());

	@ParameterizedTest
	@MethodSource("getBuildingBlocks")
	void assertThat_building_blocks_are_recognized(final BuildingBlockDescriptor buildingBlockDescriptor, final Class<?>... matchingClasses) {
		final Application application = Application
				.builder()
				.descriptor(buildingBlockDescriptor)
				.buildApplication(DOMAIN);

		final Set<String> expectedClassNames = Arrays.stream(matchingClasses).map(Class::getName).collect(Collectors.toSet());

		assertThat(application.getBuildingBlocksOfType(buildingBlockDescriptor.type()))
				.map(b -> b.getJavaClass().getName())
				.allMatch(name -> expectedClassNames.contains(name));
	}

	private static Stream<Arguments> getBuildingBlocks() {
		return Stream.of(
				Arguments.of(SpringDescriptors.BuildingBlockDescriptors.ConfigurationDescriptor, new Class[] { MyConfiguration.class }),
				Arguments.of(SpringDescriptors.BuildingBlockDescriptors.ServiceDescriptor, new Class[] { MyService.class }),
				Arguments.of(SpringDescriptors.BuildingBlockDescriptors.RepositoryDescriptor, new Class[] { MyRepository.class }),
				Arguments.of(SpringDescriptors.BuildingBlockDescriptors.ControllerDescriptor, new Class[] { MyController.class }),
				Arguments.of(SpringDescriptors.BuildingBlockDescriptors.ComponentDescriptor,
						new Class[] { MyComponent.class, MyConfiguration.class, MyService.class, MyRepository.class, MyController.class }));
	}

}
