/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.StartViewInteraction;
import edu.kit.trufflehog.model.configdata.IConfig;
import edu.kit.trufflehog.presenter.NetworkType;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *     The StartViewController is the first view the user sees. It cannot be clicked away like the other views, and
 *     it gives users an option of what to start. Once the user has selected a view port, the view is switched
 *     accordingly.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class StartViewController extends SplitableView<StartViewInteraction> {
    private final Map<StartViewInteraction, IUserCommand> interactionMap = new EnumMap<>(StartViewInteraction.class);

    private final String fxmlFileName;
    private final ViewSplitter viewSplitter;
    private final IConfig config;

    private ImageButton closeButton;

    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<String> liveListView;
    @FXML
    private ListView<String> captureListView;

    /**
     * <p>
     *     Creates a new StartViewController with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the StartViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     * @param liveItems The list of supported live network views that can be chosen.
     * @param captureItems The list of network tape captures that are able to be played back.
     * @param config The config object that is used to access configuration data from the configuration files.
     * @param viewSplitter The view splitter needed to replace the start view with the next view.
     */
    public StartViewController(final String fxmlFileName,
                               final List<NetworkType> liveItems,
                               final ObservableList<String> captureItems,
                               final IConfig config,
                               final ViewSplitter viewSplitter) {
        super(fxmlFileName);
        this.fxmlFileName = fxmlFileName;
        this.viewSplitter = viewSplitter;
        this.config = config;

        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);



        // Fill the list views
        liveListView.setItems(FXCollections.observableArrayList(liveItems.stream()
                .map(this::convertFromBaseNetwork)
                .collect(Collectors.toList())));
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
        final Text titleText = new Text(config.getProperty("SV_TITLE"));
        titleText.setId("start_view_title_text");

        final Text liveText = new Text(config.getProperty("SV_LIVE"));
        liveText.setId("start_view_sub_title_text");

        final Text captureText = new Text(config.getProperty("SV_CAPTURE"));
        captureText.setId("start_view_sub_title_text");

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

        closeButton = addCloseButton(viewSplitter);
        getChildren().add(closeButton);
        showCloseButton(false);

        // Set this anchor pane as the currently selected view when it is pressed
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> viewSplitter.setSelected(this));
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
    private void setOnClickCellFactory(final ListView<String> listView1, final ListView<String> listView2) {
        listView1.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listView1.requestFocus();
                if (!cell.isEmpty()) {
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
        NetworkType networkType = convertToBaseNetwork(selected);
        boolean switched = viewSplitter.replace(this, networkType);

        // Start the requested service
        if (switched) {
            if (networkType == NetworkType.DEMO && interactionMap.get(StartViewInteraction.START_DEMO) != null) {
                notifyListeners(interactionMap.get(StartViewInteraction.START_DEMO));
            } else if (networkType == NetworkType.PROFINET && interactionMap.get(StartViewInteraction.START_PROFINET) != null) {
                notifyListeners(interactionMap.get(StartViewInteraction.START_PROFINET));
            } else if (networkType == NetworkType.CAPTURE && interactionMap.get(StartViewInteraction.START_CAPTURE) != null) {
                notifyListeners(interactionMap.get(StartViewInteraction.START_CAPTURE));
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

    /**
     * <p>
     *     Adds a close button.
     * </p>
     */
    private ImageButton addCloseButton(final ViewSplitter viewSplitter) {
        final ImageButton closeButton = new ImageButton("close.png");

        closeButton.setScaleX(0.8);
        closeButton.setScaleY(0.8);

        closeButton.setOnAction(event -> viewSplitter.merge(this));

        AnchorPane.setTopAnchor(closeButton, 5d);
        AnchorPane.setLeftAnchor(closeButton, 5d);

        return closeButton;
    }

    /**
     * <p>
     *     Converts a {@link NetworkType} to a nice string representation so that it can be beautifully displayed.
     * </p>
     *
     * @param networkType The network type to convert to a string
     * @return The converted string.
     */
    private String convertFromBaseNetwork(final NetworkType networkType) {
        if (networkType == NetworkType.START) {
            return config.getProperty("NT_START");
        } else if (networkType == NetworkType.DEMO) {
            return config.getProperty("NT_DEMO");
        } else if (networkType == NetworkType.PROFINET) {
            return config.getProperty("NT_PROFINET");
        } else if (networkType == NetworkType.CAPTURE) {
            return config.getProperty("NT_CAPTURE");
        }

        return "";
    }

    /**
     * <p>
     *     Converts a string to a {@link NetworkType}.
     * </p>
     *
     * @param string The string to convert to a NetworkType.
     * @return The resulting NetworkType.
     */
    private NetworkType convertToBaseNetwork(final String string) {
        if (string.equals(config.getProperty("NT_START"))) {
            return NetworkType.START;
        } else if (string.equals(config.getProperty("NT_DEMO"))) {
            return NetworkType.DEMO;
        } else if (string.equals(config.getProperty("NT_PROFINET"))) {
            return NetworkType.PROFINET;
        } else if (string.equals(config.getProperty("NT_CAPTURE"))) {
            return NetworkType.CAPTURE;
        }

        return NetworkType.NULL;
    }

    @Override
    public void showCloseButton(boolean show) {
        closeButton.setVisible(show);
    }

    @Override
    public void addCommand(StartViewInteraction interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }
}