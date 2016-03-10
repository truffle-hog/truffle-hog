package edu.kit.trufflehog.util;

/**
 * Created by jan on 23.02.16.
 */
public class StringConversion {

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];

        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] bytes, char divider) {

        char[] hexChars = new char[bytes.length * 3 - 1];


        for ( int j = 0; j < bytes.length - 1; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = divider;
        }

        int value = bytes[bytes.length - 1] & 0xFF;
        hexChars[(bytes.length - 1) * 3] = hexArray[value >>> 4];
        hexChars[(bytes.length - 1) * 3 + 1] = hexArray[value & 0x0F];

        return new String(hexChars);
    }


}
