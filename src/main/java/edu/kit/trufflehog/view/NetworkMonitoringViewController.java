package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.NetworkMonitoringInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 09.01.16.
 */
public class NetworkMonitoringViewController
		extends BorderPaneController<NetworkMonitoringInteraction> {

	private final Map<NetworkMonitoringInteraction, IUserCommand<?>> interactionMap =
			new EnumMap<>(NetworkMonitoringInteraction.class);

	public NetworkMonitoringViewController() {

		final FXMLLoader fxmlLoader =
				new FXMLLoader(getClass().getResource("NetworkMonitoringView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}


	@Override
	public void addCommand(NetworkMonitoringInteraction interactor, IUserCommand<?> command) {

		interactionMap.put(interactor, command);
	}
}
