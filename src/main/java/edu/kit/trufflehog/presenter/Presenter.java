package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.OverlayViewController;
import edu.kit.trufflehog.view.RootWindowController;
import edu.kit.trufflehog.view.controllers.BorderPaneController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
     *     Creates a new instance of a Presenter. A presenter is a singelton, thus InstanceAlreadyExistsException is
     *     thrown when an instance has already been created.
     * </p>
     *
     *
     * @return A new instance of the Presenter, if none already exists.
     * @throws InstanceAlreadyExistsException Thrown if this method is called when an instance of the Presenter
     *         already exists.
     */
    public static Presenter createPresenter() throws InstanceAlreadyExistsException {
        if (presenter != null) {
            throw new InstanceAlreadyExistsException("Error, the presenter at an earlier stage. ");
        }
        presenter = new Presenter();
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
        MainViewController mainView = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainView);
        RootWindowController rootWindow = new RootWindowController(primaryStage, mainScene);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setLeftAnchor(generalStatisticsOverlay, 10d);
    }

}
