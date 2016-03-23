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
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * \brief
 * \details
 * \date 19.02.16
 * \copyright GNU Public License
 * <p>
 *     This class represents a MAC address.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class MacAddress implements IAddress {

    private final byte[] bytes;
    private final long address;
    private final String addressString;
    private final boolean isMulticast;
    private final int hashcode;
    public MacAddress(long address) throws InvalidMACAddress {

        this.address = address;

        if (this.address > 0xFFFFFFFFFFFFL || this.address < 0) {
            throw new InvalidMACAddress(address);
        }

        hashcode = (new Long(address)).hashCode();
        // transform to byte array
        final byte[] extractedBytes = ByteBuffer.allocate(8).putLong(address).array();
        bytes = Arrays.copyOfRange(extractedBytes, 2, 8);

        // set multicast bit
        isMulticast = (bytes[0] & 1) == 1;

        // set string representation
        final List<Byte> bytes = Arrays.asList(ArrayUtils.toObject(toByteArray()));
        addressString = bytes.stream().map(b -> String.format("%02x", b)).collect(Collectors.joining(":"));
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(bytes, 6);
    }

    @Override
    public int size() {
        return 48;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof MacAddress) && (address == ((MacAddress)other).address);
    }

    @Override
    public boolean isMulticast() {
        return isMulticast;
    }

    @Override
    public String toString() {
        return addressString;
    }
}
