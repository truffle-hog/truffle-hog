package edu.kit.trufflehog.model.network;

import java.nio.ByteBuffer;

/**
 * @author Mark Giraud
 * @version 0.1
 */
public class IPAddress implements IAddress {

    private final long address;
    private final byte[] bytes;

    public IPAddress(final long address) throws InvalidIPAddress {

        if (address == 0)
            throw new InvalidIPAddress();

        if (address > 4294967295L)
            throw new InvalidIPAddress();

        this.address = address;

        this.bytes = new byte[4];
        byte[] bytes = ByteBuffer.allocate(8).putLong(address).array();
        System.arraycopy(bytes, 4, this.bytes, 0, bytes.length - 4);
    }

    @Override
    public byte[] toByteArray() {
        return bytes;
    }

    @Override
    public int size() {
       return 32;
    }

    @Override
    public boolean isMulticast() {
        return (bytes[0] & 0xFF) >= 0b11100000 && (bytes[0] & 0xFF) <= 0b11101111;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IPAddress && address == ((IPAddress) other).address;
    }

    @Override
    public String toString() {

        return "" +
                (bytes[0] & 0xFF) +
                "." +
                (bytes[1] & 0xFF) +
                "." +
                (bytes[2] & 0xFF) +
                "." +
                (bytes[3] & 0xFF);
    }
}
