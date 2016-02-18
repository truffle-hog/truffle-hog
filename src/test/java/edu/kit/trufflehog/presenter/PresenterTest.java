package edu.kit.trufflehog.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p>
 *     Unit tests for the {@link Presenter} class
 * </p>
 *
 * @author Maximilian Diez
 * @version 1.0
 */
public class PresenterTest {
    private Presenter presenter;

    /**
     * <p>
     * Resets the {@link Presenter} object at the beginning of every test.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        presenter = Presenter.createPresenter();
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
        presenter = null;
    }

    /**
     * Checks if the presenter singleton pattern is implemented correctly.
     * @throws Exception
     */
    @Test
    public void testSingleton() throws Exception {
        Presenter presenter2 = Presenter.createPresenter();
        assertEquals(presenter, presenter2);
    }
}