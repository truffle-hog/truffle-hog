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
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.view.OverlayViewController;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.Collection;
import java.util.Map;

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
    private ObservableList<FilterInput> data;
    private ConfigDataModel configDataModel;

    /**
     * <p>
     *     Creates a new FilterOverlayMenu with a {@link ConfigDataModel} object. This is needed to access the database
     *     in order to save/remove/update filters.
     * </p>
     *
     * @param configDataModel The {@link ConfigDataModel} object used to save/remove/update filters to the database.
     */
    public FilterOverlayMenu(ConfigDataModel configDataModel) {
        this.configDataModel = configDataModel;
        this.data = FXCollections.observableArrayList();

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
        TableView tableView = new TableView();
        tableView.setEditable(true);

        // Set up filter column
        TableColumn nameColumn = new TableColumn("Filter");
        nameColumn.setMinWidth(158);
        tableView.getColumns().add(nameColumn);
        nameColumn.setCellValueFactory(new PropertyValueFactory<FilterInput, String>("name"));

        // Set up type column
        TableColumn typeColumn = new TableColumn("Type");
        typeColumn.setMinWidth(90);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(new PropertyValueFactory<FilterInput, String>("type"));

        // Set up active column
        TableColumn activeColumn = new TableColumn<>("Active");
        activeColumn.setMinWidth(80);
        tableView.getColumns().add(activeColumn);

        // Set up callback for CheckBoxTableCell
        activeColumn.setCellFactory(tableColumn -> {
            final CheckBoxTableCell<FilterInput, Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
            checkBoxTableCell.setSelectedStateCallback(index -> {
                final FilterInput filterInput = (FilterInput) tableView.getItems().get(index);
                BooleanProperty booleanProperty = filterInput.getBooleanProperty();

                // Make sure the filter is updated in the database when the active status changes
                booleanProperty.addListener((observable, oldValue, newValue) -> {
                    configDataModel.updateFilterInput(filterInput);
                });

                return booleanProperty;
            });

            return checkBoxTableCell;
        });

        tableView.setItems(data);
        tableView.setMinWidth(330);

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
     *     Sets up the OverlayMenu with all buttons from the existing table view.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     * @return A {@link BorderPane} containing the full menu.
     */
    public BorderPane setUpMenu(TableView tableView) {
        // Set up add button
        Button addButton = new ImageButton(".." + File.separator + "add.png");
        addButton.setOnAction(actionEvent -> {
            FilterInput filterInput = new FilterInput("Filter A", FilterType.BLACKLIST, null, null);
            addFilter(filterInput);
            filterInput = new FilterInput("Filter B", FilterType.BLACKLIST, null, null);
            addFilter(filterInput);
        });
        addButton.setScaleX(0.5);
        addButton.setScaleY(0.5);

        // Set up remove button
        Button removeButton = new ImageButton(".." + File.separator + "remove.png");
        removeButton.setOnAction(actionEvent -> {
            FilterInput filterInput = (FilterInput) tableView.getSelectionModel().getSelectedItem();
            data.remove(filterInput);
            configDataModel.removeFilterInput(filterInput);
        });
        removeButton.setScaleX(0.5);
        removeButton.setScaleY(0.5);

        // Set up components on overlay
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);

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
     *     Add a filter to the table view.
     * </p>
     *
     * @param filterInput The {@link FilterInput} object to add to the table view.
     */
    public void addFilter(FilterInput filterInput) {
        data.add(filterInput);
        configDataModel.addFilterInput(filterInput);
    }
}
