package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *     This class represents ip addresses. Each {@link IPAddress} is immutable.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class IPAddress implements IAddress, Comparable<IPAddress> {

    private final long address;
    private final byte[] bytes;
    private final boolean isMulticast;
    private final String addressString;
    private final int hash;

    public IPAddress(final long address) throws InvalidIPAddress {

        if (address < 0)
            throw new InvalidIPAddress();

        if (address > 4294967296L)
            throw new InvalidIPAddress();

        this.address = address;
        hash = new Long(address).hashCode();

        // transform to byte array
        bytes = new byte[4];
        byte[] extractedBytes = ByteBuffer.allocate(8).putLong(address).array();
        System.arraycopy(extractedBytes, 4, bytes, 0, extractedBytes.length - 4);

        // set multicast bit
        isMulticast = (bytes[0] & 0xFF) >= 0b11100000 && (bytes[0] & 0xFF) <= 0b11101111;

        // set string representation
        final List<Byte> bytes = Arrays.asList(ArrayUtils.toObject(this.bytes));
        addressString = bytes.stream().map(byt -> String.format("%d", byt & 0xFF)).collect(Collectors.joining("."));
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(bytes, 4);
    }

    @Override
    public int size() {
       return 32;
    }

    @Override
    public boolean isMulticast() {
        return isMulticast;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IPAddress && address == ((IPAddress) other).address;
    }

    @Override
    public String toString() {
        return addressString;
    }

    @Override
    public int compareTo(IPAddress o) {
        return Long.signum(address - o.address);
    }
}
