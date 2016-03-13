package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class contains all tests for the {@link Truffle} class.
 *
 * @author Mark Giraud
 */
public class TruffleTest {


    private Truffle truffle;

    @Before
    public void setUp() throws Exception {
        truffle = new Truffle();
    }

    @After
    public void tearDown() throws Exception {
        truffle = null;
    }

    /**
     * <p>
     *     This tests the buildTruffle method and checks if the values are properly stored.
     *     Unfortunately this relies on the MacAddress and IPAddress classes.
     * </p>
     * @throws Exception
     */
    @Test
    public void buildTruffle_stores_values_correctly() throws Exception {
        final Truffle truffle = Truffle.buildTruffle(1, 2, 3, 4, "test", (short) 5);

        assertEquals(new MacAddress(1), truffle.getAttribute(MacAddress.class, "sourceMacAddress"));
        assertEquals(new MacAddress(2), truffle.getAttribute(MacAddress.class, "destMacAddress"));
        assertEquals(new IPAddress(3), truffle.getAttribute(IPAddress.class, "sourceIPAddress"));
        assertEquals(new IPAddress(4), truffle.getAttribute(IPAddress.class, "destIPAddress"));
        assertEquals("test", truffle.getAttribute(String.class, "deviceName"));
        assertEquals(new Short((short) 5), truffle.getAttribute(Short.class, "etherType"));
    }

    /**
     * <p>
     *     Tests if the Truffle stores different values correctly.
     * </p>
     * @throws Exception
     */
    @Test
    public void getAttribute_returns_same_value_set_by_setAttribute() throws Exception {
        truffle.setAttribute(Integer.class, "myInt", 5);
        truffle.setAttribute(Integer.class, "mySecondInt", 42);
        truffle.setAttribute(String.class, "myInt", "Drölf");
        truffle.setAttribute(Long.class, "myLong", 999999999999999999L);

        assertEquals("Drölf", truffle.getAttribute(String.class, "myInt"));
        assertEquals(new Integer(5), truffle.getAttribute(Integer.class, "myInt"));
        assertEquals(new Long(999999999999999999L), truffle.getAttribute(Long.class, "myLong"));
        assertEquals(new Integer(42), truffle.getAttribute(Integer.class, "mySecondInt"));
    }

    /**
     * <p>
     *     Tests if the {@link Truffle#getAttribute(Class, String)} method returns null if no element was found.
     * </p>
     * @throws Exception
     */
    @Test
    public void getAttribute_returns_null_if_no_element_found() throws Exception {
        assertNull(truffle.getAttribute(Integer.class, "thisShouldNotExist"));
    }

    @Test
    public void setAttribute_returns_old_value_and_overwrites_with_new_value() throws Exception {
        truffle.setAttribute(Integer.class, "myInt", 42);
        assertEquals(new Integer(42), truffle.setAttribute(Integer.class, "myInt", 21));

        assertEquals(new Integer(21), truffle.getAttribute(Integer.class, "myInt"));
    }
}