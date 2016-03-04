package edu.kit.trufflehog.util;

/**
 * This interface has the sole purpose to signal a programmer if a certain class is mutable or not.
 * There is a strict contract to be fullfilled for classes implementing this interface:
 *
 * If a class is Immutable the function isMutable() has to return \code false \endcode and the class has to
 * use the Annotation {@link org.apache.http.annotation.Immutable}.
 *
 * If a class is mutable the function has to return \code true \endcode.
 *
 * @author Jan Hermes
 */
public interface Mutability {

    /**
     * @return \code true \endcode if this class is mutable, \code false \endcode otherwise.
     */
    boolean isMutable();
}
