package edu.kit.trufflehog;

import javafx.application.Application;
import javafx.stage.Stage;
// TODO below
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

/**
 * <p>
 *     The main class in TruffleHog. It initiates everything.
 * </p>
 */
public class Main extends Application {
	private Stage primaryStage;

	// TODO below
	// private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * <p>
	 *     The main method of TruffleHog.
	 * </p>
	 *
     * @param args command line arguments
     */
	public static void main(String[] args) {
		// TODO below
		// logger.debug("example log at debug level");
	}

	/**
	 * This method gets called even BEFORE start(). The bootstrap begins here.
	 *
	 * TODO simply remove this method if it is not used in the end.
	 */
	@Override
	public void init() {

	}

	/**
	 * JavaFX start method. This method mainly initializes the GUI-part of the application.
	 *
	 * @param primaryStage supplied by system
     */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TruffleHog");
	}

	/**
	 * JavaFX stop method. Here, all pending resources get destroyed.
	 */
	@Override
	public void stop() {

	}
}