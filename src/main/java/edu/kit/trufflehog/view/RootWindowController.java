package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.IUserCommand;
import edu.kit.trufflehog.interactor.root.RootInteraction;
import edu.kit.trufflehog.util.Notifier;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 11.01.16.
 */
public class RootWindowController extends Notifier<IUserCommand<?>>
		implements IViewController<RootInteraction> {

	private final Stage primaryStage;
	private final Scene monitorScene;

	private final Map<RootInteraction, IUserCommand<?>> interactionMap =
			new EnumMap<>(RootInteraction.class);

	public RootWindowController(Stage primaryStage, Scene monitorScene) {

		this.monitorScene = monitorScene;
		this.primaryStage = primaryStage;
		this.primaryStage.setScene(this.monitorScene);
	}

	public void show() {

		this.primaryStage.show();
	}

	@Override
	public void addCommand(RootInteraction interactor, IUserCommand<?> command) {

		interactionMap.put(interactor, command);
	}
}
