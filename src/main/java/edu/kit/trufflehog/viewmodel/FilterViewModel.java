package edu.kit.trufflehog.viewmodel;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.model.filter.SelectionModel;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * <p>
 *     The FilterViewModel is the connection between the view and the model. It has access to view components as well
 *     as model components and is thus used to verify that the user input is correct in order to create a
 *     {@link FilterInput} object from it, and otherwise notifies the user through the view.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterViewModel {
    private static final Logger logger = LogManager.getLogger();

    private final ConfigData configData;
    private Text errorText;

    /**
     * <p>
     *     Creates a new FilterViewModel.
     * </p>
     *
     * @param configData The {@link ConfigData} object that is used to bind a filter input object to the database.
     */
    public FilterViewModel(ConfigData configData) {
        this.configData = configData;
    }

    /**
     * <p>
     *     Sets the error text field of the filter edit menu.
     * </p>
     *
     * @param text The error text field of the filter edit menu.
     */
    public void setErrorText(Text text) {
        this.errorText = text;
    }

    /**
     * <p>
     *     Updates an existing filter input object with the data that is given below.
     * </p>
     *
     * @param oldFilterInput The old filter that should be updated.
     * @param name The name of the filter.
     * @param selectionModelString The selection model that is used for this filter.
     * @param filterOriginString The filter origin of this filter.
     * @param color The color associated with this filter.
     * @param priorityText The priority of the filter, as a string.
     * @param rules The rules of a filter as a single string.
     * @param authorized The authorized state of the filter.
     * @return The updated FilterInput object.
     */
    public FilterInput updateFilter(final FilterInput oldFilterInput,
                                    final String name,
                                    final String selectionModelString,
                                    final String filterOriginString,
                                    final Color color,
                                    final String priorityText,
                                    final String rules,
                                    final boolean authorized) {

        final FilterInput filterInputUpdated = createFilterInput(name, selectionModelString, filterOriginString,
                color, priorityText, rules, authorized, true);

        if (filterInputUpdated == null) {
            return null;
        }

        // Update name
        if (!filterInputUpdated.getName().equals(oldFilterInput.getName())) {
            oldFilterInput.getNameProperty().setValue(filterInputUpdated.getName());
        }

        // Update selection model
        if (!filterInputUpdated.getSelectionModel().equals(oldFilterInput.getSelectionModel())) {
            oldFilterInput.getSelectionModelProperty().setValue(filterInputUpdated.getSelectionModelProperty().getValue());
        }

        // Update color
        if (!filterInputUpdated.getColor().equals(oldFilterInput.getColor())) {
            oldFilterInput.getColorProperty().setValue(filterInputUpdated.getColorProperty().getValue());
        }

        // Update origin
        if (!filterInputUpdated.getType().equals(oldFilterInput.getType())) {
            oldFilterInput.getFilterTypeProperty().setValue(filterInputUpdated.getFilterTypeProperty().getValue());
        }

        // Update priority
        if (filterInputUpdated.getPriority() != oldFilterInput.getPriority()) {
            oldFilterInput.getPriorityProperty().setValue(filterInputUpdated.getPriorityProperty().getValue());
        }

        // Update authorization
        if (filterInputUpdated.isLegal() != oldFilterInput.isLegal()) {
            oldFilterInput.getLegalProperty().setValue(filterInputUpdated.getLegalProperty().getValue());
        }

        // Update rules and save to database, since they don't have a listener (since they are not shown in the table)
        if (!filterInputUpdated.getRules().equals(oldFilterInput.getRules())) {
            oldFilterInput.setRules(filterInputUpdated.getRules());

            configData.updateFilterInput(oldFilterInput);
            logger.debug("Updated rules for FilterInput: " + oldFilterInput.getName() + " to database.");
        }

        return filterInputUpdated;
    }

    /**
     * <p>
     *     Creates a new FilterInput based on the input that is found in the menu. If data is missing or data does not
     *     match the required format, null is returned and the error field in the view is updated.
     * </p>
     *
     * @param name The name of the filter.
     * @param selectionModelString The selection model that is used for this filter.
     * @param filterOriginString The filter origin of this filter.
     * @param color The color associated with this filter.
     * @param priorityText The priority of the filter, as a string.
     * @param rules The rules of a filter as a single string.
     * @param authorized The authorized state of the filter.
     * @return The new FilterInput object, or null if the data in the menu was invalid or missing.
     */
    public FilterInput createFilterInput(final String name,
                                         final String selectionModelString,
                                         final String filterOriginString,
                                         final Color color,
                                         final String priorityText,
                                         final String rules,
                                         final boolean authorized) {
        return createFilterInput(name, selectionModelString, filterOriginString,
                color, priorityText, rules, authorized, false);
    }


    /**
     * <p>
     *     Creates a new FilterInput based on the input that is found in the menu. If data is missing or data does not
     *     match the required format, null is returned and the error field in the view is updated.
     * </p>
     *
     * @param name The name of the filter.
     * @param selectionModelString The selection model that is used for this filter.
     * @param filterOriginString The filter origin of this filter.
     * @param color The color associated with this filter.
     * @param priorityText The priority of the filter, as a string.
     * @param rules The rules of a filter as a single string.
     * @param authorized The authorized state of the filter.
     * @param updating Whether this filter is being updated or created from a new one.
     * @return The newly created FilterInput object.
     */
    private FilterInput createFilterInput(final String name,
                                          final String selectionModelString,
                                          final String filterOriginString,
                                          final Color color,
                                          final String priorityText,
                                          final String rules,
                                          final boolean authorized,
                                          final boolean updating) {
        // Check if name is valid
        Pattern namePattern = Pattern.compile("[A-Za-z0-9. _-]{1,50}");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            errorText.setText(configData.getProperty("NAME_ERROR"));
            return null;
        }

        // Check if filter with this name already exists, but only if we are not editing a current filter (there the
        // name can be changed)
        List<String> currentFilterNames = configData.getAllLoadedFilters().stream()
                .map(FilterInput::getName)
                .collect(Collectors.toList());

        if (currentFilterNames.contains(name) && !updating) {
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
        if (selectionModelString.equals(configData.getProperty("SELECTION_LABEL"))) {
            selectionModel = SelectionModel.SELECTION;
        } else if (selectionModelString.equals(configData.getProperty("INVERSE_SELECTION_LABEL"))) {
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
        FilterType filterType;
        if (filterOriginString.equals(configData.getProperty("IP_LABEL"))) {
            filterType = FilterType.IP;
        } else if (filterOriginString.equals(configData.getProperty("MAC_LABEL"))) {
            filterType = FilterType.MAC;
        } else if (filterOriginString.equals(configData.getProperty("NAME_LABEL"))) {
            filterType = FilterType.NAME;
        } else {
            filterType = null;
        }

        // We should never get here but just for good measure
        if (filterType == null) {
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
        List<String> ruleList = processRules(rules, filterType);
        if (ruleList == null) {
            return null;
        }

        FilterInput filterInput = new FilterInput(name, selectionModel, filterType, ruleList, color, authorized, priority);
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
     * @param filterType The origin type of the filter. In other words whether the filter filters by IP or MAC.
     * @return A list of parsed and valid rules, or null if a rule was not valid.
     */
    private List<String> processRules(String rules, FilterType filterType) {
        if (rules.equals("")) {
            errorText.setText(configData.getProperty("MISSING_RULE_ERROR"));
            return null;
        }

        // If the last character of the string is a semicolon, remove it
        String lastChar = rules.substring(rules.length() - 1);
        if (lastChar.equals(";")) {
            rules = rules.substring(0, rules.length() - 1);
        }

        // Split and remove any unwanted characters
        rules = rules.replaceAll("\n", "").replaceAll("\r", "");
        String[] ruleArray = rules.split(";");
        List<String> ruleList = Arrays.asList(ruleArray);
        ruleList = ruleList.stream()
                .map(String::trim)
                .filter(rule -> !rule.equals(""))
                .collect(Collectors.toList());

        // Define MAC and IP regex
        String macRegex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        String ipRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2" +
                "[0-4][0-9]|25[0-5])(\\/([0-9]|[1-2][0-9]|3[0-2]))?$";

        // Set patterns according to MAC or IP
        Pattern pattern = null;
        if (filterType.equals(FilterType.IP)) {
            pattern = Pattern.compile(ipRegex);
        } else if (filterType.equals(FilterType.MAC)) {
            pattern = Pattern.compile(macRegex);
        }

        // Check each rule to see whether it matches its regex
        for (String rule : ruleList) {
            if (filterType.equals(FilterType.IP) || filterType.equals(FilterType.MAC)) {
                assert pattern != null;
                if (!pattern.matcher(rule).matches()) {
                    if (filterType.equals(FilterType.IP)) {
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
        return ruleList;
    }
}
