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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
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
    private static final Logger logger = LogManager.getLogger(SettingsDataModel.class);

    private FileSystem fileSystem;
    private ExecutorService executorService;
    private File settingsFile;
    private Map<Class, Map<String, StringProperty>> settingsMap = new HashMap<>();

    private static final String CONFIG_FILE_NAME = "system_config.xml";

    /**
     * <p>
     *     Creates a new SettingsDataModel object.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     * @param executorService The executor service used by TruffleHog to manage the multi-threading.
     * @throws NullPointerException Thrown when it was impossible to get the config file for some reason.
     */
    public SettingsDataModel(FileSystem fileSystem, ExecutorService executorService) throws NullPointerException {
        this.fileSystem = fileSystem;
        this.executorService = executorService;
        this.settingsFile = getSettingsFile();

        if (settingsFile == null) {
            throw new NullPointerException("Unable to get config file");
        }
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
            logger.error("Error while parsing the " + CONFIG_FILE_NAME + " file", e);
        }
    }

    /**
     * <p>
     *     Gets the settings file where all system settings are located. If none is found, it copies the default
     *     settings file from the resources folder into the data folder. If something goes wrong, null is returned.
     * </p>
     *
     * @return The settings file where all system settings are located
     */
    private File getSettingsFile() {
        // Get config file
        File configFile;
        try {
            configFile = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + CONFIG_FILE_NAME);
        } catch (IOException e) {
            logger.error("Unable to get canonical path to data/config folder", e);
            return null;
        }

        // If it is null, copy the config file from resources into the data/config
        if (!configFile.exists() && !copyConfigFileToDataFolder(configFile)) {
            return null;
        }

        return configFile;
    }

    /**
     * <p>
     *     Copies the system_config.xml from the resources folder into the data folder.
     * </p>
     *
     * @param configFile The file object to which to copy the config file
     * @return True if the copy operation was successful, else false
     */
    private boolean copyConfigFileToDataFolder(File configFile) {
        // Set file path to the default file in resources
        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = "edu" + File.separator + "kit" + File.separator + "trufflehog" + File.separator + "config"
                 + File.separator + CONFIG_FILE_NAME;

        // Get the file from the resources
        File resourceFile = null;
        URL url = classLoader.getResource(filePath);
        if (url != null) {

            // Decode from URL style to get rid of illegal characters in string like %20 etc.
            try {
                resourceFile = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("Unable to decode URL to string", e);
            }
        } else {
            logger.error("Unable to get config file from resources");
        }

        // Copy the file to data/config
        try {
            if (resourceFile != null) {
                Files.copy(resourceFile.getCanonicalFile().toPath(), configFile.getCanonicalFile().toPath());
            }
        } catch (IOException e) {
            logger.error("Unable to copy " + CONFIG_FILE_NAME + " from resources to data folder", e);
            return false;
        }
        return true;
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

            saveNewValue(typeClass, key, oldValue, newValue, document, entries.get(0));
        } catch (JDOMException | IOException e) {
            logger.error("Error updating the " + CONFIG_FILE_NAME + " file for key: " + key + " of type: "
                    + typeClass.getName(), e);
        }
    }

    /**
     * <p>
     *     Actually make the change to the XML and save it to the hard drive.
     * </p>
     *
     * @param typeClass The type of the value (i.e. String, Integer or Boolean are examples)
     * @param key The key mapped to the value, classic key value mapping.
     * @param oldValue The old value that should be updated.
     * @param newValue The new value, that should overwrite the old value.
     * @param document The document object that represent the parsed XML.
     * @param entry The entry that should be updated in the XML file.
     * @throws IOException Thrown if something goes wrong during the write operation.
     */
    private void saveNewValue(Class typeClass, String key, String oldValue, String newValue, Document document
            , Element entry) throws IOException {
        // Get the only one we found
        entry.getChild("value").setText(newValue);

        // Save the new XML to the file
        XMLOutputter xmlOutput = new XMLOutputter();
        PrintWriter writer = new PrintWriter(settingsFile, "UTF-8");
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(document, writer);
        writer.close();

        logger.debug("Changed key: " + key + " of type: " + typeClass.getName() + " from old value: " + oldValue
                + " to new value: " + newValue);
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
