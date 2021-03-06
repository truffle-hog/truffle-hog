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

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import edu.kit.trufflehog.presenter.Presenter;
import edu.kit.trufflehog.presenter.nativebuilders.NativeBuilder;
import edu.kit.trufflehog.presenter.nativebuilders.osx.OSXBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * <p>
 *     The main class in TruffleHog. It initiates everything.
 * </p>
 */
public class Main extends Application {
	private static Presenter presenter;
	private static HostServicesDelegate host;

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
	 * JavaFX start method.
	 *
	 * @param primaryStage Supplied by system
	 */
	@Override
	public void start(Stage primaryStage) {
		buildNative();

		host = HostServicesFactory.getInstance(this);

		primaryStage.setTitle("TruffleHog");
		presenter = new Presenter(primaryStage);
		presenter.run();
	}

	/**
	 * JavaFX stop method. Here, all pending resources get destroyed.
	 */
	@Override
	public void stop() {
		presenter.finish();
	}

	/**
	 * <p>
	 *     Builds components native to the operating system (for example the doc icon on Mac OSX).
	 * </p>
	 */
	private void buildNative() {
		NativeBuilder nativeBuilder = new OSXBuilder();
		nativeBuilder.build();
	}

	public static HostServicesDelegate getHost() {
		return host;
	}
}