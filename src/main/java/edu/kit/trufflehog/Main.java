/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog;

import edu.kit.trufflehog.presenter.Presenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * <p>
 *     The main class in TruffleHog. It initiates everything.
 * </p>
 */
public class Main extends Application {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    private Presenter presenter;

	/**
	 * <p>
	 *     The main method of TruffleHog.
	 * </p>
	 *
     * @param args command line arguments
     */
	public static void main(String[] args) {
        // Set docking icon on mac
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("mac")) {
            try {
                URL iconURL = Main.class.getResource(File.separator + "edu" + File.separator + "kit" + File.separator
                        + "trufflehog" + File.separator + "view" + File.separator +"icon.png");
                Image image = new ImageIcon(iconURL).getImage();
                com.apple.eawt.Application.getApplication().setDockIconImage(image);
            } catch (Exception e) {
                logger.error("Unable to set docking icon, probably not running on a mac", e);
            }
        }

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
	 * JavaFX start method.
	 *
	 * @param primaryStage Supplied by system
     */
	@Override
	public void start(Stage primaryStage) {

		// TODO horror
		primaryStage.setTitle("TruffleHog");
		presenter = new Presenter(primaryStage);
		presenter.present();
	}

	/**
	 * JavaFX stop method. Here, all pending resources get destroyed.
	 */
	@Override
	public void stop() {
		Platform.exit();

        //TODO close services (TruffleReceiver otherwise produces a broken pipe)
        presenter.shutdown();
		System.exit(0);
	}
}
