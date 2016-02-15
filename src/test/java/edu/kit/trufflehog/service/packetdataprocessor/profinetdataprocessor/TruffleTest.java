package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.mockito.cglib.transform.impl.InterceptFieldCallback;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;

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
     *     Tests if the Truffle stores different values correctly.
     * </p>
     * @throws Exception
     */
    @Test
    public void testSetAndGetAttribute() throws Exception {
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
    public void testGetAttribute_ReturnsNullIfNoElementFound() throws Exception {
        assertNull(truffle.getAttribute(Integer.class, "thisShouldNotExist"));
    }

    @Test
    public void testSetAttribute_setSameValueTwice() throws Exception {
        truffle.setAttribute(Integer.class, "myInt", 42);
        assertEquals(new Integer(42), truffle.setAttribute(Integer.class, "myInt", 21));

        assertEquals(new Integer(21), truffle.getAttribute(Integer.class, "myInt"));
    }
}