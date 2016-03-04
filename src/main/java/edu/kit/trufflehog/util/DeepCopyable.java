package edu.kit.trufflehog.util;

/**
 * This interface must be implmented by classes that support a DeepCopy operation. A Deep copy means, that, if this
 * class is mutable, all references that are mutable are copied and returned in a new instance of this class.
 *
 * If this class is Immutable and implementing this interface, there does not have to be done any copy operations,
 * just return a reference to this class object.
 *
 * @param <T> The type of the class that implements this interface, making it support the createDeepCopy operation.
 */
public interface DeepCopyable<T> extends Mutability {

    /**
     * Creates a copy of this class while also copying all of the references that are mutable. Immutable references
     * are just included by their references, as the cannot change through the lifetime of the program.
     * @return A deep copy of this INode
     */
    T createDeepCopy();
}
