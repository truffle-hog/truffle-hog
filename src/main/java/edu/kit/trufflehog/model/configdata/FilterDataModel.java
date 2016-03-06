package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.filter.FilterInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *
 * </p>
 */
class FilterDataModel implements IConfigDataModel<FilterInput> {
    private static final Logger logger = LogManager.getLogger();

    private final FileSystem fileSystem;
    private final ExecutorService executorService;

    private static final String DATABASE_NAME = "filters.sql";


    public FilterDataModel(FileSystem fileSystem, ExecutorService executorService) {
        this.fileSystem = fileSystem;
        this.executorService = executorService;
    }

    public void createDatabase() {
        try {
            File file = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + DATABASE_NAME);
            file.createNewFile();
        } catch (IOException e) {
            logger.error("Unable to create filters database", e);
        }
    }

    @Override
    public FilterInput get(Class classType, String key) {
        return null;
    }

    @Override
    public void load() {

    }
}
