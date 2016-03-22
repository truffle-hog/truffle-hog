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
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.view.controllers.AnchorPaneInteractionController;
import edu.kit.trufflehog.view.elements.ImageButton;
import edu.uci.ics.jung.visualization.picking.PickedState;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *     The FilterOverlayViewController contains all loaded filters. Through it the user can add and remove filters.
 *     Further more, the added/removed/updated filters are automatically updated accordingly in the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterOverlayViewController extends AnchorPaneInteractionController<FilterInteraction> {
    private static final Logger logger = LogManager.getLogger();

    private final Map<FilterInteraction, IUserCommand> interactionMap = new EnumMap<>(FilterInteraction.class);

    private final PickedState<INode> pickedState;
    private final ObservableList<FilterInput> data;
    private final FilterEditingMenuViewController filterEditingMenu;
    private final TableView tableView;

    /**
     * <p>
     *     Creates a new FilterOverlayViewController with a {@link ConfigData} object. This is needed to access the database
     *     in order to save/remove/update filters.
     * </p>
     *
     * @param fxml The link to the fxml file that contains the base graphical layout for the filter overlay.
     * @param pickedState The picked state that is used to get the current selection of nodes.
     * @param data The data that should be bound to the table view.
     * @param filterEditingMenu The filter edit menu through which filters can be created and updated.
     */
    public FilterOverlayViewController(final String fxml,
                                       final PickedState<INode> pickedState,
                                       final ObservableList<FilterInput> data,
                                       final FilterEditingMenuViewController filterEditingMenu) {
        super(fxml);
        this.pickedState = pickedState;
        this.filterEditingMenu = filterEditingMenu;
        this.data = data;

        // Build the table view and the filter menu
        tableView = setUpTableView();
        BorderPane borderPane = setUpMenu(tableView);

        // Add menu to overlay
        this.getChildren().add(borderPane);
    }

    /**
     * <p>
     *     Sets up the entire TableView with all its functionalities.
     * </p>
     *
     * @return The created TableView.
     */
    private TableView setUpTableView() {
        // Set up table view
        final TableView tableView = new TableView();
        tableView.setEditable(true);
        tableView.setMinWidth(522);
        tableView.setMinHeight(280);

        // Set up columns
        setUpFilterColumn(tableView);
        setUpTypeColumn(tableView);
        setUpOriginColumn(tableView);
        setUpColorColumn(tableView);
        setUpPriorityColumn(tableView);
        setUpAuthorizedColumn(tableView);
        setUpActiveColumn(tableView);

        // Insert data from database into table
        tableView.setItems(data);

        // Set select/deselect on mouse click
        tableView.setRowFactory(tableViewLambda -> {
            final TableRow<FilterInput> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)) {
                    tableView.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;
        });

        return tableView;
    }

    /**
     * <p>
     *     Create the filter column, which holds the name of the filter and also creates a callback to the string property
     *     behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpFilterColumn(TableView tableView) {
        // Set up filter column
        final TableColumn filterColumn = new TableColumn("Filter");
        filterColumn.setMinWidth(120);
        filterColumn.setPrefWidth(120);
        tableView.getColumns().add(filterColumn);
        filterColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, String>,
                ObservableValue<String>>() {

            // Set up callback for filter property
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FilterInput, String> p) {
                return p.getValue().getNameProperty();
            }
        });
    }

    /**
     * <p>
     *     Create the type column, which holds the type of the filter and also creates a callback to the string property
     *     behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpTypeColumn(TableView tableView) {
        // Set up type column
        final TableColumn typeColumn = new TableColumn("Selection Model");
        typeColumn.setMinWidth(90);
        typeColumn.setPrefWidth(90);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, String>,
                ObservableValue<String>>() {

            // Set up callback for type property
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FilterInput, String> p) {
                return p.getValue().getSelectionModelProperty();
            }
        });
    }

    /**
     * <p>
     *     Create the origin column, which holds the type of the origin and also creates a callback to the string property
     *     behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpOriginColumn(TableView tableView) {
        // Set up origin column
        final TableColumn originColumn = new TableColumn("Filtered By");
        originColumn.setMinWidth(90);
        originColumn.setPrefWidth(90);
        tableView.getColumns().add(originColumn);
        originColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, String>,
                ObservableValue<String>>() {

            // Set up callback for origin property
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FilterInput, String> p) {
                return p.getValue().getOriginProperty();
            }
        });
    }

    /**
     * <p>
     *     Create the color column, which holds the color and also creates a callback to the string property
     *     behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpColorColumn(TableView tableView) {
        final TableColumn colorColumn = new TableColumn("Color");
        colorColumn.setMinWidth(50);
        colorColumn.setPrefWidth(50);
        colorColumn.setSortable(false);
        tableView.getColumns().add(colorColumn);

        // Set the cell value factory for the color
        colorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, Color>,
                ObservableValue<Color>>() {

            // Set up callback for origin property
            public ObservableValue<Color> call(TableColumn.CellDataFeatures<FilterInput, Color> p) {
                return p.getValue().getColorProperty();
            }
        });

        // Set the cell factory for the color
        colorColumn.setCellFactory(param -> new TableCell<FilterInput, Color>() {
            Rectangle rectangle = new Rectangle();

            @Override
            public void updateItem(Color item, boolean empty) {
                // Add the color to the row
                if (item != null) {
                    HBox hBox = new HBox();
                    hBox.getChildren().add(rectangle);
                    rectangle.setHeight(20);
                    rectangle.setWidth(30);
                    hBox.setAlignment(Pos.CENTER);

                    // Copy to JavaFX color
                    javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(item.getRed(), item.getGreen(),
                            item.getBlue(), 1);
                    rectangle.setFill(color);

                    setGraphic(hBox);
                }

                // Remove the color if the row has been removed
                if (empty) {
                    setGraphic(null);
                }
            }
        });
    }

    /**
     * <p>
     *     Create the active column, which holds the activity state and also creates a callback to the string property
     *     behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpActiveColumn(TableView tableView) {
        // Set up active column
        TableColumn activeColumn = new TableColumn<>("Active");
        activeColumn.setMinWidth(50);
        activeColumn.setPrefWidth(50);
        tableView.getColumns().add(activeColumn);
        activeColumn.setSortable(false);

        // Set up callback for CheckBoxTableCell
        activeColumn.setCellFactory(tableColumn -> {
            final CheckBoxTableCell<FilterInput, Boolean> checkBoxTableCell = new CheckBoxTableCell<>();

            checkBoxTableCell.setSelectedStateCallback(index -> {
                FilterInput input = (FilterInput) tableView.getItems().get(index);
                input.getActiveProperty().addListener(l -> {
                    filterEditingMenu.notifyUpdateCommand(input);
                });

                return input.getActiveProperty();
            });

            return checkBoxTableCell;
        });
    }

    /**
     * <p>
     *     Create the authorized column, which holds the "legality" of a filter and also creates a callback to the string
     *     property behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpAuthorizedColumn(TableView tableView) {
        // Set up priority column
        final TableColumn legalColumn = new TableColumn("Authorized");
        legalColumn.setMinWidth(70);
        legalColumn.setPrefWidth(70);
        tableView.getColumns().add(legalColumn);
        legalColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, Boolean>,
                ObservableValue<Boolean>>() {

            // Set up callback for priority property
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<FilterInput, Boolean> p) {
                return p.getValue().getAuthorizedProperty();
            }
        });
    }

    /**
     * <p>
     *     Create the priority column, which holds the priority of a filter and also creates a callback to the
     *     integer property behind it.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     */
    private void setUpPriorityColumn(TableView tableView) {
        // Set up priority column
        final TableColumn priorityColumn = new TableColumn("Priority");
        priorityColumn.setMinWidth(50);
        priorityColumn.setPrefWidth(50);
        tableView.getColumns().add(priorityColumn);
        priorityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, Integer>,
                ObservableValue<Integer>>() {

            // Set up callback for priority property
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<FilterInput, Integer> p) {
                return p.getValue().getPriorityProperty().asObject();
            }
        });
    }

    /**
     * <p>
     *     Sets up the OverlayMenu with all buttons from the existing table view.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     * @return A {@link BorderPane} containing the full menu.
     */
    private BorderPane setUpMenu(TableView tableView) {
        final Button addButton = setAddButton();
        final Button removeButton = setRemoveButton(tableView);
        final Button editButton = setEditButton(tableView);
        final Button selectionButton = setSelectionButton();

        // Set up components on overlay
        final BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        tableView.setMinHeight(300);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(addButton, removeButton, editButton, selectionButton);
        borderPane.setBottom(anchorPane);

        AnchorPane.setBottomAnchor(addButton, 0d);
        AnchorPane.setRightAnchor(addButton, 0d);
        AnchorPane.setBottomAnchor(removeButton, 0d);
        AnchorPane.setRightAnchor(removeButton, 30d);
        AnchorPane.setBottomAnchor(editButton, 0d);
        AnchorPane.setRightAnchor(editButton, 60d);
        AnchorPane.setBottomAnchor(selectionButton, 0d);
        AnchorPane.setRightAnchor(selectionButton, 95d);

        return borderPane;
    }

    /**
     * <p>
     *     Set up the selection button used to create a filter from a selection.
     * </p>
     *
     * @return The fully configured selection button.
     */
    private Button setSelectionButton() {
        final Button selectionButton = new ImageButton("select.png");
        selectionButton.setScaleX(0.5);
        selectionButton.setScaleY(0.5);

        selectionButton.setOnAction(actionEvent -> {
            final Set<INode> selected = new HashSet<>(pickedState.getPicked());
            final List<String> filterStringList = selected.stream()
                    .map(node -> node.getAddress().toString())
                    .collect(Collectors.toList());
            filterEditingMenu.showMenu(filterStringList);
        });

        return selectionButton;
    }

    /**
     * <p>
     *     Set up the edit button. This button is used to edit existing filters.
     * </p>
     *
     * @param tableView The table view needed to get the currently selected filter.
     * @return The fully configured edit button.
     */
    private Button setEditButton(TableView tableView) {
        final Button editButton = new ImageButton("edit.png");
        editButton.setOnAction(actionEvent -> {
            final FilterInput filterInput = (FilterInput) tableView.getSelectionModel().getSelectedItem();
            if (!data.isEmpty() && filterInput != null) {
                filterEditingMenu.showMenu(filterInput);
            }
        });
        editButton.setScaleX(0.45);
        editButton.setScaleY(0.45);
        return editButton;
    }

    /**
     * <p>
     *     Sets up the remove button, which is used to remove filters from the table view and the graph.
     * </p>
     *
     * @param tableView The table view needed to get the currently selected filter.
     * @return The fully configured remove button.
     */
    private Button setRemoveButton(TableView tableView) {
        final Button removeButton = new ImageButton("remove.png");
        removeButton.setOnAction(actionEvent -> {
            final FilterInput filterInput = (FilterInput) tableView.getSelectionModel().getSelectedItem();
            if (!data.isEmpty() && filterInput != null) {

                // Update model
                IUserCommand updateGraphFilterCommand = interactionMap.get(FilterInteraction.REMOVE);
                if (updateGraphFilterCommand != null) {
                    filterInput.setDeleted();
                    updateGraphFilterCommand.setSelection(filterInput);
                    notifyListeners(updateGraphFilterCommand);
                }
                logger.debug("Removed FilterInput: " + filterInput.getName() + " from table view and database.");
            }
        });
        removeButton.setScaleX(0.5);
        removeButton.setScaleY(0.5);
        return removeButton;
    }

    /**
     * <p>
     *     Sets up the add button, which is used to add filters to the graph and to the table view.
     * </p>
     *
     * @return The fully configured add button.
     */
    private Button setAddButton() {
        final Button addButton = new ImageButton("add.png");
        addButton.setOnAction(actionEvent -> filterEditingMenu.showMenu());
        addButton.setScaleX(0.5);
        addButton.setScaleY(0.5);
        return addButton;
    }

    @Override
    public void addCommand(FilterInteraction interaction, IUserCommand command) {
        if (interaction == FilterInteraction.UPDATE) {
            filterEditingMenu.addCommand(interaction, command);
            data.stream().forEach(filterEditingMenu::notifyUpdateCommand);
        } else {
            interactionMap.put(interaction, command);
        }
    }
}