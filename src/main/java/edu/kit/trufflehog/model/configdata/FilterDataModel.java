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
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *     The FilterDataModel stores {@link FilterInput} objects into a sqlite database. FilterInput objects are used to
 *     create a new {@link IFilter}; they contain the core data a filter needs to be created: the user input.
 * </p>
 * <p>
 *     This class manages the storage of these FilterInput objects. It contains a list with all existing FilterInput
 *     objects and has the ability to add, remove get and update these FilterInput objects from a database. As an
 *     implementation for the database drivers a JDBC variant was chosen that is OS agnostic.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class FilterDataModel extends ConfigDataModel<FilterInput> {
    private static final Logger logger = LogManager.getLogger();

    private final ExecutorService executorService;
    private final Map<String, FilterInput> loadedFilters;
    private final Connection connection;

    private static final String DATABASE_NAME = "filters.sql";

    /**
     * <p>
     *     Creates a new FilterDataModel.
     * </p>
     *
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     */
    public FilterDataModel(FileSystem fileSystem) {
        this.executorService = LoggedScheduledExecutor.getInstance();

        // Not sure why this map has to be concurrent, but in the unit tests I got concurrent hash map exceptions when
        // it was not. Perhaps the database library is asynchronous, though I am not sure how that would affect this map.
        this.loadedFilters = new ConcurrentHashMap<>();

        // Get database file
        File databaseFile;
        try {
            databaseFile = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + DATABASE_NAME);
        } catch (IOException e) {
            databaseFile = new File(fileSystem.getConfigFolder().getAbsolutePath() + File.separator + DATABASE_NAME);
            logger.error("Unable to get canonical path to database, getting absolute path instead", e);
        }

        // Get database connection
        Connection connectionTemp;
        try {
            connectionTemp = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getCanonicalPath());
        } catch (SQLException | IOException e) {
            connectionTemp = null;
            logger.error("Unable to set connection for database", e);
        }
        connection = connectionTemp;

        // Make sure auto commit is on, so that queries will automatically updated the database
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("Unable to activate auto commits for database", e);
            }
        }

        // Create database if the database file is empty (the db file is created above with the connection no matter
        // what)
        if (databaseFile.length() == 0) {
            createDatabase();
        }

        loadFilters();
    }

    /**
     * <p>
     *     Loads all existing {@link FilterInput} objects from the database into a map with their name as they key.
     * </p>
     */
    private void loadFilters() {
        // Clear existing list because old content is no longer relevant
        loadedFilters.clear();

        if (connection == null) {
            logger.error("Unable to load filters from database, connection is null");
            return;
        }

        // This should not have be to synchronized, because this method never should have to be called more than once,
        // but because in can in theory be called more than once we do synchronize here just to be safe.
        synchronized (this) {
            // We use the try-with-resource statements here from Java 7
            try (Statement statement = connection.createStatement()) {
                try (ResultSet rs = statement.executeQuery("SELECT * FROM FILTERS;")) {
                    connection.commit();

                    // Iterate through all found entries in the database and add them to the map
                    while (rs.next()) {
                        String base64String = rs.getString("filter");
                        FilterInput filterInput = fromBase64(base64String);
                        if (filterInput != null) {
                            loadedFilters.put(filterInput.getName(), filterInput);
                        } else {
                            logger.error("Found null filter input object while loading from database, skipping");
                        }
                    }
                }
            } catch (SQLException e) {
                logger.error("Error while loading filter input objects from database into list", e);
            }
        }
    }

    /**
     * <p>
     *     Closes the connection to the data base on force. For more information see
     * </p>
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Unable to close filter database correctly. Data might be lost.", e);
        }
    }

    /**
     * <p>
     *     Updates a {@link FilterInput} entry in the database by deleting it and adding it again. The internal map is
     *     updated as well.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to update.
     */
    public void updateFilterInDatabase(FilterInput filterInput) {
        updateFilterInDatabase(filterInput, null);
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
    public void updateFilterInDatabase(final FilterInput filterInput, final String newName) {
        executorService.submit(() -> {
            removeFilterFromDatabaseSynchronous(filterInput);
            if (newName != null) {
                filterInput.setName(newName);
            }
            addFilterToDataBaseSynchronous(filterInput);
        });
    }

    /**
     * <p>
     *     Adds a {@link FilterInput} to the database asynchronously. The internal map is updated as well. The
     *     FilterInput object is stored as a base64 string and not as a {@link Clob} because the internal
     *     implementation of the database does not provide a CLOB implementation. However since it is OS agnostic,
     *     we decided to go with it anyway.
     * </p>
     * <p>
     *     If filterInput is null, nothing is added.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to add to the database.
     */
    public void addFilterToDatabaseAsynchronous(final FilterInput filterInput) {
        executorService.submit(() -> addFilterToDataBaseSynchronous(filterInput));
    }

    /**
     * <p>
     *     Adds a {@link FilterInput} to the database synchronously. The internal map is updated as well. The FilterInput
     *     object is stored as a base64 string and not as a {@link Clob} because the internal implementation of the
     *     database does not provide a CLOB implementation. However since it is OS agnostic, we decided to go with it
     *     anyway.
     * </p>
     * <p>
     *     If filterInput is null, nothing is added.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to add to the database.
     */
    private void addFilterToDataBaseSynchronous(FilterInput filterInput) {
        // Make sure connection is not null
        if (connection == null) {
            logger.error("Unable to add filter to database, connection is null");
            return;
        }

        // Make sure the given filter input is not null
        if (filterInput == null) {
            logger.error("Unable to add filter to database, filter input is null");
            return;
        }

        // Convert filterInput object into base64 string representation
        String filterBase64 = toBase64(filterInput);
        if (filterBase64 == null) {
            logger.error("Unable to add filter to database, base64 string is null");
            return;
        }

        // Add the base64 string into the database
        synchronized (this) {
            // We use the try-with-resource statements here from Java 7
            try (Statement statement = connection.createStatement()) {
                String sql = "INSERT INTO FILTERS(ID,FILTER) " + "VALUES('" + filterInput.getName() + "','"
                        + filterBase64 + "');";

                statement.executeUpdate(sql);
                connection.commit();

                // Only update the map if the database query was successful
                loadedFilters.put(filterInput.getName(), filterInput);
            } catch (SQLException e) {
                logger.error("Unable to add a filter to the database", e);
            }
        }
    }

    /**
     * <p>
     *     Removes a {@link FilterInput} from the database asynchronously. The internal map is updated as well.
     * </p>
     * <p>
     *     If filterInput is null, nothing is done.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to remove from the database.
     */
    public void removeFilterFromDatabaseAsynchronous(FilterInput filterInput) {
        executorService.submit(() -> removeFilterFromDatabaseSynchronous(filterInput));
    }

    /**
     * <p>
     *     Removes a {@link FilterInput} from the database synchronously. The internal map is updated as well.
     * </p>
     * <p>
     *     If filterInput is null, nothing is done.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to remove from the database.
     */
    private void removeFilterFromDatabaseSynchronous(FilterInput filterInput) {
        // Make sure connection is not null
        if (connection == null) {
            logger.error("Unable to remove filter from database, connection is null");
            return;
        }

        // Make sure the given filter input is not null
        if (filterInput == null) {
            logger.error("Unable to add filter to database, filter input is null");
            return;
        }

        // Remove the filterInput from the database
        synchronized (this) {
            // We use the try-with-resource statements here from Java 7
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE from FILTERS where ID='" + filterInput.getName()
                        + "';");
                connection.commit();

                // Only update the map if the database query was successful
                loadedFilters.remove(filterInput.getName());
            } catch (SQLException e) {
                logger.error("Unable to remove filter input " + filterInput.getName() + " from database", e);
            }
        }
    }

    /**
     * <p>
     *     Converts a {@link FilterInput} object into a base64 string, which is how it will be stored into the database.
     * </p>
     *
     * @param filterInput The {@link FilterInput} that should be converted into a base64 string.
     * @return The base64 string representing the FilterInput object.
     */
    private String toBase64(FilterInput filterInput) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(filterInput);
            oos.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch(IOException e) {
            logger.error("Unable to serialize filter input into a base64 string", e);
            return null;
        }
    }

    /**
     * <p>
     *     Converts a base64 string into a {@link FilterInput} object, so that the object can be recreated from the
     *     database.
     * </p>
     *
     * @param string The base64 string that should be converted back into a {@link FilterInput} object.
     * @return The original FilterInput object represented by the given base64 string.
     */
    private FilterInput fromBase64(String string) {
        try {
            byte[] data = Base64.getDecoder().decode(string);
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            FilterInput filterInput = (FilterInput) objectInputStream.readObject();
            objectInputStream.close();
            return filterInput;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Unable to convert received string into FilterInput object", e);
            return null;
        }
    }

    /**
     * <p>
     *     Creates a new sqlite database with an ID and a FILTER column. This is where the filters will be saved.
     * </p>
     */
    private void createDatabase() {
        // This is not synchronized because it should only be called one single time by the thread that creates this
        // database
        try (Statement statement = connection.createStatement()) {
            logger.debug("Creating new database..");

            String sql = "CREATE TABLE FILTERS" +
                    "(ID        TEXT    NOT NULL," +
                    " FILTER    TEXT    NOT NULL)";
            statement.executeUpdate(sql);
            connection.commit();

            logger.debug("Created new database successfully.");
        } catch (SQLException e) {
            logger.error("Unable to fill new filter database", e);
        }
    }

    /**
     * <p>
     *     Gets all loaded {@link FilterInput} objects. If none have been loaded yet, none are returned.
     * </p>
     *
     * @return The list of loaded {@link FilterInput} objects.
     */
    public Map<String, FilterInput> getAllFilters() {
        return loadedFilters;
    }

    @Override
    public FilterInput get(Class classType, String key) {
        return loadedFilters.get(key);
    }
}
