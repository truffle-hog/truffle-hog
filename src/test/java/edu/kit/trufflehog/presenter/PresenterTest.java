package edu.kit.trufflehog.presenter;

import javafx.stage.Stage;
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
        presenter = new Presenter(new Stage());
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
}