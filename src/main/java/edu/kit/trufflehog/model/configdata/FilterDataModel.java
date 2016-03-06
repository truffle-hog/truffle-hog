package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.filter.FilterInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *
 * </p>
 */
class FilterDataModel implements IConfigDataModel<FilterInput> {
    private static final Logger logger = LogManager.getLogger();

    private final Map<String, FilterInput> loadedFilters;
    private final FileSystem fileSystem;
    private final ExecutorService executorService;
    private final File filterDatabase;
    private final Connection connection;

    private static final String DATABASE_NAME = "filters.sql";

    /**
     * <p>
     *
     * </p>
     *
     * @param fileSystem
     * @param executorService
     */
    public FilterDataModel(FileSystem fileSystem, ExecutorService executorService) {
        this.fileSystem = fileSystem;
        this.executorService = executorService;
        this.loadedFilters = new HashMap<>();

        File databaseTemp;
        try {
            databaseTemp = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + DATABASE_NAME);
        } catch (IOException e) {
            databaseTemp = new File(fileSystem.getConfigFolder().getAbsolutePath() + File.separator + DATABASE_NAME);
            logger.error("Unable to get canonical path to database, getting absolute path instead", e);
        }
        filterDatabase = databaseTemp;

        Connection connectionTemp;
        try {
            connectionTemp = DriverManager.getConnection("jdbc:sqlite:" + filterDatabase.getCanonicalPath());
        } catch (SQLException | IOException e) {
            connectionTemp = null;
            logger.error("Unable to set connection for database", e);
        }
        connection = connectionTemp;

        if (connection != null) {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Unable to activate auto commits for database", e);
            }
        }

        // Create database if the database file is empty (the db file is created above with the connection no matter
        // what)
        if (filterDatabase.length() == 0) {
            createDatabase();
        }
    }

    /**
     * <p>
     *
     * </p>
     */
    public void loadFilters() {
        if (connection == null) {
            logger.error("Unable to load filters from database, connection is null");
            return;
        }

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM FILTERS;");
            if (rs.next()) {
                String base64String = rs.getString("filter");
                FilterInput filterInput = fromBase64(base64String);
                loadedFilters.put(filterInput.getName(), filterInput);
            }
        } catch (SQLException e) {
            logger.error("Error while loading filter input objects from database into list", e);
        }
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param filterInput
     */
    public void updateFilterInDatabase(FilterInput filterInput) {
        removeFilterFromDatabase(filterInput);
        addFilterToDatabase(filterInput);
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param filterInput
     */
    public void addFilterToDatabase(FilterInput filterInput) {
        if (connection == null) {
            logger.error("Unable to add filter to database, connection is null");
            return;
        }

        String filterBase64 = toBase64(filterInput);

        if (filterBase64 == null) {
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
     *
     * </p>
     *
     * @param filterInput
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
     *
     * </p>
     *
     * @param filterInput
     * @return
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
     *
     * </p>
     *
     * @param string
     * @return
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
     *
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
     *
     * </p>
     *
     * @return
     */
    public Map<String, FilterInput> getAllFilters() {
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
