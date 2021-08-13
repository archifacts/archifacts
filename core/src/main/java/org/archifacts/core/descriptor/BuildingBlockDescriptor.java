package org.archifacts.core.descriptor;

import java.lang.annotation.Annotation;

import org.archifacts.core.model.BuildingBlock;
import org.archifacts.core.model.BuildingBlockType;

import com.tngtech.archunit.core.domain.JavaClass;

/**
 * Describes a {@link BuildingBlock}.
 *
 * @author Oliver Libutzki
 * @see BuildingBlock
 *
 */
public interface BuildingBlockDescriptor {

	/**
	 * The type of the {@link BuildingBlock} which this descriptor describes.
	 *
	 * @return the type of the {@link BuildingBlock} which this descriptor
	 *         describes. Cannot be null.
	 */
	BuildingBlockType type();

	/**
	 * Evaluates if the given {@link JavaClass} is a {@link BuildingBlock} of
	 * the type which is returned by {@link #type()}.
	 *
	 * @param javaClass The {@link JavaClass} to be evaluated
	 * @return true, if the given {@link JavaClass} is a {@link BuildingBlock}
	 *         of type {@link #type()}, otherwise false.
	 */
	boolean isBuildingBlock(JavaClass javaClass);
	
	/**
	 * Convenient method to create a new descriptor matching all {@link JavaClass java classes} whose simple name ends with a given suffix.
	 * The suffix matching is case sensitive.
	 * 
	 * @param buildingBlockType The type of the {@link BuildingBlock} the new descriptor should describe. Must not be <code>null</code>.
	 * @param suffix The suffix the matching java classes should end with. Must not be <code>null</code>.
	 * 
	 * @return A new descriptor.
	 * 
	 * @throws NullPointerException If one of the parameter was <code>null</code>.
	 */
	static BuildingBlockDescriptor forSimpleNameEndingWith(final BuildingBlockType buildingBlockType, final String suffix) {
		return new BuildingBlockDescriptorForSimpleNameEndingWith(buildingBlockType, suffix);
	}
	
	/**
	 * Convenient method to create a new descriptor matching all {@link JavaClass java classes} that are assignable to at least one of the given types.
	 * 
	 * @param buildingBlockType The type of the {@link BuildingBlock} the new descriptor should describe. Must not be <code>null</code>.
	 * @param types The types the matching java classes should be assignable to. At least one type must be assignable in order to match. Must not be <code>null</code>.
	 * 
	 * @return A new descriptor.
	 * 
	 * @throws NullPointerException If one of the parameter was <code>null</code>.
	 */
	static BuildingBlockDescriptor forAssignableTo(final BuildingBlockType buildingBlockType, final Class<?>... types) {
		return new BuildingBlockDescriptorForAssignableTo(buildingBlockType, types);
	}
	
	/**
	 * Convenient method to create a new descriptor matching all {@link JavaClass java classes} that are annotated with the given annotation.
	 * 
	 * @param buildingBlockType The type of the {@link BuildingBlock} the new descriptor should describe. Must not be <code>null</code>.
	 * @param type The annotation with which the matching java classes should be annotated. Must not be <code>null</code>.
	 * 
	 * @return A new descriptor.
	 * 
	 * @throws NullPointerException If one of the parameter was <code>null</code>.
	 */
	static BuildingBlockDescriptor forAnnatatedWith(final BuildingBlockType buildingBlockType, final Class<? extends Annotation> type) {
		return new BuildingBlockDescriptorForAnnotatedWith(buildingBlockType, type);
	}
	
}
