package org.archifacts.core.descriptor;

import org.archifacts.core.model.BuildingBlockType;
import org.archifacts.core.model.BuildingBlock;

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
}
