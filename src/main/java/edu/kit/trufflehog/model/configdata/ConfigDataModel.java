package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import java.io.FileNotFoundException;
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
public class ConfigDataModel implements IConfig {
    private IConfigDataModel<StringProperty> settingsDataModel;

    /**
     * <p>
     *     Creates a new ConfigDataModel object.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     * @param executorService The executor service used by TruffleHog to manage the multi-threading.
     * @throws FileNotFoundException Thrown when the config files are not found or when there are too many.
     */
    public ConfigDataModel(FileSystem fileSystem, ExecutorService executorService) throws FileNotFoundException {
        settingsDataModel = new SettingsDataModel(fileSystem, executorService);
    }

    /**
     * <p>
     *     Loads all settings that are stored on the hard drive into the program.
     * </p>
     */
    public void load() {
        settingsDataModel.load();
        loadFilters();
    }

    /**
     * <p>
     *     Loads all filters found on the hard drive into memory.
     * </p>
     */
    private void loadFilters() {

    }

    @Override
    public StringProperty getSetting(Class typeClass, String key) {
        return settingsDataModel.get(typeClass, key);
    }

    @Override
    public String getFilter(String key) {
        return null;
    }
}
