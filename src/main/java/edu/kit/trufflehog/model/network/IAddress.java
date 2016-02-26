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

    /** Must be overwritten to adhere to the interfaces contract. **/
    @Override
    int hashCode();

    /** Must be overwritten to adhere to the interfaces contract. **/
    @Override
    boolean equals(Object o);

    boolean isMulticast();
}
