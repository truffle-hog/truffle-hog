package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.view.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.management.InstanceAlreadyExistsException;

import static edu.kit.trufflehog.Main.*;

/**
 * <p>
 *      The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *      with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *      TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {
    private static Presenter presenter;
    /**
     * <p>
     *     Creates a new instance of a singleton Presenter or returns it if it was created before.
     * </p>
     *
     *
     * @return A new instance of the Presenter, if none already exists.
     */
    public static Presenter createPresenter() {
        if (Presenter.presenter == null) {
            Presenter.presenter = new Presenter();
        }
        return presenter;
    }

    /**
     * <p>
     *     Creates a new instance of a Presenter.
     * </p>
     */
    private Presenter() {
    }

    /**
     *  <p>
     *      Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *      resources they need ect.
     *  </p>
     */
    public void present() { initGUI(); }

    private void initGUI() {
        Stage primaryStage = getPrimaryStage();

        // setting up main window
        MainViewController mainView = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainView);
        RootWindowController rootWindow = new RootWindowController(primaryStage, mainScene);
        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(
                new Image(
                        RootWindowController.class.getResourceAsStream("icon.png")));
        primaryStage.show();

        // setting up general statistics overlay
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);

        // setting up menubar
        Button settingsButton = new ImageButton("cog-3x.png");
        // TODO das ist ein bisschen hässlich. geht leider afaik nicht anders mit eigen gebauten javafx-nodes.
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage settingsStage = new Stage();
                SettingsViewController settingsView = new SettingsViewController("settings_view.fxml");
                Scene settingsScene = new Scene(settingsView);
                settingsStage.setScene(settingsScene);
                settingsStage.show();
            }
        });
        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton);
        mainView.getChildren().add(mainToolBarController);


        // setting up node statistics overlay
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        mainView.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
    }

}
