package edu.kit.trufflehog.model;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.truffledatalog.TruffleLogger;
import edu.kit.trufflehog.service.replaylogging.ReplayLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * <p>
 *     The FileSystem class servers as a means to access data that is saved on the hard drive. It has getter methods
 *     for every directory that TruffleHog creates and uses. The following directories are:
 *     <ul>
 *         <li>
 *             Data folder:
 *             <p>
 *                 This is where all files and folders are created. It is the parent folder to every other file and
 *                 folder created by TruffleHog. If you need to save data, put it in here.
 *             </p>
 *         </li>
 *         <br></br>
 *         <li>
 *             Replay log folder:
 *             <p>
 *                 This is where the {@link ReplayLog} objects are saved to.
 *             </p>
 *         </li>
 *         <br></br>
 *         <li>
 *             Config folder:
 *             <p>
 *                 This is where the property files for the {@link ConfigDataModel} are saved to.
 *             </p>
 *         </li>
 *         <br></br>
 *         <li>
 *             Truffle data log Folder:
 *             <p>
 *                 This is where the logs created by the {@link TruffleLogger} for each Node are saved to.
 *             </p>
 *         </li>
 *         <br></br>
 *          <li>
 *             Log Folder:
 *             <p>
 *                 This is where actual logs with debug and error information etc. are saved to.
 *             </p>
 *         </li>
 *     </ul>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FileSystem {
    private static final Logger logger = LogManager.getLogger(FileSystem.class);

    private final File parentLocation;
    private File dataFolder = null;
    private File replayLogFolder = null;
    private File configFolder = null;
    private File truffleDataLogFolder = null;
    private File logFolder = null;

    /**
     * <p>
     *     Creates a new FileSystem object.
     * </p>
     */
    public FileSystem() {
        try {
            // Get parent folder
            parentLocation = new File(FileSystem.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath()).getParentFile();

            // Create folder where all data will be kept
            dataFolder = createFolder(dataFolder, "data", parentLocation);

            // Set path to log folder
            logFolder = new File(dataFolder.getCanonicalPath() + File.separator + "log").getCanonicalFile();
        } catch (IOException | URISyntaxException e) {
            logger.error("Unable to get current working directory's parent folder", e);
            throw new IllegalStateException("Unable to initialize TruffleHog file system");
        }
    }

    /**
     * <p>
     *     Returns the File object of the data folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the data folder.
     */
    public File getDataFolder() {
        dataFolder = createFolder(dataFolder, "data", parentLocation);

        return dataFolder;
    }

    /**
     * <p>
     *     Returns the File object of the replay log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the replay log folder.
     */
    public File getReplayLogFolder() {
        getDataFolder();
        replayLogFolder = createFolder(replayLogFolder, "replay_data", dataFolder);

        return replayLogFolder;
    }

    /**
     * <p>
     *     Returns the File object of the config folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the config folder.
     */
    public File getConfigFolder() {
        getDataFolder();
        configFolder = createFolder(configFolder, "config", dataFolder);

        return configFolder;
    }

    /**
     * <p>
     *     Returns the File object of the graph log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the graph log folder.
     */
    public File getTruffleDataLogFolder() {
        getDataFolder();
        truffleDataLogFolder = createFolder(truffleDataLogFolder, "truffle_data_log", dataFolder);

        return truffleDataLogFolder;
    }

    /**
     * <p>
     *     Returns the File object of the log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the log folder.
     * @throws FileNotFoundException Thrown if the log folder is not found - this could happen if there is an error
     *          with log4j, and the log or log folder was not created correctly
     */
    public File getLogFolder() throws FileNotFoundException {
        getDataFolder();
        if (logFolder == null || !logFolder.exists()) {
            throw new FileNotFoundException("Unable to find log folder - possible error with log4j");
        }

        return logFolder;
    }

    /**
     * <p>
     *     Creates a folder below its parent.
     * </p>
     *
     * @param folder The folder to create
     * @param name The name of the folder in case it does not exists yet
     * @param parentFolder The parent of the folder to create.
     * @return The created folder.
     */
    private File createFolder(File folder, String name, File parentFolder) {
        if (folder == null || !folder.exists()) {
            try {
                folder = new File(parentFolder.getCanonicalPath() + File.separator + name)
                        .getCanonicalFile();
                folder.mkdir();
            } catch (IOException e) {
                logger.error("Unable to create truffle_data_log folder", e);
            }
        }

        return folder;
    }
}