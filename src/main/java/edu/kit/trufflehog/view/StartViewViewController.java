package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.StartViewInteraction;
import edu.kit.trufflehog.view.controllers.AnchorPaneInteractionController;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The StartViewViewController is the first view the user sees. It cannot be clicked away like the other views, and
 *     it gives users an option of what to start. Once the user has selected a view port, the view is switched
 *     accordingly.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class StartViewViewController extends AnchorPaneInteractionController<StartViewInteraction> {
    private final Map<StartViewInteraction, IUserCommand> interactionMap = new EnumMap<>(StartViewInteraction.class);
    private final ViewSwitcher viewSwitcher;

    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<String> liveListView;
    @FXML
    private ListView<String> captureListView;

    /**
     * <p>
     *     Creates a new OverlayViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     OverlayViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public StartViewViewController(final String fxmlFileName,
                                   final ObservableList<String> liveItems,
                                   final ObservableList<String> captureItems,
                                   final ViewSwitcher viewSwitcher) {
        super(fxmlFileName);
        this.viewSwitcher = viewSwitcher;

        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);

        // Fill the list views
        liveListView.setItems(liveItems);
        captureListView.setItems(captureItems);

        // Handle mutual selection: if one cell is selected, deselect the other on automatically
        setOnClickCellFactory(liveListView, captureListView);
        setOnClickCellFactory(captureListView, liveListView);

        // Add the start button
        ImageButton startButton = new ImageButton("start.png");
        startButton.setOnAction(event -> playPress());
        AnchorPane.setBottomAnchor(startButton, 25d);
        AnchorPane.setRightAnchor(startButton, 25d);

        // Set up the text
        final Text titleText = new Text("Choose a view port");
        titleText.setId("title_text");

        final Text liveText = new Text("Live");
        liveText.setId("sub_title_text");

        final Text captureText = new Text("Capture");
        captureText.setId("sub_title_text");

        // Add everything to anchor pane
        this.getChildren().addAll(titleText, liveText, captureText, startButton);


        // Set up resizing of text and split pane
        titleText.translateXProperty().bind(this.widthProperty().subtract(230).divide(2));

        liveText.xProperty().bind(this.widthProperty().subtract(splitPane.widthProperty()).divide(2).multiply(1.3));
        captureText.xProperty().bind(this.widthProperty().subtract(splitPane.widthProperty()).divide(2).multiply(1.8));

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            resizeHeightOnUpdate(splitPane, titleText, liveText, captureText);
        });

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(splitPane, this.getWidth() * 0.31);
            AnchorPane.setRightAnchor(splitPane, this.getWidth() * 0.31);
        });
    }

    /**
     * <p>
     *     Sets the vertical resize behavior of all the components this method receives.
     * </p>
     *
     * @param splitPane The split pane where the two list views are included
     * @param titleText The title text of the slide (has to be
     * @param liveText The live text label
     * @param captureText The capture text label
     */
    private void resizeHeightOnUpdate(final SplitPane splitPane,
                                      final Text titleText,
                                      final Text liveText,
                                      final Text captureText) {

        // Resize splitpane
        AnchorPane.setBottomAnchor(splitPane, this.getHeight() * 0.25);
        AnchorPane.setTopAnchor(splitPane, this.getHeight() * 0.25);

        // Resize titleText
        AnchorPane.setTopAnchor(titleText, this.getHeight() * 0.12);
        AnchorPane.setBottomAnchor(titleText, this.getHeight() * 0.82);

        // Resize liveText
        AnchorPane.setTopAnchor(liveText, this.getHeight() * 0.77);
        AnchorPane.setBottomAnchor(liveText, this.getHeight() * 0.18);

        // Resize captureText
        AnchorPane.setTopAnchor(captureText, this.getHeight() * 0.77);
        AnchorPane.setBottomAnchor(captureText, this.getHeight() * 0.18);
    }

    /**
     * <p>
     *     Sets up the select and deselect mechanisms between the two list views. This is actually a bit more complicated
     *     because one only one item should ever be selected. This unfortunately requires a custom cell factory which
     *     sucks a bit. But there is nothing we can do as of right now.
     * </p>
     * <p>
     *    This method has to be applied to twice, to both list views in alternating order. This is because the first
     *    parameter is the one whose selection behavior is set, while the second parameter is used to deselect any
     *    element from the other table view in case there was one selected.
     * </p>
     *
     * @param listView1 The first list view
     * @param listView2 The second list view
     */
    private void setOnClickCellFactory(ListView<String> listView1, ListView<String> listView2) {
        listView1.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listView1.requestFocus();
                if (! cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (index >= 0 && index < listView1.getItems().size() && listView1.getSelectionModel()
                            .isSelected(index)) {
                        listView1.getSelectionModel().clearSelection();
                        event.consume();
                    } else if (listView2.getSelectionModel().getSelectedItem() != null) {
                        index = listView2.getSelectionModel().getSelectedIndex();
                        if (index >= 0 && index < listView2.getItems().size() && listView2.getSelectionModel()
                                .isSelected(index)) {
                            listView2.getSelectionModel().clearSelection();
                        }
                    } else {
                        listView1.getSelectionModel().select(index);
                    }
                }
            });
            return cell;
        });
    }

    /**
     * <p>
     *     Starts the actual graph by switching views and sending out the start signal (if something was selected).
     * </p>
     */
    private void playPress() {
        String selected = getSelectedItem();

        // Do nothing if nothing was selected
        if (selected == null) {
            return;
        }

        // Switch views according to selected item
        boolean switched = viewSwitcher.replace(this, selected);

        // Start the requested service
        if (switched) {
            switch (selected) {
                case "Demo":
                    if (interactionMap.get(StartViewInteraction.START_DEMO) != null) {
                        notifyListeners(interactionMap.get(StartViewInteraction.START_DEMO));
                    }
                    break;
                case "Profinet":
                    if (interactionMap.get(StartViewInteraction.START_PROFINET) != null) {
                        notifyListeners(interactionMap.get(StartViewInteraction.START_PROFINET));
                    }
                    break;
                default:
                    if (interactionMap.get(StartViewInteraction.START_CAPTURE) != null) {
                        notifyListeners(interactionMap.get(StartViewInteraction.START_CAPTURE));
                    }
            }
        }
    }

    /**
     * <p>
     *     Returns the currently selected item from the list views.
     * </p>
     *
     * @return The currently selected item from the list views.
     */
    private String getSelectedItem() {
        String liveItem = liveListView.getSelectionModel().getSelectedItem();
        String captureItem = captureListView.getSelectionModel().getSelectedItem();

        // Make sure only 1 item is selected and check for live item selection
        if (liveItem != null && captureItem == null) {
            return liveItem;
        }

        // Make sure only 1 item is selected and check for capture item selection
        if (captureItem != null && liveItem == null) {
            return captureItem;
        }

        // No item selected
        return null;
    }

    @Override
    public void addCommand(StartViewInteraction interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }
}
