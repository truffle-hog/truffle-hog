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

import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static edu.kit.trufflehog.Main.getPrimaryStage;

/**
 * <p>
 * </p>
 *
 * @author Maximilian Diez, Julian Brendl
 * @version 1.0
 */
public class ViewBuilder {
    private Stage primaryStage;
    private MainViewController mainViewController;

    private OverlayViewController recordOverlayMenu;
    private OverlayViewController filterOverlayMenu;
    private OverlayViewController settingsOverlayMenu;

    private boolean filterButtonPressed = false;

    public void build() {
        primaryStage = getPrimaryStage();
        mainViewController = new MainViewController("main_view.fxml");

        // Set up scene
        Scene mainScene = new Scene(mainViewController);
        mainScene.getStylesheets().add("@scrollbar.css");


        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(new Image(RootWindowController.class.getResourceAsStream("icon.png")));

        // Set min. dimensions
        primaryStage.setMinWidth(720d);
        primaryStage.setMinHeight(480d);

        primaryStage.show();

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);

        buildToolbar();
        buildGeneralStatisticsOverlay();
        buildNodeStatisticsOverlay();
        buildFilterMenuOverlay();
    }

    private void buildMenuOverlays() {
        settingsOverlayMenu = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(settingsOverlayMenu);
        AnchorPane.setBottomAnchor(settingsOverlayMenu, 10d);
        AnchorPane.setLeftAnchor(settingsOverlayMenu, 10d);
        settingsOverlayMenu.setVisible(false);

        recordOverlayMenu = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(recordOverlayMenu);
        AnchorPane.setBottomAnchor(recordOverlayMenu, 10d);
        AnchorPane.setLeftAnchor(recordOverlayMenu, 10d);
        recordOverlayMenu.setVisible(false);
    }

    private void buildFilterMenuOverlay() {
        filterOverlayMenu = new OverlayViewController("filter_menu_overlay.fxml");
        //filterOverlayMenu.getStylesheets().add("@scrollbar.css");
        ObservableList<FilterInput> data = FXCollections.observableArrayList();

        // Set up table view
        TableView tableView = new TableView();
        tableView.setEditable(true);
        tableView.setStyle("-fx-background-color:#000000");

        // Set up filter column
        TableColumn nameColumn = new TableColumn("Filter");
        nameColumn.setMinWidth(160);
        tableView.getColumns().add(nameColumn);
        nameColumn.setCellValueFactory(new PropertyValueFactory<FilterInput, String>("name"));
        nameColumn.setStyle("-fx-background-color:#000000");

        // Set up type column
        TableColumn typeColumn = new TableColumn("Type");
        typeColumn.setMinWidth(90);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(new PropertyValueFactory<FilterInput, String>("type"));
        typeColumn.setStyle("-fx-background-color:#000000");

        // Set up active column
        TableColumn activeColumn = new TableColumn<>("Active");
        activeColumn.setMinWidth(80);
        tableView.getColumns().add(activeColumn);
        activeColumn.setCellFactory(p -> new CheckBoxTableCell());
        activeColumn.setStyle("-fx-background-color:#000000");

        tableView.setItems(data);
        tableView.setMinWidth(328);

        // Set up add button
        Button btnNew = new Button("New Filter");
        btnNew.setOnAction(number -> {
            FilterInput filterInput = new FilterInput("Filter A", FilterType.BLACKLIST, null, null);
            data.add(filterInput);
        });

        // Set up remove button



        filterOverlayMenu.getChildren().addAll(tableView, btnNew);

        // Set
        mainViewController.getChildren().add(filterOverlayMenu);
        AnchorPane.setBottomAnchor(filterOverlayMenu, 60d);
        AnchorPane.setLeftAnchor(filterOverlayMenu, 18d);
        filterOverlayMenu.setVisible(false);
    }

    private void buildNodeStatisticsOverlay() {
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

    private void buildGeneralStatisticsOverlay() {
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainViewController.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);
    }

    private void buildToolbar() {
        Button settingsButton = buildSettingsButton();
        Button filterButton = buildFilterButton();
        Button recordButton = buildRecordButton();

        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton);
        mainViewController.getChildren().add(mainToolBarController);
        AnchorPane.setBottomAnchor(mainToolBarController, 5d);
        AnchorPane.setLeftAnchor(mainToolBarController, 5d);
    }

    private Button buildSettingsButton() {
        Button settingsButton = new ImageButton("gear.png");
        settingsButton.setOnAction(event -> {
            Stage settingsStage = new Stage();
            SettingsViewController settingsView = new SettingsViewController("settings_view.fxml");
            Scene settingsScene = new Scene(settingsView);
            settingsStage.setScene(settingsScene);
            settingsStage.show();

            // CTRL+W for closing
            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
                    , settingsStage::close);

            // CTRL+S triggers info about program settings saving
            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                    () -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Relax, no need to save anything here");
                        alert.setHeaderText(null);
                        alert.setContentText("Oops. Seems you wanted to save the configuration by pressing CTRL+S. This" +
                                " is not necessary thanks to the awesome always up-to-date saving design of TruffleHog.");
                        alert.showAndWait();
                    });
        });

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN),
                settingsButton::fire);

        settingsButton.setScaleX(0.8);
        settingsButton.setScaleY(0.8);

        return settingsButton;
    }

    private Button buildFilterButton() {
        Button filterButton = new ImageButton("filter.png");
        filterButton.setOnAction(event -> {
            filterOverlayMenu.setVisible(!filterButtonPressed);
            filterButtonPressed = !filterButtonPressed;
        });

        filterButton.setScaleX(0.8);
        filterButton.setScaleY(0.8);
        filterButton.setMaxSize(20, 20);
        filterButton.setMinSize(20, 20);

        return filterButton;
    }

    private Button buildRecordButton() {
        ImageButton recordButton = new ImageButton("record.png");

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
    }
}
