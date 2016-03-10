package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by jan on 23.02.16.
 */
public class MacAddressTest {

    private IAddress address;

    private final long small = 0x000000000001L;
    private final String smallString = "00:00:00:00:00:01";

    private final long middle = 0x0000FF000000L;
    private final String middleString = "00:00:ff:00:00:00";

    private final long big = 0xFF7f7f7f0000L;
    private final String bigString = "ff:7f:7f:7f:00:00";

    @Before
    public void setUp() throws Exception {

        address = new MacAddress(0x201000000000L);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testHashCode() throws Exception {

        final IAddress addressA_1 = new MacAddress(0xFF0022002200L);
        final IAddress addressA_2 = new MacAddress(0xFF0022002200L);

        final IAddress addressB_1 = new MacAddress(0xFF0022002000L);
        final IAddress addressB_2 = new MacAddress(0xFF0022002000L);

        // Corner cases:

        final IAddress smallest = new MacAddress(0x000000000000L);
        final IAddress smallest2 = new MacAddress(0x000000000000L);

        final IAddress biggest = new MacAddress(0xFFFFFFFFFFFFL);
        final IAddress biggest2 = new MacAddress(0xFFFFFFFFFFFFL);

        assertEquals(addressA_1.hashCode(), addressA_2.hashCode());
        assertEquals(addressB_1.hashCode(), addressB_2.hashCode());
        assertEquals(smallest.hashCode(), smallest2.hashCode());
        assertEquals(biggest.hashCode(), biggest2.hashCode());

        assertNotEquals(smallest.hashCode(), biggest.hashCode());
        assertNotEquals(addressA_1.hashCode(), addressB_1.hashCode());

    }

    @Test
    public void testEquals() throws Exception {

        final IAddress addressA_1 = new MacAddress(0xFF0022002200L);
        final IAddress addressA_2 = new MacAddress(0xFF0022002200L);

        final IAddress addressB_1 = new MacAddress(0xFF0022002000L);
        final IAddress addressB_2 = new MacAddress(0xFF0022002000L);

        // Corner cases:

        final IAddress smallest = new MacAddress(0x000000000000L);
        final IAddress smallest2 = new MacAddress(0x000000000000L);

        final IAddress biggest = new MacAddress(0xFFFFFFFFFFFFL);
        final IAddress biggest2 = new MacAddress(0xFFFFFFFFFFFFL);

        assertEquals(addressA_1, addressA_2);
        assertEquals(addressB_1, addressB_2);
        assertEquals(smallest, smallest2);
        assertEquals(biggest, biggest2);

        assertNotEquals(smallest, biggest);
        assertNotEquals(addressA_1, addressB_1);




    }

    @Test
    public void testToByteArray() throws Exception {

        final IAddress addressh = new MacAddress(0xFF0022002200L);

        final byte[] bytes = new byte[] {(byte)0xFF, 0x00, 0x22, 0x00, 0x22, 0x00};

        assertArrayEquals(bytes, addressh.toByteArray());

        System.out.println(Arrays.asList(ArrayUtils.toObject(addressh.toByteArray())).
                stream().map(b -> String.format("%02X", b)).collect(Collectors.joining(":")));

    }

/*    @Test
    public void testToIntArray() throws Exception {

        final IAddress addressh = new MacAddress(0x000001000002L);

        assertArrayEquals(new int[] {0x000001, 0x000002}, addressh.toIntArray());
    }*/

    @Test
    public void testToString() throws Exception {

        assertEquals("20:10:00:00:00:00", address.toString());

        assertEquals(smallString, new MacAddress(small).toString());

        assertEquals(middleString, new MacAddress(middle).toString());

        assertEquals(bigString, new MacAddress(big).toString());
    }
}