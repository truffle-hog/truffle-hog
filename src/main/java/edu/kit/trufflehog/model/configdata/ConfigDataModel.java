package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

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
public class ConfigDataModel implements IConfigDataModel {
    private static final Logger logger = LogManager.getLogger(ConfigDataModel.class);

    private FileSystem fileSystem;
    private ExecutorService executorService;
    private Map<Class, Map<String, StringProperty>> settingsMap = new HashMap<>();

    /**
     * <p>
     *     Creates a new ConfigDataModel object.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     * @param executorService The executor service used by TruffleHog to manage the multi-threading.
     */
    public ConfigDataModel(FileSystem fileSystem, ExecutorService executorService) {
        this.fileSystem = fileSystem;
        this.executorService = executorService;

        settingsMap = new HashMap<>();
    }

    /**
     * <p>
     *     Loads all settings that are stored on the hard drive into the program.
     * </p>
     *
     * @throws FileNotFoundException Thrown when the config files are not found or when there are too many.
     */
    public void load() throws FileNotFoundException {
        loadSettings();
        loadFilters();
    }

    /**
     * <p>
     *     Loads all filters found on the hard drive into memory.
     * </p>
     *
     * @throws FileNotFoundException Thrown when no filter files are found or when there are too many.
     */
    private void loadFilters() throws FileNotFoundException {

    }


    /**
     * <p>
     *     Loads all settings found on the hard drive into memory.
     * </p>
     *
     * @throws FileNotFoundException Thrown when the system_config.xml file is not found or when there is more than one.
     */
    private void loadSettings() throws FileNotFoundException {
        //File inputFile = new File("./src/test/resources/data/config/system_config.xml");

        // Get the settings file, if none is found or multiple are found, a FileNotFoundException is thrown
        File settingsFile = getSettingsFile();

        try {
            // Set up the XML parser
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(settingsFile);
            Element rootElement = document.getRootElement();

            // Get the "data" elements of the xml file
            List<Element> elements = rootElement.getChildren();
            Element data = elements.get(1);

            // Parse through all entries and add them to the map
            List<Element> entries = data.getChildren();
            entries.stream()
                    .forEach(entry -> {
                        // Get the type, key and value. The type has to be specified as the whole class name, otherwise
                        // it is set to java.lang.Object
                        String type = entry.getAttribute("type").getValue();
                        Class typeClass;
                        try {
                            typeClass = Class.forName(type);
                        } catch (ClassNotFoundException e) {
                            logger.error("Specified type class does not exist, setting the type class to java.lang.Object", e);
                            typeClass = Object.class;
                        }
                        String key = entry.getChild("key").getValue();
                        String value = entry.getChild("value").getValue();

                        // Finally add the data to the map
                        addToMap(typeClass, key, value);
                    });
        } catch (JDOMException | IOException e) {
            logger.error("Error while parsing the system_config.xml file", e);
        }
    }

    /**
     * <p>
     *     Gets the settings file where all system settings are located. If no file is found, or if too many files
     *     are found, FileNotFoundException is thrown.
     * </p>
     *
     * @return The settings file where all system settings are located
     * @throws FileNotFoundException Thrown when the system_config.xml file is not found or when there is more than one.
     */
    private File getSettingsFile() throws FileNotFoundException {
        File configFolder = fileSystem.getConfigFolder();
        File[] files = configFolder.listFiles();

        if (files == null) {
            throw new FileNotFoundException("system_config.xml file not found");
        }

        List<File> fileList = Arrays.asList(files);
        fileList = fileList.stream()
                .filter(file -> file.getName().equals("system_config.xml"))
                .collect(Collectors.toList());

        if (fileList.size() != 1) {
            throw new FileNotFoundException("More than one system_config.xml file found");
        }

        return fileList.get(0);
    }

    /**
     * <p>
     *     Adds an entry from the xml file into the settings map as a {@link StringProperty} along with a listener
     *     so that when it is updated, the underlying xml file can also be updated.
     * </p>
     * <p>
     *     Note that if no class matching the given type in the xml file was found, the typeClass is set to
     *     {@link Object}. Make sure the full path to the class is given in the xml file (e.g. java.lang.String instead
     *     of String).
     * </p>
     *
     * @param typeClass The type of the value (i.e. String, Integer or Boolean are examples)
     * @param key The key mapped to the value, classic key value mapping.
     * @param value The value that should be turned into a StringProperty and added to the map as such.
     */
    private void addToMap(final Class typeClass, final String key, final String value) {
        Map<String, StringProperty> propertyMap = settingsMap.get(typeClass);
        if (propertyMap == null) {
            propertyMap =  new HashMap<>();
        }

        StringProperty stringProperty = new SimpleStringProperty(value);
        stringProperty.addListener((observable, final oldValue, final newValue) -> {
            executorService.submit(() -> updateSettingsFile(typeClass, key, oldValue, newValue));
        });

        propertyMap.put(key, stringProperty);
    }

    /**
     * <p>
     *     Updates the settings xml file with the current value.
     * </p>
     *
     * @param typeClass The type of the value (i.e. String, Integer or Boolean are examples)
     * @param key The key mapped to the value, classic key value mapping.
     * @param oldValue The old value that should be updated.
     * @param newValue The new value, that should overwrite the old value.
     */
    private synchronized void updateSettingsFile(Class typeClass, String key, String oldValue, String newValue) {

    }

    @Override
    public StringProperty getSetting(String key, Class typeClass) {
        return settingsMap.get(typeClass).get(key);
    }

    @Override
    public String getFilter(String key) {
        return null;

    }
}
