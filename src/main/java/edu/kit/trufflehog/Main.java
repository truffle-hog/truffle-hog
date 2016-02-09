package edu.kit.trufflehog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 *     The main class in TruffleHog. It initiates everything.
 * </p>
 */
public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * <p>
	 *     The main method of TruffleHog. It starts the program.
	 * </p>
	 *
     * @param args command line arguments
     */
	public static void main(String[] args) {
        logger.debug("test");
	}

    /**
     * Dummy method to test unit tests and code coverage
     *
     * TODO: Take away once real test exist for TruffleHog
     *
     * @param i any number
     * @return returns i
     */
    public int methodToTest(int i) {
        return i;
    }
}