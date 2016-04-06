package edu.kit.trufflehog.util;

import edu.kit.trufflehog.util.StringConversion;
import static org.junit.Assert.assertEquals;
import  org.junit.Test;

/**
 * Created by max on 06.04.16.
 */
public class StringConversionTest {

    @Test
    public void simpleTest() {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) 5;
        byteArray[1] = (byte) 6;
        byteArray[2] = (byte) 7;
        byteArray[3] = (byte) 8;
        assertEquals("05060708", StringConversion.bytesToHex(byteArray));
    }

    @Test
    public void secondTest() {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) 9;
        byteArray[1] = (byte) 10;
        byteArray[2] = (byte) 11;
        byteArray[3] = (byte) 12;
        assertEquals("090A0B0C", StringConversion.bytesToHex(byteArray));
    }

    @Test
    public void thirdTest() {
        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 12;
        byteArray[1] = (byte) 13;
        byteArray[2] = (byte) 14;
        byteArray[3] = (byte) 15;
        assertEquals("0C0D0E0F00", StringConversion.bytesToHex(byteArray));
    }

    @Test
    public void nullTest() {
        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 16;
        byteArray[1] = (byte) 17;
        byteArray[2] = (byte) 32;
        byteArray[3] = (byte) 64;
        assertEquals("1011204000", StringConversion.bytesToHex(byteArray));
    }

    @Test
    public void dividerTest() {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) 1;
        byteArray[1] = (byte) 'a';
        byteArray[2] = (byte) 3;
        byteArray[3] = (byte) 4;
        assertEquals("01a61a03a04", StringConversion.bytesToHex(byteArray, 'a'));
    }
}
