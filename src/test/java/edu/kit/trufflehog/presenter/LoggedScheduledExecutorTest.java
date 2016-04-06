package edu.kit.trufflehog.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * <p>
 *     Unit tests for the {@link LoggedScheduledExecutor} class
 * </p>
 *
 * @author Maximilian Diez
 * @version 1.0
 */
public class LoggedScheduledExecutorTest {
    private LoggedScheduledExecutor executor;

    /**
     * <p>
     * Resets the {@link Presenter} object at the beginning of every test.
     * </p>
     *

  * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        executor = LoggedScheduledExecutor.getInstance();
    }

    /**
     * <p>
     * Deletes all folders created by the previous test.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @After
    public void tearDown() throws Exception {
        executor = null;
    }

    /**
     * Test singleton.
     */
    @Test
    public void testSingleton() {
        assertEquals(executor, LoggedScheduledExecutor.getInstance());
    }
}