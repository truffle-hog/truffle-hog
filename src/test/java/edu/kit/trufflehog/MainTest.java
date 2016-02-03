package edu.kit.trufflehog;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Dummy test class to test main
 */
public class MainTest {

    /**
     * Dummy test method
     *
     * @throws Exception if something goes wrong --> here it is :D <--
     */
    @Test
    public void testMethodToTest() throws Exception {
        Main main = new Main();
        int result = main.methodToTest(1);
        assertEquals(result, 1);
    }
}
