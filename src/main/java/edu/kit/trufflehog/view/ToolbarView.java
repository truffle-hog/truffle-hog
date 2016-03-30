package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.interaction.StartViewInteraction;
import edu.kit.trufflehog.interaction.ToolbarViewInteraction;
import edu.kit.trufflehog.view.controllers.IViewController;
import edu.kit.trufflehog.view.controllers.ToolBarViewController;
import edu.kit.trufflehog.view.controllers.ViewControllerNotifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The ToolBarViewController class is the view designed for all most important functionality that should be
 *     directly accessed by the user during the running program.
 * </p>
 */
public final class ToolbarView extends ToolBarViewController<ToolbarViewInteraction> {

    /** The commands that are mapped to their interactions. **/
    private final Map<ToolbarViewInteraction, IUserCommand> interactionMap =
            new EnumMap<>(ToolbarViewInteraction.class);

    @FXML
    private ToggleButton connectButton;

    @FXML
    private ToggleButton settingsButton;

    @FXML
    private ToggleButton filterButton;

    private final FilterOverlayView filterOverlayView;

    public ToolbarView(FilterOverlayView filterOverlayView) {
        super("main_toolbar.fxml", new EnumMap<>(ToolbarViewInteraction.class));

        this.filterOverlayView = filterOverlayView;
        this.filterOverlayView.visibleProperty().bind(filterButton.selectedProperty());
    }

    public void onToggleConnect() {

        if (connectButton.isSelected()) {

            notifyListeners(interactionMap.get(ToolbarViewInteraction.DISCONNECT));

        } else {

            notifyListeners(interactionMap.get(ToolbarViewInteraction.CONNECT));

        }
    }

    public void onToggleFilter() {

/*        if (filterButton.isSelected()) {

            Platform.runLater(() -> {

                filterOverlayView.setVisible(true);
            });
        } else {

            Platform.runLater(() -> {

                filterOverlayView.setVisible(false);
            });
        }*/




    }

    public void onToggleSettings() {

    }
}
