package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
class FilterDataModel implements IConfigDataModel<FilterInput> {
    private static final Logger logger = LogManager.getLogger();

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
        this.loadedFilters = new HashMap<>();

        File databaseFile;
        try {
            databaseFile = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + DATABASE_NAME);
        } catch (IOException e) {
            databaseFile = new File(fileSystem.getConfigFolder().getAbsolutePath() + File.separator + DATABASE_NAME);
            logger.error("Unable to get canonical path to database, getting absolute path instead", e);
        }

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
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Unable to activate auto commits for database", e);
            }
        }

        // Create database if the database file is empty (the db file is created above with the connection no matter
        // what)
        if (databaseFile.length() == 0) {
            createDatabase();
        }
    }

    /**
     * <p>
     *     Loads all existing {@link FilterInput} objects from the database into a map with their name as they key.
     * </p>
     */
    private void loadFilters() {
        if (connection == null) {
            logger.error("Unable to load filters from database, connection is null");
            return;
        }

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM FILTERS;");
            if (rs.next()) {
                String base64String = rs.getString("filter");
                FilterInput filterInput = fromBase64(base64String);
                if (filterInput != null) {
                    loadedFilters.put(filterInput.getName(), filterInput);
                } else {
                    logger.error("Found null filter input object while loading from database, skipping");
                }
            }
        } catch (SQLException e) {
            logger.error("Error while loading filter input objects from database into list", e);
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
        removeFilterFromDatabase(filterInput);
        addFilterToDatabase(filterInput);
    }

    /**
     * <p>
     *     Adds a {@link FilterInput} to the database. The internal map is updated as well. The FilterInput object is
     *     stored as a base64 string and not as a {@link Clob} because the internal implementation of the database does
     *     not provide a CLOB implementation. However since it is OS agnostic, we decided to go with it anyway.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to add to the database.
     */
    public void addFilterToDatabase(FilterInput filterInput) {
        if (connection == null) {
            logger.error("Unable to add filter to database, connection is null");
            return;
        }

        String filterBase64 = toBase64(filterInput);

        if (filterBase64 == null) {
            logger.error("Unable to add filter to database, base64 string is null");
            return;
        }

        try {
            String sql = "INSERT INTO FILTERS(ID,FILTER) " +
                    "VALUES('" + filterInput.getName() + "','" + filterBase64 + "');";
            connection.createStatement().executeUpdate(sql);

            // Only update the map if the database query was successful
            loadedFilters.put(filterInput.getName(), filterInput);
        } catch (SQLException e) {
            logger.error("Unable to add a filter to the database", e);
        }
    }

    /**
     * <p>
     *     Removes a {@link FilterInput} from the database. The internal map is updated as well.
     * </p>
     *
     * @param filterInput The {@link FilterInput} to remove from the database.
     */
    public void removeFilterFromDatabase(FilterInput filterInput) {
        if (connection == null) {
            logger.error("Unable to remove filter from database, connection is null");
            return;
        }

        try {
            connection.createStatement().executeUpdate("DELETE from FILTERS where ID='"+ filterInput.getName() +"';");

            // Only update the map if the database query was successful
            loadedFilters.remove(filterInput.getName());
        } catch (SQLException e) {
            logger.error("Unable to remove filter input " + filterInput.getName() + " from database", e);
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
        try {
            logger.debug("Creating new database..");
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE FILTERS" +
                    "(ID        TEXT    NOT NULL," +
                    " FILTER    TEXT    NOT NULL)";
            statement.executeUpdate(sql);

            logger.debug("Created new database successfully.");
        } catch (SQLException e) {
            logger.error("Unable to fill new filter database", e);
        }
    }

    /**
     * <p>
     *     Gets all loaded {@link FilterInput} objects. If none have been loaded yet, the method loads them first.
     * </p>
     *
     * @return The list of loaded {@link FilterInput} objects.
     */
    public Map<String, FilterInput> getAllFilters() {
        if (loadedFilters.isEmpty()) {
            loadFilters();
        }
        return loadedFilters;
    }

    @Override
    public FilterInput get(Class classType, String key) {
        return loadedFilters.get(key);
    }

    @Override
    public void load() {
        loadFilters();
    }
}
