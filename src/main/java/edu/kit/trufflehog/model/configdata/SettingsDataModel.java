package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * <p>
 *     The SettingsDataModel manages all settings that are found in the settings menu of TruffleHog. It makes sure that
 *     when a setting is changed in real time, it is also updated right away on the hard drive.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class SettingsDataModel implements IConfigDataModel<StringProperty> {
    private static final Logger logger = LogManager.getLogger(ConfigDataModel.class);

    private FileSystem fileSystem;
    private ExecutorService executorService;
    private File settingsFile;
    private Map<Class, Map<String, StringProperty>> settingsMap = new HashMap<>();

    /**
     * <p>
     *     Creates a new SettingsDataModel object.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     * @param executorService The executor service used by TruffleHog to manage the multi-threading.
     * @throws FileNotFoundException Thrown when the config files are not found or when there are too many.
     */
    public SettingsDataModel(FileSystem fileSystem, ExecutorService executorService) throws FileNotFoundException {
        this.fileSystem = fileSystem;
        this.executorService = executorService;
        this.settingsFile = getSettingsFile();
    }

    /**
     * <p>
     *     Loads all settings found on the hard drive into memory.
     * </p>
     */
    @Override
    public void load() {
        settingsMap = new HashMap<>();

        try {
            // Set up the XML parser
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(settingsFile);
            Element rootElement = document.getRootElement();

            if (!checkHead(rootElement.getChild("head"))) {
                logger.error("Settings file does not match specified type");
                return;
            }

            // Get the "data" elements of the xml file
            Element data = rootElement.getChild("data");

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
     *     Checks that the head of the settings file matches the required file type. For example, if the head does
     *     not contain type = settings, it is likely that the rest of the file also does not match the expected format
     *     and false is returned.
     * </p>
     *
     * @param head The head segment of the settings file.
     * @return True if the head segment matched the required type, else false.
     */
    private boolean checkHead(Element head) {
        return head.getChild("type").getValue().equals("settings");
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
            settingsMap.put(typeClass, propertyMap);
        }

        StringProperty stringProperty = new SimpleStringProperty(value);
        stringProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                executorService.submit(() -> updateSettingsFile(typeClass, key, oldValue, newValue));
            }
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
        // synchronized because this always runs in its own thread

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(settingsFile);

            Element rootElement = document.getRootElement();

            // Get the "data" elements of the xml file
            Element data = rootElement.getChild("data");

            // Parse through all entries and find the right one
            List<Element> entries = data.getChildren();
            entries = entries.stream()
                    .filter(entry -> entry.getAttribute("type").getValue().equals(typeClass.getName())
                            && entry.getChild("key").getValue().equals(key)
                            && entry.getChild("value").getValue().equals(oldValue))
                    .collect(Collectors.toList());

            // Make sure we actually only found 1 entry
            if (entries.size() < 1) {
                logger.error("No entry was found for type: " + typeClass.getName() + ", key: " + key + ", value: "
                        + oldValue);
                return;
            } else if (entries.size() > 1) {
                logger.error("More than one entry was found for type: " + typeClass.getName() + ", key: " + key
                        + ", value: " + oldValue);
                return;
            }

            // Get the only one we found
            entries.get(0).getChild("value").setText(newValue);

            // Save the new XML to the file
            XMLOutputter xmlOutput = new XMLOutputter();
            PrintWriter writer = new PrintWriter(settingsFile, "UTF-8");
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, writer);
            writer.close();

            logger.debug("Changed key: " + key + " of type: " + typeClass.getName() + " from old value: " + oldValue
                    + " to new value: " + newValue);
        } catch (JDOMException | IOException e) {
            logger.error("Error updating the system_config.xml file for key: " + key + " of type: "
                    + typeClass.getName(), e);
        }
    }

    @Override
    public StringProperty get(Class typeClass, String key) {
        Map<String, StringProperty> propertyMap = settingsMap.get(typeClass);
        if (propertyMap != null) {
            return propertyMap.get(key);
        }

        return null;
    }
}
