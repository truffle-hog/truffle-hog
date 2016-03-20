package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class PlaybackMenuViewController extends AnchorPaneController {

    @FXML
    private Slider slider;

    /**
     * <p>
     * Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public PlaybackMenuViewController(String fxmlFile) {
        super(fxmlFile);

        final ImageButton playButton = new ImageButton("play_capture.png");
        final ImageButton pauseButton = new ImageButton("pause_capture.png");
        getChildren().addAll(playButton, pauseButton);

        playButton.setScaleX(0.7);
        playButton.setScaleY(0.7);

        AnchorPane.setTopAnchor(playButton, 1d);
        AnchorPane.setLeftAnchor(playButton, 40d);

        pauseButton.setScaleX(0.7);
        pauseButton.setScaleY(0.7);

        AnchorPane.setTopAnchor(pauseButton, 1d);
        AnchorPane.setRightAnchor(pauseButton, 40d);
    }
}
