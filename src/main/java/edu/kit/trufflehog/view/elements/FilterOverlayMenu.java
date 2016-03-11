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

package edu.kit.trufflehog.view.elements;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterOrigin;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.view.AddFilterMenuViewController;
import edu.kit.trufflehog.view.OverlayViewController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *     The FilterOverlayMenu contains all loaded filters. Through it the user can add and remove filters. Further more,
 *     the added/removed/updated filters are automatically updated accordingly in the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterOverlayMenu {
    private static final Logger logger = LogManager.getLogger();

    private final ObservableList<FilterInput> data;
    private final ConfigDataModel configDataModel;
    private final AddFilterMenuViewController addFilterOverlayMenu;

    /**
     * <p>
     *     Creates a new FilterOverlayMenu with a {@link ConfigDataModel} object. This is needed to access the database
     *     in order to save/remove/update filters.
     * </p>
     *
     * @param configDataModel The {@link ConfigDataModel} object used to save/remove/update filters to the database.
     * @param stackPane The groundView of the app on which the add filter menu should be drawn.
     */
    public FilterOverlayMenu(ConfigDataModel configDataModel, StackPane stackPane) {
        this.configDataModel = configDataModel;
        this.data = FXCollections.observableArrayList();
        this.addFilterOverlayMenu = new AddFilterMenuViewController(stackPane, "add_filter_menu_overlay.fxml", this);

        // Load existing filters from hard drive into filter menu
        Map<String, FilterInput> filterInputMap = configDataModel.getAllLoadedFilters();
        Collection<FilterInput> filterInputList = filterInputMap.values();
        data.addAll(filterInputList);
    }

    /**
     * <p>
     *     Creates a new OverlayViewController.
     * </p>
     *
     * @return The new OverlayViewController.
     */
    public OverlayViewController setUpOverlayViewController() {
        return new OverlayViewController("filter_menu_overlay.fxml");
    }

    /**
     * <p>
     *     Sets up the entire TableView with all its functionalities.
     * </p>
     *
     * @return The created TableView.
     */
    public TableView setUpTableView() {
        // Set up table view
        final TableView tableView = new TableView();
        tableView.setEditable(true);
        tableView.setMinWidth(452);
        tableView.setMinHeight(280);

        // Set up columns
        setUpFilterColumn(tableView);
        setUpTypeColumn(tableView);
        setUpOriginColumn(tableView);
        setUpColorColumn(tableView);
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
     */
    private void setUpFilterColumn(TableView tableView) {
        // Set up filter column
        final TableColumn filterColumn = new TableColumn("Filter");
        filterColumn.setMinWidth(150);
        filterColumn.setPrefWidth(150);
        tableView.getColumns().add(filterColumn);
        filterColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, String>,
                ObservableValue<String>>() {

            // Set up callback for filter property
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FilterInput, String> p) {
                final FilterInput filterInput = p.getValue();
                final StringProperty stringProperty =  filterInput.getNameProperty();

                // Make sure the filter is updated in the database when the name changes
                stringProperty.addListener((observable, oldValue, newValue) -> {
                    filterInput.setName(newValue);

                    configDataModel.updateFilterInput(filterInput);
                    logger.debug("Updated FilterInput: " + filterInput.getName() + " to table view and database.");
                });

                return stringProperty;
            }
        });
    }

    /**
     * <p>
     *     Create the type column, which holds the type of the filter and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpTypeColumn(TableView tableView) {
        // Set up type column
        final TableColumn typeColumn = new TableColumn("Type");
        typeColumn.setMinWidth(90);
        typeColumn.setPrefWidth(90);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FilterInput, String>,
                ObservableValue<String>>() {

            // Set up callback for type property
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FilterInput, String> p) {
                final FilterInput filterInput = p.getValue();
                final StringProperty stringProperty =  filterInput.getTypeProperty();

                // Make sure the filter is updated in the database when the type changes
                stringProperty.addListener((observable, oldValue, newValue) -> {
                    if (newValue.equals(FilterType.WHITELIST.name())) {
                        filterInput.setType(FilterType.WHITELIST);
                    } else {
                        filterInput.setType(FilterType.BLACKLIST);
                    }

                    configDataModel.updateFilterInput(filterInput);
                    logger.debug("Updated FilterInput: " + filterInput.getName() + " to table view and database.");
                });

                return stringProperty;
            }
        });
    }

    /**
     * <p>
     *     Create the origin column, which holds the type of the origin and also creates a callback to the string property
     *     behind it.
     * </p>
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
                final FilterInput filterInput = p.getValue();
                final StringProperty stringProperty =  filterInput.getOriginProperty();

                // Make sure the filter is updated in the database when the origin changes
                stringProperty.addListener((observable, oldValue, newValue) -> {
                    if (newValue.equals(FilterOrigin.IP.name())) {
                        filterInput.setOrigin(FilterOrigin.IP);
                    } else if (newValue.equals(FilterOrigin.MAC.name())) {
                        filterInput.setOrigin(FilterOrigin.MAC);
                    } else {
                        filterInput.setOrigin(FilterOrigin.SELECTION);
                    }

                    configDataModel.updateFilterInput(filterInput);
                    logger.debug("Updated FilterInput: " + filterInput.getName() + " to table view and database.");
                });

                return stringProperty;
            }
        });
    }

    /**
     * <p>
     *     Create the color column, which holds the color and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpColorColumn(TableView tableView) {
        final TableColumn colorColumn = new TableColumn("Color");
        colorColumn.setMinWidth(50);
        colorColumn.setPrefWidth(50);
        colorColumn.setSortable(false);
        tableView.getColumns().add(colorColumn);
        colorColumn.setCellValueFactory(new PropertyValueFactory<FilterInput, Color>("color"));

        // SETTING THE CELL FACTORY FOR THE ALBUM ART
        colorColumn.setCellFactory(param -> new TableCell<FilterInput, Color>() {
            Rectangle rectangle = new Rectangle();

            @Override
            public void updateItem(Color item, boolean empty) {
                if (item != null) {
                    HBox hBox = new HBox();
                    hBox.getChildren().add(rectangle);
                    rectangle.setHeight(20);
                    rectangle.setWidth(30);
                    hBox.setAlignment(Pos.CENTER);

                    // Copy to JavaFX color
                    javafx.scene.paint.Color color = new javafx.scene.paint.Color(item.getRed(), item.getGreen(),
                            item.getBlue(), 1);
                    rectangle.setFill(color);

                    setGraphic(hBox);
                }
            }
        });
    }

    /**
     * <p>
     *     Create the active column, which holds the activity state and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpActiveColumn(TableView tableView) {
        // Set up active column
        TableColumn activeColumn = new TableColumn<>("Active");
        activeColumn.setMinWidth(70);
        activeColumn.setPrefWidth(70);
        tableView.getColumns().add(activeColumn);
        activeColumn.setSortable(false);

        // Set up callback for CheckBoxTableCell
        activeColumn.setCellFactory(tableColumn -> {
            final CheckBoxTableCell<FilterInput, Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
            checkBoxTableCell.setSelectedStateCallback(index -> {
                final FilterInput filterInput = (FilterInput) tableView.getItems().get(index);
                final BooleanProperty booleanProperty = filterInput.getBooleanProperty();

                // Make sure the filter is updated in the database when the active status changes
                booleanProperty.addListener((observable, oldValue, newValue) -> {
                    filterInput.setActive(newValue);

                    configDataModel.updateFilterInput(filterInput);
                    logger.debug("Updated FilterInput: " + filterInput.getName() + " to table view and database.");
                });

                return booleanProperty;
            });

            return checkBoxTableCell;
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
    public BorderPane setUpMenu(TableView tableView) {
        // Set up add button
        Button addButton = new ImageButton("add.png");
        addButton.setOnAction(actionEvent -> {
            addFilterOverlayMenu.showMenu();
        });
        addButton.setScaleX(0.5);
        addButton.setScaleY(0.5);

        // Set up remove button
        Button removeButton = new ImageButton("remove.png");
        removeButton.setOnAction(actionEvent -> {
            FilterInput filterInput = (FilterInput) tableView.getSelectionModel().getSelectedItem();
            if (!data.isEmpty() && filterInput != null) {
                data.remove(filterInput);
                configDataModel.removeFilterInput(filterInput);
                logger.debug("Removed FilterInput: " + filterInput.getName() + " from table view and database.");
            }
        });
        removeButton.setScaleX(0.5);
        removeButton.setScaleY(0.5);

        // Set up components on overlay
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        tableView.setMinHeight(300);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(addButton, removeButton);
        borderPane.setBottom(anchorPane);

        AnchorPane.setBottomAnchor(addButton, 0d);
        AnchorPane.setRightAnchor(addButton, 0d);
        AnchorPane.setBottomAnchor(removeButton, 0d);
        AnchorPane.setRightAnchor(removeButton, 30d);

        return borderPane;
    }

    /**
     * <p>
     *     Gets a list with all names of all filters currently loaded.
     * </p>
     *
     * @return A list with all names of all filters currently loaded.
     */
    public List<String> getAllFilterNames() {
        return data.stream()
                .map(FilterInput::getName)
                .collect(Collectors.toList());
    }

    /**
     * <p>
     *     Add a filter to the table view.
     * </p>
     *
     * @param filterInput The {@link FilterInput} object to add to the table view.
     */
    public void addFilter(FilterInput filterInput) {
        if (filterInput != null) {
            data.add(filterInput);
            configDataModel.addFilterInput(filterInput);

            logger.debug("Added FilterInput: " + filterInput.getName() + " to table view and database.");
        }
    }
}