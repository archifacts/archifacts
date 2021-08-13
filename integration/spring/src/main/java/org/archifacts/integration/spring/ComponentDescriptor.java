package org.archifacts.integration.spring;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.tngtech.archunit.core.domain.JavaClass;

final class ComponentDescriptor implements BuildingBlockDescriptor {

	private static final Class<?>[] EXCLUDES = {Configuration.class, Repository.class, Service.class, Controller.class};
	private static final BuildingBlockType TYPE = BuildingBlockType.of("Component");

	ComponentDescriptor() {

	}

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(final JavaClass javaClass) {
		return javaClass.isMetaAnnotatedWith(Component.class) && notMetaAnnotatedWithExcludedAnnotations(javaClass);
	}

	@SuppressWarnings( "unchecked" )
	private boolean notMetaAnnotatedWithExcludedAnnotations(final JavaClass javaClass) {
		return Stream.of(EXCLUDES)
				.map(c -> (Class<? extends Annotation>) c)
				.noneMatch(javaClass::isMetaAnnotatedWith);
	}

}
