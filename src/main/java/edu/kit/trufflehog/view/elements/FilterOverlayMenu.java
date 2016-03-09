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

import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.view.OverlayViewController;
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

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterOverlayMenu {
    private ObservableList<FilterInput> data = FXCollections.observableArrayList();

    /**
     *
     * @return
     */
    public OverlayViewController setUpOverlayViewController() {
        return new OverlayViewController("filter_menu_overlay.fxml");
    }

    /**
     *
     * @return
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
                FilterInput filterInput = (FilterInput) tableView.getItems().get(index);
                return filterInput.getBooleanProperty();
            });

            return checkBoxTableCell;
        });

        tableView.setItems(data);
        tableView.setMinWidth(330);

        // Set select/deselect on mouseclick
        tableView.setRowFactory(tableViewLambda -> {
            final TableRow<FilterInput> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)  ) {
                    tableView.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;
        });

        return tableView;
    }

    /**
     *
     * @param tableView
     * @return
     */
    public BorderPane setUpMenu(TableView tableView) {
        // Set up add button
        Button addButton = new ImageButton(".." + File.separator + "add.png");
        addButton.setOnAction(actionEvent -> {
            FilterInput filterInput = new FilterInput("Filter A", FilterType.BLACKLIST, null, null);
            data.add(filterInput);
            filterInput = new FilterInput("Filter B", FilterType.BLACKLIST, null, null);
            data.add(filterInput);
        });
        addButton.setScaleX(0.5);
        addButton.setScaleY(0.5);

        // Set up remove button
        Button removeButton = new ImageButton(".." + File.separator + "remove.png");
        removeButton.setOnAction(actionEvent -> {
            FilterInput filterInput = (FilterInput) tableView.getSelectionModel().getSelectedItem();
            data.remove(filterInput);
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
     *
     * @param filterInput
     */
    public void addFilter(FilterInput filterInput) {
        data.add(filterInput);
    }
}
