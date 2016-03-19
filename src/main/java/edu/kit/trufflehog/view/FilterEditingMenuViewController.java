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

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterOrigin;
import edu.kit.trufflehog.model.filter.SelectionModel;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>
 *     The FilterEditingMenuViewController is an overlay that slides in from the top center, similar to menus that ask you
 *     if you are really sure that you want to quit the app. It has a show and a hide method, both of which trigger the
 *     respective animations. It is used to add new Filters or update existing ones.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterEditingMenuViewController extends AnchorPaneController {
    private static final Logger logger = LogManager.getLogger();

    private final ConfigData configData;

    private final FilterOverlayViewController filterOverlayViewController;
    private final TranslateTransition transitionShow;
    private final TranslateTransition transitionHide;
    private final StackPane stackPane;
    private FilterInput updatingFilter; // The filter that is being updated if there is one.

    // FXML variables
    @FXML
    private TextField nameTextField;

    @FXML
    private ComboBox<String> selectionComboBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ComboBox<String> filterByComboBox;

    @FXML
    private CheckBox authorizedCheckBox;
    @FXML
    private TextField priorityTextField;
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

    // Labels
    private final String SELECTION_LABEL;
    private final String INVERSE_SELECTION_LABEL;

    private final String MAC_LABEL;
    private final String IP_LABEL;
    private final String NAME_LABEL;

    public ComboBox<String> getFilterByComboBox() {
        return filterByComboBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextArea getRulesTextArea() {
        return rulesTextArea;
    }

    /**
     * <p>
     *     Creates a new FilterEditingMenuViewController. Through the FilterEditingMenuViewController you can add or edit
     *     filters.
     * </p>
     *
     * @param stackPane The stackPane to put the menu on.
     * @param fxml The fxml to load.
     * @param filterOverlayViewController The filterOverlayViewController where the {@link TableView} is held that the
     *                                    filter should be added to.
     * @param configData The {@link ConfigData} object used to save/remove/update filters to the database.
     */
    public FilterEditingMenuViewController(StackPane stackPane,
                                           String fxml,
                                           FilterOverlayViewController filterOverlayViewController,
                                           ConfigData configData) {
        super(fxml);
        this.configData = configData;
        this.filterOverlayViewController = filterOverlayViewController;
        this.stackPane = stackPane;
        this.stackPane.getChildren().add(this);
        StackPane.setAlignment(this, Pos.TOP_CENTER);

        // Load the labels
        SELECTION_LABEL = configData.getProperty("SELECTION_LABEL");
        INVERSE_SELECTION_LABEL = configData.getProperty("INVERSE_SELECTION_LABEL");

        MAC_LABEL = configData.getProperty("MAC_LABEL");
        IP_LABEL = configData.getProperty("IP_LABEL");
        NAME_LABEL = configData.getProperty("NAME_LABEL");

        // Set up transition animation to show menu
        transitionShow = new TranslateTransition(Duration.seconds(0.5), this);
        transitionShow.setFromY(-450);
        transitionShow.setToY(0);

        // Set up transition animation to hide menu
        transitionHide = new TranslateTransition(Duration.seconds(0.5), this);
        transitionHide.setFromY(0);
        transitionHide.setToY(-450);

        // Hide by default
        stackPane.setVisible(false);
        this.setVisible(false);

        // Fill comboboxes
        selectionComboBox.getItems().setAll(SELECTION_LABEL, INVERSE_SELECTION_LABEL);
        filterByComboBox.getItems().setAll(IP_LABEL, MAC_LABEL, NAME_LABEL);

        // Set the buttons
        createButton.setOnAction(eventHandler ->  {
            if (updatingFilter == null) {
                addFilter();
            } else {
                updateFilter(updatingFilter);
            }
        });
        cancelButton.setOnAction(eventHandler -> cancel());
        helpButton.setOnAction(eventHandler -> help());
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation. This method should be called when a new filter should be
     *     created.
     * </p>
     */
    public void showMenu() {
       showMenu(null);
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation. This method will out the form with the data of the given
     *     filter input, and should thus only be called if a filter input should be edited.
     * </p>
     */
    public void showMenu(FilterInput filterInput) {
        if (filterInput != null) {
            updatingFilter = filterInput;
            nameTextField.setText(filterInput.getName());
            selectionComboBox.setValue(filterInput.getSelectionModel().name());

            // Display selection model correctly
            if (filterInput.getSelectionModel().equals(SelectionModel.SELECTION)) {
                selectionComboBox.setValue(SELECTION_LABEL);
            } else {
                selectionComboBox.setValue(INVERSE_SELECTION_LABEL);
            }

            // Convert to correct color object
            colorPicker.setValue(javafx.scene.paint.Color.rgb(filterInput.getColor().getRed(),
                    filterInput.getColor().getGreen(), filterInput.getColor().getBlue(), 1));

            // Display origin type correctly
            if (filterInput.getOrigin().equals(FilterOrigin.IP)) {
                filterByComboBox.setValue(IP_LABEL);
            } else if (filterInput.getOrigin().equals(FilterOrigin.MAC)) {
                filterByComboBox.setValue(MAC_LABEL);
            } else {
                filterByComboBox.setValue(NAME_LABEL);
            }

            priorityTextField.setText(filterInput.getPriority() + "");

            authorizedCheckBox.setSelected(filterInput.isAuthorized());

            rulesTextArea.setText(filterOverlayViewController.concatRules(filterInput.getRules()));
        } else {
            updatingFilter = null;
        }

        StackPane.setAlignment(this, Pos.TOP_CENTER);
        stackPane.setVisible(true);
        this.setVisible(true);
        transitionShow.play();
    }

    /**
     * <p>
     *     Clears the menu of all entries.
     * </p>
     */
    private void clearMenu() {
        nameTextField.setText("");
        selectionComboBox.setValue(null);
        colorPicker.setValue(Color.WHITE);
        filterByComboBox.setValue(null);
        priorityTextField.setText("");
        rulesTextArea.setText("");
        errorText.setText("");
        updatingFilter = null;
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
        clearMenu();
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
            filterOverlayViewController.addFilter(filterInput);
            clearMenu();
            hideMenu();
        }
    }

    /**
     * <p>
     *     Adds a new filter to the list.
     * </p>
     */
    private void updateFilter(FilterInput filterInput) {
        FilterInput filterInputUpdated = createFilterInput();

        if (filterInputUpdated == null) {
            return;
        }

        // Update name
        if (!filterInputUpdated.getName().equals(filterInput.getName())) {
            filterInput.getNameProperty().setValue(filterInputUpdated.getName());
        }

        // Update selection model
        if (!filterInputUpdated.getSelectionModel().equals(filterInput.getSelectionModel())) {
            filterInput.getSelectionModelProperty().setValue(filterInputUpdated.getSelectionModelProperty().getValue());
        }

        // Update color
        if (!filterInputUpdated.getColor().equals(filterInput.getColor())) {
            filterInput.getColorProperty().setValue(filterInputUpdated.getColorProperty().getValue());
        }

        // Update origin
        if (!filterInputUpdated.getOrigin().equals(filterInput.getOrigin())) {
            filterInput.getOriginProperty().setValue(filterInputUpdated.getOriginProperty().getValue());
        }

        // Update priority
        if (filterInputUpdated.getPriority() != filterInput.getPriority()) {
            filterInput.getPriorityProperty().setValue(filterInputUpdated.getPriorityProperty().getValue());
        }

        // Update authorization
        if (filterInputUpdated.isAuthorized() != filterInput.isAuthorized()) {
            filterInput.getAuthorizedProperty().setValue(filterInputUpdated.getAuthorizedProperty().getValue());
        }

        // Update rules and save to database, since they don't have a listener (since they are not shown in the table)
        if (!filterInputUpdated.getRules().equals(filterInput.getRules())) {
            filterInput.setRules(filterInputUpdated.getRules());

            configData.updateFilterInput(filterInput);
            logger.debug("Updated rules for FilterInput: " + filterInput.getName() + " to database.");
        }

        // Notify the model that a filter has changed
        filterOverlayViewController.notifyUpdateCommand(filterInput);
        clearMenu();
        hideMenu();
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
        final String selectionModelString = selectionComboBox.getValue();
        final String filterOriginString = filterByComboBox.getValue();
        final Color color = colorPicker.getValue();
        final String priorityText = priorityTextField.getText();
        final String rules = rulesTextArea.getText();
        final boolean authorized = authorizedCheckBox.isSelected();

        // Check if name is valid
        Pattern namePattern = Pattern.compile("[A-Za-z0-9. _-]{1,50}");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            errorText.setText(configData.getProperty("NAME_ERROR"));
            return null;
        }

        // Check if filter with this name already exists, but only if we are not editing a current filter (there the
        // name can be changed)
        List<String> currentFilterNames = filterOverlayViewController.getAllFilterNames();
        if (currentFilterNames.contains(name) && updatingFilter == null) {
            errorText.setText(configData.getProperty("NAME_ALREADY_EXISTS"));
            return null;
        }

        // Check for no input for selection model
        if (selectionModelString == null) {
            errorText.setText(configData.getProperty("SELECTION_MODEL_ERROR"));
            return null;
        }

        // Find the right selection model
        SelectionModel selectionModel;
        if (selectionModelString.equals(SELECTION_LABEL)) {
            selectionModel = SelectionModel.SELECTION;
        } else if (selectionModelString.equals(INVERSE_SELECTION_LABEL)) {
            selectionModel = SelectionModel.INVERSE_SELECTION;
        } else {
            selectionModel = null;
        }

        // We should never get here but just for good measure
        if (selectionModel == null) {
            errorText.setText(configData.getProperty("SELECTION_MODEL_ERROR"));
            return null;
        }

        // Check if color is not null (should never be)
        if (color == null) {
            errorText.setText(configData.getProperty("COLOR_ERROR"));
            return null;
        }

        // Check for no input for origin type
        if (filterOriginString == null) {
            errorText.setText(configData.getProperty("ORIGIN_ERROR"));
            return null;
        }

        // Find the right filter origin
        FilterOrigin filterOrigin;
        if (filterOriginString.equals(IP_LABEL)) {
            filterOrigin = FilterOrigin.IP;
        } else if (filterOriginString.equals(MAC_LABEL)) {
            filterOrigin = FilterOrigin.MAC;
        } else if (filterOriginString.equals(NAME_LABEL)) {
            filterOrigin = FilterOrigin.NAME;
        } else {
            filterOrigin = null;
        }

        // We should never get here but just for good measure
        if (filterOrigin == null) {
            errorText.setText(configData.getProperty("ORIGIN_ERROR"));
            return null;
        }

        // Check that priority is valid
        Pattern priorityPattern = Pattern.compile("[0-9]{1,3}");
        Matcher priorityMatcher = priorityPattern.matcher(priorityText);
        if (!priorityMatcher.matches()) {
            errorText.setText(configData.getProperty("PRIORITY_ERROR"));
            return null;
        }
        final int priority = Integer.parseInt(priorityText);

        if (priority < 0) {
            errorText.setText(configData.getProperty("PRIORITY_NOT_NEGATIVE"));
            return null;
        }

        // Check if rules are valid
        List<String> ruleList = processRules(rules, filterOrigin);
        if (ruleList == null) {
            return null;
        }

        // Convert JavaFX color to the java.awt color object
        java.awt.Color colorAwt = new java.awt.Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        FilterInput filterInput = new FilterInput(name, selectionModel, filterOrigin, ruleList, colorAwt, authorized, priority);
        filterInput.load(configData); // Binds properties to database

        return filterInput;
    }

    /**
     * <p>
     *     This method parses the rules string and makes sure that every rule is either MAC or IP conform, and not both.
     *     Rules cannot be mixed. That means either all rules are IP based or all rules are MAC based.
     * </p>
     *
     * @param rules The rules string from the menu that the user entered.
     * @param filterOrigin The origin type of the filter. In other words whether the filter filters by IP or MAC.
     * @return A list of parsed and valid rules, or null if a rule was not valid.
     */
    private List<String> processRules(String rules, FilterOrigin filterOrigin) {
        if (rules.equals("")) {
            errorText.setText(configData.getProperty("MISSING_RULE_ERROR"));
            return null;
        }

        // If the last character of the string is a semicolon, remove it
        String lastChar = rules.substring(rules.length() - 1);
        if (lastChar.equals(";")) {
            rules = rules.substring(0, rules.length() - 1);
        }

        // If the rules are not based on a name regex remove whitespaces and line breaks
        if (!filterOrigin.equals(FilterOrigin.NAME)) {
            rules = rules.replaceAll("\\s+", "").replaceAll("\n", "").replaceAll("\r", "");
        }

        String[] ruleArray = rules.split(";");

        // Define MAC and IP regex
        String macRegex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        String ipRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2" +
                "[0-4][0-9]|25[0-5])(\\/([0-9]|[1-2][0-9]|3[0-2]))?$";

        // Set patterns according to MAC or IP
        Pattern pattern = null;
        if (filterOrigin.equals(FilterOrigin.IP)) {
            pattern = Pattern.compile(ipRegex);
        } else if (filterOrigin.equals(FilterOrigin.MAC)) {
            pattern = Pattern.compile(macRegex);
        }

        // Check each rule to see whether it matches its regex
        for (String rule : ruleArray) {
            if (filterOrigin.equals(FilterOrigin.IP) || filterOrigin.equals(FilterOrigin.MAC)) {
                assert pattern != null;
                if (!pattern.matcher(rule).matches()) {
                    if (filterOrigin.equals(FilterOrigin.IP)) {
                        errorText.setText(configData.getProperty("INVALID_IP_RULE"));
                    } else {
                        errorText.setText(configData.getProperty("INVALID_MAC_RULE"));
                    }
                    return null;
                }
            } else {
                try {
                    Pattern.compile(rule);
                } catch (PatternSyntaxException exception) {
                    errorText.setText(configData.getProperty("INVALID_NAME_REGEX"));
                    return null;
                }
            }
        }

        // If we got here, everything should have passed
        return Arrays.asList(ruleArray);
    }

    /**
     * <p>
     *     This method opens a the filter manual page where more is explained about how the filters work.
     * </p>
     */
    private void help() {

    }
}
