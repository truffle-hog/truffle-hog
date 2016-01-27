package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.MainInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 09.01.16.
 */
public class MainViewController extends BorderPaneController<MainInteraction> {

	private final Map<MainInteraction, IUserCommand<?>> interactionMap =
			new EnumMap<>(MainInteraction.class);

	public MainViewController() {

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void onExit() {


	}


	@Override
	public void addCommand(MainInteraction interactor, IUserCommand<?> command) {

		interactionMap.put(interactor, command);
	}
}
