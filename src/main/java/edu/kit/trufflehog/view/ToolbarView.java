package edu.kit.trufflehog.view;


import edu.kit.trufflehog.interaction.ToolbarInteraction;
import edu.kit.trufflehog.view.controllers.ToolBarViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import java.util.EnumMap;

/**
 * <p>
 *     The ToolBarViewController class is the view designed for all most important functionality that should be
 *     directly accessed by the user during the running program.
 * </p>
 */
public final class ToolbarView extends ToolBarViewController<ToolbarInteraction> {

    @FXML
    private ToggleButton connectButton;

    @FXML
    private ToggleButton filterButton;

    private final FilterOverlayView filterOverlayView;

    public ToolbarView(FilterOverlayView filterOverlayView) {
        super("main_toolbar.fxml", new EnumMap<>(ToolbarInteraction.class));

        this.filterOverlayView = filterOverlayView;
        this.filterOverlayView.visibleProperty().bind(filterButton.selectedProperty());
    }

    @FXML
    private void onToggleConnect(ActionEvent event) {

        if (!connectButton.isSelected()) {

            notifyListeners(getCommand(ToolbarInteraction.DISCONNECT));

        } else {

            notifyListeners(getCommand(ToolbarInteraction.CONNECT));
        }
    }

}
