package de.fraunhofer.iosb.trufflehog.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

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
 *             Data log folder:
 *             <p>
 *                 This is where the DataLog objects are saved to.
 *             </p>
 *         </li>
 *         <br></br>
 *         <li>
 *             Config folder:
 *             <p>
 *                 This is where the property files for the GUIDataModel are saved to.
 *             </p>
 *         </li>
 *         <br></br>
 *         <li>
 *             Graph log Folder:
 *             <p>
 *                 This is where the logs created by the TruffleLogger for each Node are saved to.
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
 */
public class FileSystem {

    /**
     * <p>
     *     Returns the File object of the data folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the data folder.
     */
    public File getDataFolder() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Returns the File object of the data log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the data log folder.
     */
    public File getDataLogFolder() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Returns the File object of the config folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the config folder.
     */
    public File getConfigFolder() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Returns the File object of the graph log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the graph log folder.
     */
    public File getGraphLogFolder() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Returns the File object of the log folder, or creates the folder if it does not exist yet.
     * </p>
     *
     * @return The corresponding File object of the log folder.
     */
    public File getLogFolder() {
        throw new NotImplementedException();
    }
}

