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

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    public void build() {
        Stage primaryStage = getPrimaryStage();
        MainViewController mainViewController = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainViewController);
        primaryStage.setScene(mainScene);
        primaryStage.getIcons().add(new Image(RootWindowController.class.getResourceAsStream("icon.png")));
        primaryStage.show();

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);

        buildToolbar(primaryStage, mainViewController);
        buildGeneralStatisticsOverlay(mainViewController);
        buildNodeStatisticsOverlay(mainViewController);
    }

    private void buildNodeStatisticsOverlay(MainViewController mainViewController) {
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        mainViewController.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

    private void buildGeneralStatisticsOverlay(MainViewController mainViewController) {
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainViewController.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);
    }

    private void buildToolbar(Stage primaryStage, MainViewController mainViewController) {
        Button settingsButton = buildSettingsButton(primaryStage);
        Button filterButton = buildFilterButton();
        Button recordButton = buildRecordButton();

        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton);
        mainViewController.getChildren().add(mainToolBarController);
        AnchorPane.setBottomAnchor(mainToolBarController, 10d);
        AnchorPane.setLeftAnchor(mainToolBarController, 10d);
    }

    private Button buildSettingsButton(Stage primaryStage) {
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

        return settingsButton;
    }

    private Button buildFilterButton() {
         return new ImageButton("filter.png");
    }

    private Button buildRecordButton() {
        return new ImageButton("record.png");
    }
}
