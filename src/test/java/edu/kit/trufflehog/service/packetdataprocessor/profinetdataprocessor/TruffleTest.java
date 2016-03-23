package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        //FIXME test additional variables
        final Truffle truffle = Truffle.buildTruffle(1, 2, 3, 4, "test", 5, 6, "testing", 7, "type", 8, 9, 10);

        assertEquals(new MacAddress(1), truffle.getAttribute(MacAddress.class, "sourceMacAddress"));
        assertEquals(new MacAddress(2), truffle.getAttribute(MacAddress.class, "destMacAddress"));
        assertEquals(new IPAddress(3), truffle.getAttribute(IPAddress.class, "sourceIPAddress"));
        assertEquals(new IPAddress(4), truffle.getAttribute(IPAddress.class, "destIPAddress"));
        assertEquals("test", truffle.getAttribute(String.class, "deviceName"));
        assertEquals(new Integer(5), truffle.getAttribute(Integer.class, "etherType"));
        assertEquals(new Integer(6), truffle.getAttribute(Integer.class, "serviceID"));
        assertEquals("testing", truffle.getAttribute(String.class, "serviceIDName"));
        assertEquals(new Integer(7), truffle.getAttribute(Integer.class, "serviceType"));
        assertEquals("type", truffle.getAttribute(String.class, "serviceTypeName"));
        assertEquals(new Long(8), truffle.getAttribute(Long.class, "xid"));
        assertEquals(new Integer(9), truffle.getAttribute(Integer.class, "responseDelay"));
        assertEquals(true, truffle.getAttribute(Boolean.class, "isResponse"));
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