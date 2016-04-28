package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.BorderPaneController;
import edu.kit.trufflehog.viewmodel.GeneralStatisticsViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * <p>
 *     The StatisticsViewController provides GUI components and functionality for every statistic view.
 * </p>
 */
public class GeneralStatisticsViewController extends BorderPaneController {

    @FXML
    private TreeTableView<GeneralStatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoTable;

    private final GeneralStatisticsViewModel statViewModel;

    @FXML
    private TreeTableColumn<GeneralStatisticsViewModel.IEntry<StringProperty, ? extends Property>, String> keyColumn;

    @FXML
    private TreeTableColumn<GeneralStatisticsViewModel.IEntry<StringProperty, ? extends Property>, Object> valueColumn;

    /**
     * <p>
     *     Creates a new StatisticsViewController with the given fxmlFileName. The fxml file has to be in the same
     *     namespace as the StatisticsViewController.
     * </p>
     *
     */
    public GeneralStatisticsViewController(GeneralStatisticsViewModel statModel) {

        super("general_statistics_view.fxml");

        this.statViewModel = statModel;

        keyColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GeneralStatisticsViewModel.IEntry<StringProperty, ? extends Property>, String> param) ->
                param.getValue().getValue().getKeyProperty());

        valueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GeneralStatisticsViewModel.IEntry<StringProperty, ? extends Property>, Object> param) ->
                param.getValue().getValue().getValueProperty());

        infoTable.setRoot(statViewModel.getRootItem());
        infoTable.getColumns().setAll(keyColumn, valueColumn);
        infoTable.setShowRoot(false);
    }
}
