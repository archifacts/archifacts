package org.archifacts.integration.axon;

import java.util.Collection;
import java.util.Map;

import org.archifacts.core.descriptor.BuildingBlockDescriptor;
import org.archifacts.core.model.BuildingBlockType;
import org.axonframework.modelling.command.AggregateMember;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.domain.JavaType;

final class EntityDescriptor implements BuildingBlockDescriptor{

	private static final BuildingBlockType TYPE = BuildingBlockType.of("Entity");

	@Override
	public BuildingBlockType type() {
		return TYPE;
	}

	@Override
	public boolean isBuildingBlock(JavaClass javaClass) {
		return javaClass.getDirectDependenciesToSelf()
			.stream()
			.flatMap(dep -> dep.getOriginClass().getMembers().stream())
			.filter(field -> field.isMetaAnnotatedWith(AggregateMember.class))
			.map(this::getType)
			.filter(this::isNotUntypedCollectionOrMap)
			.map(this::getJavaClass)
			.anyMatch(javaClass::equals);
	}
	
	private JavaType getType(final JavaMember javaMember) {
		if (javaMember instanceof JavaField) {
			return ((JavaField) javaMember).getType();
		} else if (javaMember instanceof JavaMethod) {
			return ((JavaMethod) javaMember).getReturnType();
		} else {
			throw new IllegalArgumentException(String.format("A JavaMember (%s) annotated with '%s' is neither a field nor a method.", javaMember, AggregateMember.class.getSimpleName()));
		}
	}

	private JavaClass getJavaClass(final JavaType type) {
		final JavaClass erasure = type.toErasure();

		if (type instanceof JavaParameterizedType && erasure.isAssignableTo(Collection.class)) {
			final JavaType collectionType = ((JavaParameterizedType) type).getActualTypeArguments().get(0);
			return collectionType.toErasure();
		}
		if (type instanceof JavaParameterizedType && erasure.isAssignableTo(Map.class)) {
			final JavaType valueType = ((JavaParameterizedType) type).getActualTypeArguments().get(1);
			return valueType.toErasure();
		}

		return erasure;
	}
	
	private boolean isNotUntypedCollectionOrMap(final JavaType type) {
		final boolean isUntypedCollection = type.toErasure().isAssignableTo(Collection.class) && !(type instanceof JavaParameterizedType);
		final boolean isUntypedMap = type.toErasure().isAssignableTo(Map.class) && !(type instanceof JavaParameterizedType);
		return !(isUntypedCollection || isUntypedMap);
	}

}
