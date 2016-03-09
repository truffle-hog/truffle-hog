package edu.kit.trufflehog.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.Object;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * <p>
 *     This class tests the Notifier.
 * </p>
 * @author Mark Giraud
 */
public class NotifierTest {

    private Notifier notifier;

    @Before
    public void setUp() throws Exception {
        notifier = new Notifier() {
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Checks if one listener is correctly added.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddListenerAndNotify() throws Exception {

        IListener mockedListener = mock(IListener.class);

        notifier.addListener(mockedListener);

        Object msg = mock(Object.class);

        notifier.notifyListeners(msg);

        verify(mockedListener).receive(msg);
        verifyZeroInteractions(msg);
    }

    /**
     * <p>
     *     Checks if a listener is correctly removed.
     * </p>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddListenerThenNotifyThenRemove() throws Exception {

        IListener mockedListener = mock(IListener.class);

        notifier.addListener(mockedListener);

        Object msg = mock(Object.class);
        notifier.notifyListeners(msg);

        verify(mockedListener).receive(msg);

        notifier.removeListener(mockedListener);
        notifier.notifyListeners(msg);

        verifyNoMoreInteractions(mockedListener);
        verifyZeroInteractions(msg);
    }

    /**
     * <p>
     *     This test checks, if multiple listeners are registered correctly and
     *     if the sent message is received correctly by each listener.
     * </p>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddMultipleListeners() throws Exception {

        IListener mockedListener1 = mock(IListener.class);
        IListener mockedListener2 = mock(IListener.class);
        IListener mockedListener3 = mock(IListener.class);

        notifier.addListener(mockedListener1);
        notifier.addListener(mockedListener2);
        notifier.addListener(mockedListener3);

        Object msg = mock(Object.class);

        notifier.notifyListeners(msg);

        verify(mockedListener1).receive(msg);
        verify(mockedListener2).receive(msg);
        verify(mockedListener3).receive(msg);
        verifyZeroInteractions(msg);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNotifyMultipleTimes() throws Exception {
        IListener mockedListener1 = mock(IListener.class);
        IListener mockedListener2 = mock(IListener.class);

        notifier.addListener(mockedListener1);
        notifier.addListener(mockedListener2);

        Object msg1 = mock(Object.class);
        Object msg2 = mock(Object.class);
        Object msg3 = mock(Object.class);
        Object msg4 = mock(Object.class);

        notifier.notifyListeners(msg1);
        notifier.notifyListeners(msg2);
        notifier.notifyListeners(msg3);
        notifier.notifyListeners(msg4);

        verify(mockedListener1).receive(msg1);
        verify(mockedListener1).receive(msg2);
        verify(mockedListener1).receive(msg3);
        verify(mockedListener1).receive(msg4);

        verify(mockedListener2).receive(msg1);
        verify(mockedListener2).receive(msg2);
        verify(mockedListener2).receive(msg3);
        verify(mockedListener2).receive(msg4);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void testAddListenerNull() throws Exception {
        notifier.addListener(null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void testRemoveListenerNull() throws Exception {
        notifier.removeListener(null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void testNotifyListenersNull() throws Exception {
        notifier.notifyListeners(null);
    }

}