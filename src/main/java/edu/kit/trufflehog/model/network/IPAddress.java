package edu.kit.trufflehog.model.network;

/**
 * Created by root on 10.03.16.
 */
public class IPAddress implements IAddress {

    private final long address;

    public IPAddress(final long address) {
        this.address = address;

        //TODO implement this correctly
    }

    @Override
    public byte[] toByteArray() {
        //TODO implement this
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public int size() {
        //TODO implement this
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public boolean isMulticast() {
        //TODO implement this
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String toString() {
        //TODO implement this
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
