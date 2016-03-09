
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

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import edu.kit.trufflehog.view.elements.ImageButton;
import eu.hansolo.enzo.notification.Notification;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

import static edu.kit.trufflehog.Main.getPrimaryStage;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl, Maximilian Diez
 * @version 1.0
 */
public class ViewBuilder {
    private Stage primaryStage;
    private MainViewController mainViewController;

    private OverlayViewController recordOverlayViewController;
    private OverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;

    private boolean settingsButtonPressed = false;
    private boolean filterButtonPressed = false;
    private boolean recordButtonPressed = false;

    private TableView tableView;

    public void build() {
        loadFonts();

        primaryStage = getPrimaryStage();
        mainViewController = new MainViewController("main_view.fxml");

        // Set up scene
        Scene mainScene = new Scene(mainViewController);

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
        buildSettingsOverlay();
        buildFilterMenuOverlay();
        buildRecordOverlay();
        startNotification();
    }

    private void buildSettingsOverlay() {
        settingsOverlayViewController = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(settingsOverlayViewController);
        AnchorPane.setBottomAnchor(settingsOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(settingsOverlayViewController, 18d);
        settingsOverlayViewController.setVisible(false);
    }

    private void buildFilterMenuOverlay() {
        // Build filter menu
        FilterOverlayMenu filterOverlayMenu = new FilterOverlayMenu();
        filterOverlayViewController = filterOverlayMenu.setUpOverlayViewController();
        tableView = filterOverlayMenu.setUpTableView();
        BorderPane borderPane = filterOverlayMenu.setUpMenu(tableView);

        // Add menu to overlay
        filterOverlayViewController.getChildren().add(borderPane);

        // Set up overlay on screen
        mainViewController.getChildren().add(filterOverlayViewController);
        AnchorPane.setBottomAnchor(filterOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(filterOverlayViewController, 18d);
        filterOverlayViewController.setMaxSize(330d, 210d);
        filterOverlayViewController.setVisible(false);
    }

    private void buildRecordOverlay() {
        recordOverlayViewController = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(recordOverlayViewController);
        AnchorPane.setBottomAnchor(recordOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(recordOverlayViewController, 18d);
        recordOverlayViewController.setVisible(false);
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
        Button settingsButton = new ImageButton(".." + File.separator + "gear.png");
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
        Button filterButton = new ImageButton(".." + File.separator + "filter.png");
        filterButton.setOnAction(event -> {
            settingsOverlayViewController.setVisible(false);
            filterOverlayViewController.setVisible(!filterButtonPressed);
            recordOverlayViewController.setVisible(false);
            filterButtonPressed = !filterButtonPressed;

            // Deselect anything that was selected
            if (!filterButtonPressed) {
                tableView.getSelectionModel().clearSelection();
            }
        });

        filterButton.setScaleX(0.8);
        filterButton.setScaleY(0.8);
        filterButton.setMaxSize(20, 20);
        filterButton.setMinSize(20, 20);

        return filterButton;
    }

    private Button buildRecordButton() {
        ImageButton recordButton = new ImageButton(".." + File.separator + "record.png");

        recordButton.setOnAction(event -> {
            settingsOverlayViewController.setVisible(false);
            filterOverlayViewController.setVisible(false);
            recordOverlayViewController.setVisible(!recordButtonPressed);
            recordButtonPressed = !recordButtonPressed;
        });

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
    }

    private void startNotification() {
        Notification.Notifier.INSTANCE.notifyInfo("Program started", "Congrats, you just started TruffleHog.");
    }

    private void loadFonts() {
        Font.loadFont(Main.class.getClassLoader().getResourceAsStream("fonts" + File.separator + "DroidSans" +
                File.separator + "DroidSans.ttf"), 12);
    }
}