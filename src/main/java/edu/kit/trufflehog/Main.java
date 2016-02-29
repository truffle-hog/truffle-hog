package edu.kit.trufflehog;

import edu.kit.trufflehog.presenter.Presenter;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.RootWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.management.InstanceAlreadyExistsException;
// TODO below
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

/**
 * <p>
 *     The main class in TruffleHog. It initiates everything.
 * </p>
 */
public class Main extends Application {
	private static Stage primaryStage;
	private Presenter presenter;

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
		launch(args);
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
		Main.primaryStage = primaryStage;
		primaryStage.setTitle("TruffleHog");
		this.presenter = Presenter.createPresenter();
		presenter.present();
	}

	/**
	 * JavaFX stop method. Here, all pending resources get destroyed.
	 */
	@Override
	public void stop() {

	}

	/**
	 * Getter for the primary JavaFX stage supplied by the system for use by the {@link Presenter}. As there can
	 * must only be one primaryStage it is used in a static context.
	 *
	 * @return primaryStage
     */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}
