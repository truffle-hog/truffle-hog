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

package edu.kit.trufflehog.service.packetdataprocessor;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;

/**
 * <p>
 *     This Interface provides the default methods for any incoming packet data which is received by e.g.
 *     a {@link TruffleReceiver}
 *     and put into a class (e.g a {@link Truffle}) that implements this interface.
 * </p>
 */
public interface IPacketData {

    /**
     * <p>
     *     This method gets an attribute of the packet data object using the specified attribute name.
     * </p>
     * <p>
     *     The function should always return a valid element of the specified type. Each implementation has to make
     *     sure this rule is never violated. In case no element was found under the specified identifier, null is
     *     returned.
     * </p>
     *
     * @param attributeType The type of attribute that is supposed to be retrieved, for example Integer.class
     * @param attributeIdentifier The string identifier of the attribute that should be retrieved.
     * @param <T> The type of the attribute that is retrieved.
     * @return The value of the attribute or null if nothing was found under the specified identifier
     */
    <T> T getAttribute(final Class<T> attributeType, final String attributeIdentifier);
}
