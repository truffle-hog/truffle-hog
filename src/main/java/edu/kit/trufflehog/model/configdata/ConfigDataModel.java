package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(ConfigDataModel.class);

    private final IConfigDataModel<StringProperty> settingsDataModel;

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
    public StringProperty getSetting(final Class typeClass, final String key) {
        return settingsDataModel.get(typeClass, key);
    }

    @Override
    public String getFilter(final String key) {
        return null;
    }
}
