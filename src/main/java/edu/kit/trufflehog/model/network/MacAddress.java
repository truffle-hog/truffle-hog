package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jan on 19.02.16.
 */
public class MacAddress implements IAddress {

    private final byte[] addressByteArray;

    private int[] intArray = null;

    private final long address;

    private final int hashcode;

    private String addressString = null;

    public MacAddress(long address) {

        if (address > 0xFFFFFFFFFFFFL || address < 0) {
            throw new IllegalArgumentException("the address is too big or in wrong format");
        }

        this.address = address;

        hashcode = (new Long(this.address)).hashCode();

        byte[] bytes = ByteBuffer.allocate(8).putLong(0, this.address).array();

        addressByteArray = Arrays.copyOfRange(bytes, 2, 8);
    }

/*    public MacAddress(byte[] address) {

        if (address.length != 6) {
            throw new IllegalArgumentException("A mac address has to consist of exactly 6 byte");
        }

        this.addressByteArray = address;

        int result = 17;
        for (byte aByte : this.addressByteArray) {
            result = 31 * result + aByte;
        }
        hashcode = result;

        addressString = StringConversion.bytesToHex(addressByteArray, ':');
    //    addressString = Arrays.asList(ArrayUtils.toObject(addressByteArray)).
    //            stream().map(b -> StringUtils.leftPad(Integer.toHexString(b).substring(Integer.toHexString(b)), 2, '0')).collect(Collectors.joining(":"));
    }*/


    @Override
    public byte[] toByteArray() {

        // TODO change to Arrays.copyof or?
        return Arrays.copyOf(addressByteArray, 6);
    }

/*    @Override
    public int[] toIntArray() {

        if (intArray == null) {

            intArray = new int[2];

            intArray[0] = (int) (address >>> 24);
            intArray[1] = (int) (address & 0x0000000000FFFFFFL);

        }
        return Arrays.copyOf(intArray, 2);
    }*/

    @Override
    public int size() {
        return 48;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof IAddress)) {
            return false;
        }
        final IAddress other = (IAddress) o;

        return Arrays.equals(addressByteArray, other.toByteArray());

        //return Arrays.equals(this.toByteArray(), other.toByteArray());
    }

    @Override
    public String toString() {

        if (addressString == null) {

/*            String myMacString = Long.toHexString(address);

            myMacString = StringUtils.leftPad(myMacString, 12, '0');

            final StringBuilder macBuilder = new StringBuilder(myMacString);

            for (int i = 2; i < 17; i += 3) {
                macBuilder.insert(i, ':');
            }
            this.addressString = macBuilder.toString();*/

            this.addressString = Arrays.asList(ArrayUtils.toObject(toByteArray())).stream().
                    map(b -> String.format("%02x", b)).collect(Collectors.joining(":"));
        }

        return addressString;
    }
}
