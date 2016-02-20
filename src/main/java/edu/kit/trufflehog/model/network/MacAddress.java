package edu.kit.trufflehog.model.network;

import java.nio.ByteBuffer;

/**
 * Created by jan on 19.02.16.
 */
public class MacAddress implements IAddress {

    private final long addressLong;

    private final byte[] addressByteArray;

 //   private final String addressString; // TODO implment

    private final int hashcode;



/*    public MacAddress(long address) {

        // TODO implement to split the address into bytes and call the byte ctor accordingly
    }*/

    public MacAddress(byte[] address) {

        if (address.length != 6) {
            throw new IllegalArgumentException("A mac address has to consist of exactly 6 byte");
        }

        this.addressByteArray = address;

        int result = 17;
        for (byte aByte : this.addressByteArray) {
            result = 31 * result + aByte;
        }
        hashcode = result;

        this.addressLong = bytesToLong(this.addressByteArray);
    }

/*
    public MacAddress(String address) {

        // TODO implment to parse the string and call the byte constructur accordingly
    }
*/

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    @Override
    public byte[] toByteArray() {
        return addressByteArray;
    }

    @Override
    public int toInt() {
        throw new UnsupportedOperationException("A Mac address cannot be represented as an integer because" +
                                                " an integer has only 32 bits and needed is 48 bits");
    }

    @Override
    public long toLong() {
        return addressLong;
    }

    @Override
    public int size() {
        return 48;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}
