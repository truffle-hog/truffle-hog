package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.OverlayInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 19.01.16.
 */
public class OverlayViewController extends BorderPaneController<OverlayInteraction> {

	private final Map<OverlayInteraction, IUserCommand<?>> interactionMap =
			new EnumMap<>(OverlayInteraction.class);

	public OverlayViewController() {

		final FXMLLoader fxmlLoader =
				new FXMLLoader(getClass().getResource("OverlayView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void addCommand(OverlayInteraction interactor, IUserCommand<?> command) {
		interactionMap.put(interactor, command);
	}
}
