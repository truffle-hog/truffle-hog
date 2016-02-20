package edu.kit.trufflehog.model.network;

/**
 * This intervae provides all functionality that is needed in an address object.
 *
 * A MacAddress implementation has to override hashcode and equals to adhere to the contract of this interface
 */
public interface IAddress {

    /**
     * Returns this address as byte array.
     * @return a byte array representation of this address
     */
    byte[] toByteArray();

    /**
     * Returns this address as integer.
     * @return an integer representation of this address
     * @throws UnsupportedOperationException if this operation is not supported by the implementing address
     *              (e.g. the size of the address is more than 32 bit)
     */
    int toInt();

    /**
     * Returns this address as long.
     * @return a long representation of this address
     * @throws UnsupportedOperationException if this operation is not supported by the implementing address
     *              (e.g. the size of the address is more than 64 bit)
     */
    long toLong();

    /**
     * Returns the size in Bit of this address.
     * @return the size of this address in bit
     */
    int size();

    /**
     * Returns a proper string representaiton of this address (e.g. 00:00:23:23:44:55: for mac or 192.168.3.1 for IP)
     * @return proper string representation
     */
    @Override
    String toString();

    @Override
    int hashCode();

    @Override
    boolean equals(Object o);
}
