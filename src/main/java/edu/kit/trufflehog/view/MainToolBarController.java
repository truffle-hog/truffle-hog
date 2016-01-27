package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.ToolBarInteraction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 19.01.16.
 */
public class MainToolBarController extends ToolBarController<ToolBarInteraction> {

	private final Map<ToolBarInteraction, IUserCommand<?>> interactionMap =
			new EnumMap<>(ToolBarInteraction.class);

	@FXML
	private Button connectButton;

	@FXML
	private Button disconnectButton;

	@FXML
	private ToggleButton recordButton;

	@FXML
	private Button refreshButton;

	@FXML
	private Button transmodeButton;

	@FXML
	private Button pickmodeButton;

	public MainToolBarController() {

		final FXMLLoader fxmlLoader =
				new FXMLLoader(getClass().getResource("MainToolBar.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

/*    private void sendCommand(ICommand<?> command) {

        assert(command != null);

*//*        myObservers.parallelStream().forEach(observer -> {
			observer.update(command);
        });*//*



        try {
            userCommandQueue.push(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

	public void onClickButtonConnect() {

		connectButton.setDisable(true);
		disconnectButton.setDisable(false);

		// TODO make nullcheck
		final IUserCommand<?> command = interactionMap.get(ToolBarInteraction.CONNECT);
		sendToListeners(command);
	}


	public void onClickButtonDisconnect() {

		connectButton.setDisable(false);
		disconnectButton.setDisable(true);

		if (recordButton.isSelected()) {
			recordButton.setSelected(false);
		}
		final IUserCommand<?> command = interactionMap.get(ToolBarInteraction.DISCONNECT);
		sendToListeners(command);

	}

	public void onClickButtonRecord() {

		if (recordButton.isSelected()) {

			final IUserCommand<?> command =
					interactionMap.get(ToolBarInteraction.RECORD_START);
			sendToListeners(command);

		} else {

			final IUserCommand<?> command = interactionMap.get(ToolBarInteraction.RECORD_STOP);
			sendToListeners(command);
		}
	}

	public void onClickButtonRefreshLayout() {

		sendToListeners(interactionMap.get(ToolBarInteraction.LAYOUT_REFRESH));
	}

	public void onClickButtonPickMode() {

		transmodeButton.setDisable(false);
		pickmodeButton.setDisable(true);

		sendToListeners(interactionMap.get(ToolBarInteraction.PICK_MODE));
	}

	public void onClickButtonTransMode() {

		pickmodeButton.setDisable(false);
		transmodeButton.setDisable(true);

		sendToListeners(interactionMap.get(ToolBarInteraction.TRANS_MODE));
	}

	@Override
	public void addCommand(ToolBarInteraction interaction, IUserCommand<?> command) {

		interactionMap.put(interaction, command);
	}
}
