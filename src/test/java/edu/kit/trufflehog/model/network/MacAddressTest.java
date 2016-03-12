package edu.kit.trufflehog.model.network;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * <p>
 *     This class contains all tests for the {@link MacAddress} class.
 * </p>
 * @author Jan Hermes, Mark Giraud
 */
public class MacAddressTest {

    private IAddress address;

    @Before
    public void setUp() throws Exception {

        address = new MacAddress(0x201000000000L);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = InvalidMACAddress.class)
    public void macAddress_constructor_throws_on_too_small_address() throws Exception {
        new MacAddress(-1);
    }

    @Test(expected = InvalidMACAddress.class)
    public void macAddress_constructor_throws_on_too_large_address() throws Exception {
        new MacAddress(0xFFFFFFFFFFFFFL);
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

    @Test
    public void testToString() throws Exception {

        assertEquals("20:10:00:00:00:00", address.toString());

        assertEquals("00:00:00:00:00:01", new MacAddress(0x000000000001L).toString());

        assertEquals("00:00:ff:00:00:00", new MacAddress(0x0000FF000000L).toString());

        assertEquals("ff:7f:7f:7f:00:00", new MacAddress(0xFF7f7f7f0000L).toString());
    }

    @Test
    public void size_is_48() throws Exception {
        assertEquals(48, address.size());
    }

    @Test
    public void isMulticast_returns_true_if_multicast() throws Exception {
        assertTrue("01:00:0C:CC:CC:CC should be multicast but is not", new MacAddress(0x01000CCCCCCCL).isMulticast());
        assertTrue("01:00:0C:CC:CC:CD should be multicast but is not", new MacAddress(0x01000CCCCCCDL).isMulticast());
        assertTrue("01:80:C2:00:00:01 should be multicast but is not", new MacAddress(0x0180C2000001L).isMulticast());
        assertTrue("33:33:AA:BB:BA:AB should be multicast but is not", new MacAddress(0x3333AABBBAABL).isMulticast());
    }

    @Test
    public void isMulticast_returns_false_if_not_multicast() throws Exception {
        assertFalse("02:00:0C:CC:CC:CC should not be multicast but is", new MacAddress(0x02000CCCCCCCL).isMulticast());
        assertFalse("08:00:0C:C5:CC:22 should not be multicast but is", new MacAddress(0x08000CC5CC22L).isMulticast());
        assertFalse("34:33:AA:BB:BA:AB should not be multicast but is", new MacAddress(0x3433AABBBAABL).isMulticast());
    }
}