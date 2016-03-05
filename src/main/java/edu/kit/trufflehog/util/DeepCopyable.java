/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.util;

/**
 * This interface must be implemented by classes that support a createDeepCopy operation. A Deep copy means, that, if this
 * class is mutable, all references that are mutable are copied and returned in a new instance of this class.
 *
 * If this class is Immutable and implementing this interface, there does not have to be done any copy operations,
 * just return a reference to this class object.
 *
 * @param <T> The type of the class that implements this interface, making it support the createDeepCopy operation.
 *
 * @author Jan Hermes
 */
public interface DeepCopyable<T> extends Mutability {

    /**
     * Creates a copy of this class while also copying all of the references that are mutable. Immutable references
     * are just included by their references, as the cannot change through the lifetime of the program.
     *
     * @return A deep copy of this INode
     */
    T createDeepCopy(ICopyCreator copyCreator);
}
