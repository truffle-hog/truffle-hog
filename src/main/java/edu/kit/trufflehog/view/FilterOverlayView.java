package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.ImageButton;
import edu.uci.ics.jung.visualization.picking.PickedState;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;


/**
 * Created by root on 30.03.16.
 */
public class FilterOverlayView extends AnchorPaneController<FilterInteraction> {

    private final Map<FilterInteraction, IUserCommand> interactionMap = new EnumMap<>(FilterInteraction.class);

    private static final Logger logger = LogManager.getLogger();

    private final ObservableList<FilterInput> data;
    private final FilterEditingMenuViewController filterEditingMenu;
    private final TableView tableView;

    /**
     * <p>
     *     Creates a new FilterOverlayViewController with a {@link ConfigData} object. This is needed to access the database
     *     in order to save/remove/update filters.
     * </p>
     *
     * @param data The data that should be bound to the table view.
     * @param filterEditingMenu The filter edit menu through which filters can be created and updated.
     */
    public FilterOverlayView(final ObservableList<FilterInput> data,
                                       final FilterEditingMenuViewController filterEditingMenu) {

        super("filter_menu_overlay.fxml", new EnumMap<>(FilterInteraction.class));

        //this.pickedState = pickedState;
        this.filterEditingMenu = filterEditingMenu;
        this.data = data;

        //this.filterEditingMenu.setVisible(false);

        // Build the table view and the filter menu
        tableView = setUpTableView();
        BorderPane borderPane = setUpMenu(tableView);

        // Add menu to overlay
        this.getChildren().add(borderPane);
    }

    @Override
    public void addCommand(FilterInteraction interaction, IUserCommand command) {

        if (interaction == FilterInteraction.UPDATE) {
            interactionMap.put(interaction, command);
            data.stream().forEach(this::notifyUpdateCommand);
        } else {
            interactionMap.put(interaction, command);
        }
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
        final TableView<FilterInput> tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setMinWidth(522);
        tableView.setMinHeight(280);

        // Set up columns
        setUpFilterColumn(tableView);
        setUpTypeColumn(tableView);
        setUpOriginColumn(tableView);
        setUpColorColumn(tableView);
        setUpPriorityColumn(tableView);
        setUpLegalityColumn(tableView);
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
    private void setUpFilterColumn(TableView<FilterInput> tableView) {
        // Set up filter column
        final TableColumn<FilterInput, String> filterColumn = new TableColumn<>("Filter");
        filterColumn.setMinWidth(120);
        filterColumn.setPrefWidth(120);
        tableView.getColumns().add(filterColumn);
        filterColumn.setCellValueFactory(p -> p.getValue().getNameProperty());
    }

    /**
     * <p>
     *     Create the type column, which holds the type of the filter and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpTypeColumn(TableView<FilterInput> tableView) {
        // Set up type column
        final TableColumn<FilterInput, String> typeColumn = new TableColumn<>("Selection Model");
        typeColumn.setMinWidth(90);
        typeColumn.setPrefWidth(90);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(p -> p.getValue().getSelectionModelProperty());
    }

    /**
     * <p>
     *     Create the origin column, which holds the type of the origin and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpOriginColumn(TableView<FilterInput> tableView) {
        // Set up origin column
        final TableColumn<FilterInput, String> originColumn = new TableColumn<>("Filtered By");
        originColumn.setMinWidth(90);
        originColumn.setPrefWidth(90);
        tableView.getColumns().add(originColumn);
        originColumn.setCellValueFactory(p -> p.getValue().getOriginProperty());
    }

    /**
     * <p>
     *     Create the color column, which holds the color and also creates a callback to the string property
     *     behind it.
     * </p>
     */
    private void setUpColorColumn(TableView<FilterInput> tableView) {
        final TableColumn<FilterInput, Color> colorColumn = new TableColumn<>("Color");
        colorColumn.setMinWidth(50);
        colorColumn.setPrefWidth(50);
        colorColumn.setSortable(false);
        tableView.getColumns().add(colorColumn);

        // Set the cell value factory for the color
        colorColumn.setCellValueFactory(p -> p.getValue().getColorProperty());

        // Set the cell factory for the color
        colorColumn.setCellFactory(param -> new TableCell<FilterInput, Color>() {

            final Rectangle rectangle = new Rectangle();

            @Override
            public void updateItem(Color item, boolean empty) {
                // Add the color to the row
                if (item != null) {
                    HBox hBox = new HBox();
                    hBox.getChildren().add(rectangle);
                    rectangle.setHeight(20);
                    rectangle.setWidth(30);
                    hBox.setAlignment(Pos.CENTER);

                    rectangle.setFill(item);

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
     */
    private void setUpActiveColumn(TableView<FilterInput> tableView) {
        // Set up active column
        TableColumn<FilterInput, Boolean> activeColumn = new TableColumn<>("Active");
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
                    notifyUpdateCommand(input);
                });

                return input.getActiveProperty();
            });

            return checkBoxTableCell;
        });
    }

    /**
     * <p>
     *     Create the legalality column, which holds the legality of a filter and also creates a callback to the string
     *     property behind it.
     * </p>
     */
    private void setUpLegalityColumn(TableView<FilterInput> tableView) {
        // Set up priority column
        final TableColumn<FilterInput, Boolean> legalColumn = new TableColumn<>("Authorized");
        legalColumn.setMinWidth(70);
        legalColumn.setPrefWidth(70);
        tableView.getColumns().add(legalColumn);
        legalColumn.setCellValueFactory(p -> p.getValue().getLegalProperty());
    }

    /**
     * <p>
     *     Create the priority column, which holds the priority of a filter and also creates a callback to the
     *     integer property behind it.
     * </p>
     */
    private void setUpPriorityColumn(TableView<FilterInput> tableView) {
        // Set up priority column
        final TableColumn<FilterInput, Integer> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setMinWidth(50);
        priorityColumn.setPrefWidth(50);
        tableView.getColumns().add(priorityColumn);
        priorityColumn.setCellValueFactory(p -> p.getValue().getPriorityProperty().asObject());
    }

    /**
     * <p>
     *     Sets up the OverlayMenu with all buttons from the existing table view.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     * @return A {@link BorderPane} containing the full menu.
     */
    private BorderPane setUpMenu(TableView<FilterInput> tableView) {

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

            throw new UnsupportedOperationException("not working yet");

/*            final Set<INode> selected = new HashSet<>(pickedState.getPicked());
            final List<String> filterStringList = selected.stream()
                    .map(node -> node.getAddress().toString())
                    .collect(Collectors.toList());
            filterEditingMenu.showMenu(filterStringList);*/
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

    public void notifyUpdateCommand(FilterInput filterInput) {
        if (interactionMap.get(FilterInteraction.UPDATE) != null) {
            IUserCommand command = interactionMap.get(FilterInteraction.UPDATE);
            command.setSelection(filterInput);
            notifyListeners(interactionMap.get(FilterInteraction.UPDATE));
        }
    }


}
