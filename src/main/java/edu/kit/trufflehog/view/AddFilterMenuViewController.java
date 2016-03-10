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
import edu.kit.trufflehog.interaction.OverlayInteraction;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterOrigin;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     The AddFilterMenuViewController is an overlay that slides in from the top center, similar to menus that ask you
 *     if you are really sure that you want to quit the app. It has a show and a hide method, both of which trigger the
 *     respective animations. It is used to add new filter.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AddFilterMenuViewController extends AnchorPaneController<OverlayInteraction> {
    private final FilterOverlayMenu filterOverlayMenu;
    private final TranslateTransition transitioShow;
    private final TranslateTransition transitionHide;
    private final StackPane stackPane;

    // FXML variables
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<FilterType> typeComboBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ComboBox<String> filterByComboBox;
    @FXML
    private TextArea rulesTextArea;

    @FXML
    private Text errorText;

    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button helpButton;

    /**
     * <p>
     *     Creates a new AddFilterMenuViewController. Through the AddFilterMenuViewController you can add filters.
     * </p>
     *
     * @param stackPane The stackPane to put the menu on.
     * @param fxml The fxml to load.
     */
    public AddFilterMenuViewController(StackPane stackPane, String fxml, FilterOverlayMenu filterOverlayMenu) {
        super(fxml);
        this.filterOverlayMenu = filterOverlayMenu;
        this.stackPane = stackPane;
        this.stackPane.getChildren().add(this);
        StackPane.setAlignment(this, Pos.TOP_CENTER);

        // Set up transition animation to show menu
        transitioShow = new TranslateTransition(Duration.seconds(0.5), this);
        transitioShow.setFromY(-450);
        transitioShow.setToY(0);

        // Set up transition animation to hide menu
        transitionHide = new TranslateTransition(Duration.seconds(0.5), this);
        transitionHide.setFromY(0);
        transitionHide.setToY(-450);

        // Hide by default
        stackPane.setVisible(false);
        this.setVisible(false);

        // Fill comboboxes
        typeComboBox.getItems().setAll(FilterType.BLACKLIST, FilterType.WHITELIST);
        filterByComboBox.getItems().setAll("IP-Address", "MAC-Address", "Current Selection");

        // Set the buttons
        createButton.setOnAction(eventHandler ->  addFilter());
        cancelButton.setOnAction(eventHandler -> cancel());
        helpButton.setOnAction(eventHandler -> help());
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation.
     * </p>
     */
    public void showMenu() {
        StackPane.setAlignment(this, Pos.TOP_CENTER);
        stackPane.setVisible(true);
        this.setVisible(true);
        transitioShow.play();
    }

    /**
     * <p>
     *     Hides the menu: starts the slide out animation.
     * </p>
     */
    public void hideMenu() {
        transitionHide.play();
        transitionHide.setOnFinished(event -> {
            stackPane.setVisible(false);
            this.setVisible(false);
        });
    }

    /**
     * <p>
     *     Cancels the add/edit filter operation and closes the window
     * </p>
     */
    private void cancel() {
        hideMenu();
    }

    /**
     * <p>
     *     Adds a new filter to the list.
     * </p>
     */
    private void addFilter() {
        FilterInput filterInput = createFilterInput();
        if (filterInput != null) {
            filterOverlayMenu.addFilter(filterInput);
            hideMenu();
        }
    }

    /**
     * <p>
     *     Creates a new FilterInput based on the input that is found in the menu. If data is missing or data does not
     *     match the required format, null is returned and the error field in the view is updated.
     * </p>
     *
     * @return The new FilterInput object, or null if the data in the menu was invalid or missing.
     */
    private FilterInput createFilterInput() {
        final String name = nameTextField.getText();
        final FilterType filterType = typeComboBox.getValue();
        final String filterOriginString = filterByComboBox.getValue();
        final Color color = colorPicker.getValue();
        final String rules = rulesTextArea.getText();

        // Check if name is valid
        Pattern namePattern = Pattern.compile("[A-Za-z0-9. _-]{1,50}");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            errorText.setText("The name you entered is not valid.");
            return null;
        }

        // Check if filter with this name already exists
        List<String> currentFilterNames = filterOverlayMenu.getAllFilterNames();
        if (currentFilterNames.contains(name)) {
            errorText.setText("The name already exists.");
            return null;
        }

        // Check if type is valid
        if (filterType == null) {
            errorText.setText("Please choose a filter type.");
            return null;
        }

        // Check if origin is valid
        if (filterOriginString == null) {
            errorText.setText("Please choose what you would like to filter by.");
            return null;
        }

        // Check if color is not null (should never be)
        if (color == null) {
            errorText.setText("Please choose a color.");
            return null;
        }

        // Map the string to the actual FilterOrigin object. This is done so that
        FilterOrigin filterOrigin;
        switch (filterOriginString) {
            case "IP-Address":
                filterOrigin = FilterOrigin.IP;
                break;
            case "MAC-Address":
                filterOrigin = FilterOrigin.MAC;
                break;
            case "Current Selection":
                filterOrigin = FilterOrigin.SELECTION;
                break;
            default:
                filterOrigin = null;
        }

        if (filterOrigin == null) {
            errorText.setText("Please choose what you would like to filter by.");
            return null;
        }

        // Check if rules are valid
        List<String> ruleList = processRules(rules, filterOrigin);
        if (ruleList == null) {
            errorText.setText("A rule you entered does not have the correct format.");
            return null;
        }

        // Convert JavaFX color to the java.awt color object
        java.awt.Color colorAwt = new java.awt.Color((int) color.getRed(), (int) color.getGreen(), (int) color.getBlue());

        return new FilterInput(name, filterType, filterOrigin, ruleList, colorAwt);
    }

    private List<String> processRules(String rules, FilterOrigin filterOrigin) {
        String[] ruleArray = rules.split(";");

        // Define MAC and IP regex
        String macRegex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        String ipRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4]" +
                "[0-9]|25[0-5])$";
        String submaskRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2" +
                "[0-4][0-9]|25[0-5])(\\/([0-9]|[1-2][0-9]|3[0-2]))$";

        // Set patterns according to MAC or IP
        Pattern pattern1, pattern2;
        if (filterOrigin.equals(FilterOrigin.IP)) {
            pattern1 = Pattern.compile(ipRegex);
            pattern2 = Pattern.compile(submaskRegex);
        } else {
            pattern1 = Pattern.compile(macRegex);
            pattern2 = null; // mac only requires one regex
        }

        // Check each rule to see whether it matches its regex
        for (String rule : ruleArray) {
            rule = rule.replaceAll("\\s+","");

            boolean match1 = pattern1.matcher(rule.toLowerCase()).matches();
            boolean match2 = false;
            if (pattern2 != null) {
                match2 = pattern2.matcher(rule.toLowerCase()).matches();
            }

            if (!match1 && !match2) {
                errorText.setText("A rule does not have the valid format.");
                return null;
            }
        }

        // If we got to here, everything should have passed
        return Arrays.asList(ruleArray);
    }

    private void help() {

    }

    @Override
    public void addCommand(OverlayInteraction interaction, IUserCommand command) {
    }
}
