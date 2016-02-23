package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by jan on 19.02.16.
 */
public class MacAddress implements IAddress {

    //private final byte[] addressByteArray;

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
        return null;
    }

    @Override
    public int[] toIntArray() {

        if (intArray == null) {

            intArray = new int[2];

            intArray[0] = (int) (address >>> 24);
            intArray[1] = (int) (address & 0x0000000000FFFFFFL);

        }
        return Arrays.copyOf(intArray, 2);
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
    public boolean equals(Object o) {

        if (!(o instanceof IAddress)) {
            return false;
        }
        final IAddress other = (IAddress) o;

        return Arrays.equals(this.toIntArray(), other.toIntArray());

        //return Arrays.equals(this.toByteArray(), other.toByteArray());
    }


    @Override
    public int compareTo(IAddress o) {

/*        if (size() > o.size()) {
            return ;
        } else if (size() < o.size()) {
            return -1;
        }*/

        if (size() != o.size()) {
            return size() - o.size();
        }

        // so sizes are same

        for (int i = 0; i < this.toIntArray().length; i++) {

            if (this.toIntArray()[i] != o.toIntArray()[i]) {
                return this.toIntArray()[i] - o.toIntArray()[i];
            }
        }

        return 0;
    }

    @Override
    public String toString() {

        if (addressString == null) {

            String myMacString = Long.toHexString(address);

            myMacString = StringUtils.leftPad(myMacString, 12, '0');

            final StringBuilder macBuilder = new StringBuilder(myMacString);

            for (int i = 2; i < 17; i += 3) {
                macBuilder.insert(i, ':');
            }
            this.addressString = macBuilder.toString();
        }

        return addressString;
    }
}
