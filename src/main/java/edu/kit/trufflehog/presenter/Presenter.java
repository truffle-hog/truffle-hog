package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.configdata.IConfigData;
import edu.kit.trufflehog.view.MainToolBarController;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.OverlayViewController;
import edu.kit.trufflehog.view.RootWindowController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledExecutorService;

import static edu.kit.trufflehog.Main.getPrimaryStage;

/**
 * <p>
 *      The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *      with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *      TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {
    private static final Logger logger = LogManager.getLogger(Presenter.class);

    private static Presenter presenter;
    private final IConfigData configData;
    private final FileSystem fileSystem;
    private final ScheduledExecutorService executorService;

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
        this.fileSystem = new FileSystem();
        this.executorService = new LoggedScheduledExecutor(10);

        IConfigData configDataTemp;
        try {
            configDataTemp = new ConfigDataModel(fileSystem, executorService);
        } catch (NullPointerException e ) {
            configDataTemp = null;
            logger.error("Unable to set config data model", e);
        }
        configData = configDataTemp;
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
        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(
                new Image(
                        RootWindowController.class.getResourceAsStream("icon.png")));
        primaryStage.show();

        // CTRL+Q for program quitting
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN), primaryStage::close);

        // setting up general statistics overlay
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);

        // setting up menubar
        Button settingsButton = new ImageButton("gear.png");
        settingsButton.setOnAction(event -> {
            Stage settingsStage = new Stage();
            SettingsViewController settingsView = new SettingsViewController("settings_view.fxml");
            Scene settingsScene = new Scene(settingsView);
            settingsStage.setScene(settingsScene);
            settingsStage.show();
            // CTRL+W for closing
            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN), settingsStage::close);
            // CTRL+S triggers info about program settings saving
            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Relax, no need to save anything here");
                alert.setHeaderText(null);
                alert.setContentText("Oops. Seems you wanted to save the configuration bei pressing CTRL+S. This" +
                        " is not necessary thanks to the awesome always up-to-date saving design of TruffleHog.");
                alert.showAndWait();
            });
        });

        // setting up keyboard shortcut
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN), settingsButton::fire);
        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton);
        mainView.getChildren().add(mainToolBarController);

        // setting up node statistics overlay
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        mainView.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

}
