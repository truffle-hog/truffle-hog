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
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.configdata.IConfig;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterOrigin;
import edu.kit.trufflehog.model.filter.SelectionModel;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.viewmodel.FilterViewModel;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.IntStream;

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
public class FilterEditingMenuViewController extends AnchorPaneController<FilterInteraction> {

    private static final Logger logger = LogManager.getLogger();

    private final IConfig config;
    private final FilterViewModel filterViewModel;
    private final TranslateTransition transitionShow;
    private final TranslateTransition transitionHide;
 //   private final StackPane stackPane;
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

    /**
     * <p>
     *     Creates a new FilterEditingMenuViewController. Through the FilterEditingMenuViewController you can add or
     *     edit filters.
     * </p>
     *
     * @param config The config object that is used to access configuration data from the configuration files.
     * @param filterViewModel The object that is used to create a new {@link FilterInput} object.
     */
    public FilterEditingMenuViewController(final IConfig config,
                                           final FilterViewModel filterViewModel) {

        super("filter_edit_menu_overlay.fxml", new EnumMap<>(FilterInteraction.class));

        // Load the labels
        this.config = config;
        SELECTION_LABEL = this.config.getProperty("SELECTION_LABEL");
        INVERSE_SELECTION_LABEL = this.config.getProperty("INVERSE_SELECTION_LABEL");
        MAC_LABEL = this.config.getProperty("MAC_LABEL");
        IP_LABEL = this.config.getProperty("IP_LABEL");
        NAME_LABEL = this.config.getProperty("NAME_LABEL");

        // Set up the filterViewModel
        this.filterViewModel = filterViewModel;
        this.filterViewModel.setErrorText(errorText);

        // Add menu to stackpane
       // this.stackPane = stackPane;
    //    this.stackPane.getChildren().add(this);
        StackPane.setAlignment(this, Pos.TOP_CENTER);

        // Set up transition animation to show menu
        transitionShow = new TranslateTransition(Duration.seconds(0.5), this);
        transitionShow.setFromY(-450);
        transitionShow.setToY(0);

        // Set up transition animation to hide menu
        transitionHide = new TranslateTransition(Duration.seconds(0.5), this);
        transitionHide.setFromY(0);
        transitionHide.setToY(-450);

        // Hide by default
        //stackPane.setVisible(false);
        //this.setVisible(false);

        // Fill combo-boxes
        selectionComboBox.getItems().setAll(SELECTION_LABEL, INVERSE_SELECTION_LABEL);
        filterByComboBox.getItems().setAll(IP_LABEL, MAC_LABEL, NAME_LABEL);

        // Set up the buttons
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
    void showMenu() {
        showMenu((FilterInput) null);
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation. This method should be called when a new filter should be
     *     created based on a previous selection.
     * </p>
     *
     * @param filterStringList The list of MAC addresses that should be included in the filter. These will be shown
     *                         in the rules text area.
     */
    void showMenu(List<String> filterStringList) {
        final String filterString = concatRules(filterStringList);
        rulesTextArea.setText(filterString);
        filterByComboBox.setValue(config.getProperty("MAC_LABEL"));
        showMenu();
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation. This method will out the form with the data of the given
     *     filter input, and should thus only be called if a filter input should be edited.
     * </p>
     *
     * @param filterInput The filterInput that should be updated.
     */
    void showMenu(FilterInput filterInput) {
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
            colorPicker.setValue(filterInput.getColor());

            // Display origin type correctly
            if (filterInput.getOrigin().equals(FilterOrigin.IP)) {
                filterByComboBox.setValue(IP_LABEL);
            } else if (filterInput.getOrigin().equals(FilterOrigin.MAC)) {
                filterByComboBox.setValue(MAC_LABEL);
            } else {
                filterByComboBox.setValue(NAME_LABEL);
            }

            priorityTextField.setText(filterInput.getPriority() + "");

            authorizedCheckBox.setSelected(filterInput.isLegal());

            rulesTextArea.setText(concatRules(filterInput.getRules()));
        } else {
            updatingFilter = null;
        }

        StackPane.setAlignment(this, Pos.TOP_CENTER);
    //    stackPane.setVisible(true);
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
    void hideMenu() {
        transitionHide.play();
        transitionHide.setOnFinished(event -> {
        //    stackPane.setVisible(false);
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
     *     Adds a new filter based on the data that the user entered, and dispatches commands to update the model
     *     accordingly.
     * </p>
     */
    private void addFilter() {
        final String name = nameTextField.getText();
        final String selectionModelString = selectionComboBox.getValue();
        final String filterOriginString = filterByComboBox.getValue();
        final Color color = colorPicker.getValue();
        final String priorityText = priorityTextField.getText();
        final String rules = rulesTextArea.getText();
        final boolean authorized = authorizedCheckBox.isSelected();

        FilterInput filterInput = filterViewModel.createFilterInput(name, selectionModelString, filterOriginString,
                color, priorityText, rules, authorized);

        if (filterInput != null) {

            final IUserCommand addCommand = getCommand(FilterInteraction.ADD);

            if (addCommand != null) {
                addCommand.setSelection(filterInput);
                notifyListeners(addCommand);
            } else {
                logger.warn("no add command for filter!!");
            }

            logger.debug("Added FilterInput: " + filterInput.getName() + " to table view and database.");
            clearMenu();
            hideMenu();
        }
    }

    /**
     * <p>
     *     Updates an existing filter based on the user input, and updates the graph accordingly (the model does not
     *     have to be updated because the values of the filter input are bound to the database through JavaFX property
     *     objects).
     * </p>
     */
    private void updateFilter(FilterInput filterInput) {
        final String name = nameTextField.getText();
        final String selectionModelString = selectionComboBox.getValue();
        final String filterOriginString = filterByComboBox.getValue();
        final Color color = colorPicker.getValue();
        final String priorityText = priorityTextField.getText();
        final String rules = rulesTextArea.getText();
        final boolean authorized = authorizedCheckBox.isSelected();

        FilterInput filterInputUpdated = filterViewModel.updateFilter(filterInput, name, selectionModelString,
                filterOriginString, color, priorityText, rules, authorized);

        // Notify the model that a filter has changed
        notifyUpdateCommand(filterInputUpdated);
        clearMenu();
        hideMenu();
    }

    /**
     * <p>
     *     This method opens a the filter manual page where more is explained about how the filters work.
     * </p>
     */
    private void help() {

    }

    /**
     * <p>
     *     Takes a list of rules and concatenates them in a matter that makes the string easily readable.
     * </p>
     *
     * @param rules The rules to concatenate to one string.
     * @return The rules when they are completely concatenated.
     */
    private String concatRules(List<String> rules) {
        final String[] ruleArray = rules.toArray(new String[rules.size()]);
        return IntStream.range(0, ruleArray.length)
                .mapToObj(i -> {
                    // Add a new line to every second element in the stream
                    if (i % 2 == 1) {
                        return ruleArray[i] + ";\n";
                    } else {
                        return ruleArray[i] + ";    ";
                    }
                })
                .reduce((currentRule, rule) -> currentRule += rule)
                .orElse("");
    }

    /**
     * <p>
     *     Send a command to update filters in the graph.
     * </p>
     * @param filterInput The filter to update in the graph.
     */
    void notifyUpdateCommand(FilterInput filterInput) {

        //throw new UnsupportedOperationException("not working yet");

        final IUserCommand updateCommand = getCommand(FilterInteraction.UPDATE);

        if (updateCommand != null) {
            updateCommand.setSelection(filterInput);
            notifyListeners(updateCommand);
        }
    }
}
