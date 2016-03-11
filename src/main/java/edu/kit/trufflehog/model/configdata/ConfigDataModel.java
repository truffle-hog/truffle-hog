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

package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.filter.FilterInput;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *     The ConfigDataModel saves all configurations of TruffleHog into an xml file and continuously updates it.
 *     This is done through the JavaFX {@link Property} object using its bindings. Further more, filter options are
 *     stored separately from the xml file. At the start of the program, everything is loaded from the hard drive into
 *     memory.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ConfigDataModel implements IConfigData {
    private final IConfigDataModel<StringProperty> settingsDataModel;
    private final FilterDataModel filterDataModel;
    private final ExecutorService executorService;

    /**
     * <p>
     *     Creates a new ConfigDataModel object.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     * @param executorService The executor service used by TruffleHog to manage the multi-threading.
     * @throws NullPointerException Thrown when it was impossible to get config data for some reason.
     */
    public ConfigDataModel(final FileSystem fileSystem, final ExecutorService executorService) throws NullPointerException{
        this.settingsDataModel = new SettingsDataModel(fileSystem, executorService);
        this.filterDataModel = new FilterDataModel(fileSystem, executorService);
        this.executorService = executorService;

        // Load all settings on creation
        load();
    }

    /**
     * <p>
     *     Updates a {@link FilterInput} entry in the database by deleting it and adding it again.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to update.
     */
    public void updateFilterInput(final FilterInput filterInput) {
        filterDataModel.updateFilterInDatabase(filterInput);
    }

    /**
     * <p>
     *     Updates a {@link FilterInput} entry in the database by deleting it and adding it again.
     *     When the name changes, things get more complicated, because the database is index by names. Thus the old
     *     entry has to be removed before the new one is added. Since this has to be done synchronously, it requires
     *     an extra method, because the default is asynchronous.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to update.
     */
    public void updateFilterInput(final FilterInput filterInput, final String newName) {
        filterDataModel.updateFilterInDatabase(filterInput, newName);
    }

    /**
     * <p>
     *     Adds a {@link FilterInput} to the database.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to add to the database.
     */
    public void addFilterInput(final FilterInput filterInput) {
        filterDataModel.addFilterToDatabaseAsynchronous(filterInput);
    }

    /**
     * <p>
     *     Removes a {@link FilterInput} from the database.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to remove from the database.
     */
    public void removeFilterInput(final FilterInput filterInput) {
        filterDataModel.removeFilterFromDatabaseAsynchronous(filterInput);
    }

    /**
     * <p>
     *     Gets all loaded {@link FilterInput} objects. If none have been loaded yet, the method loads them first.
     * </p>
     *
     * @return The list of loaded {@link FilterInput} objects.
     */
    public Map<String, FilterInput> getAllLoadedFilters() {
        return filterDataModel.getAllFilters();
    }

    /**
     * <p>
     *     Loads all settings that are stored on the hard drive into the program.
     * </p>
     */
    private void load() {
        settingsDataModel.load();
        filterDataModel.load();

        // VERY IMPORTANT: This makes sure that we can map the filter activity state to a check box in the
        // table view in the filters menu
        filterDataModel.getAllFilters().forEach((name, filter) -> filter.load(this));
    }

    @Override
    public StringProperty getSetting(final Class typeClass, final String key) {
        return settingsDataModel.get(typeClass, key);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     The given name must be the same name as the name that is stored inside the name parameter of the
     *     {@link FilterInput} object.
     * </p>
     *
     * @param name The name that belongs to the FilterInput object that should be retrieved.
     * @return The FilterInput object that has the matching name.
     */
    @Override
    public FilterInput getFilter(final String name) {
        return filterDataModel.get(name);
    }
}
