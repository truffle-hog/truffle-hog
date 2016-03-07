package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.configdata.IConfigData;
import edu.kit.trufflehog.view.ViewBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledExecutorService;

/**
 * <p>
 *     The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *     with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *     TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {
    private static final Logger logger = LogManager.getLogger(Presenter.class);

    private static Presenter presenter;
    private final IConfigData configData;
    private final FileSystem fileSystem;
    private final ViewBuilder viewBuilder;
    private final ScheduledExecutorService executorService;

    /**
     * <p>
     *     Creates a new instance of a singleton Presenter or returns it if it was created before.
     * </p>
     *
     * @return A new instance of the Presenter, if none already exists.
     */
    public static Presenter createPresenter() {
        if (Presenter.presenter == null) {
            Presenter.presenter = new Presenter();
        }
        return presenter;
    }

    /**
     * <p>
     *     Creates a new instance of a Presenter.
     * </p>
     */
    private Presenter() {
        this.fileSystem = new FileSystem();
        this.executorService = new LoggedScheduledExecutor(10);
        this.viewBuilder = new ViewBuilder();

        IConfigData configDataTemp;
        try {
            configDataTemp = new ConfigDataModel(fileSystem, executorService);
        } catch (NullPointerException e) {
            configDataTemp = null;
            logger.error("Unable to set config data model", e);
        }
        configData = configDataTemp;
    }

    /**
     * <p>
     *     Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *     resources they need ect.
     * </p>
     */
    public void present() {
        viewBuilder.build();
    }
}
