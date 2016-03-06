package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.configdata.IConfigData;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import edu.kit.trufflehog.model.network.recording.*;
import edu.kit.trufflehog.service.executor.CommandExecutor;
import edu.kit.trufflehog.service.executor.TruffleExecutor;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleCrook;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver;
import edu.kit.trufflehog.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.kit.trufflehog.Main.getPrimaryStage;

/**
 * <p>
 *      The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *      with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *      TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {
    private static final Logger logger = LogManager.getLogger(Presenter.class);

    private CommandExecutor commandExecutor;

    private static Presenter presenter;
   // private final IConfigData configData;
   // private final FileSystem fileSystem;
   // private final ScheduledExecutorService executorService;

    private INetworkViewPort viewPort;

    /**
     * <p>
     *     Creates a new instance of a singleton Presenter or returns it if it was created before.
     * </p>
     *
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
/*        this.fileSystem = new FileSystem();
        this.executorService = new LoggedScheduledExecutor(10);

        IConfigData configDataTemp;
        try {
            configDataTemp = new ConfigDataModel(fileSystem, executorService);
        } catch (NullPointerException e ) {
            configDataTemp = null;
            logger.error("Unable to set config data model", e);
        }
        configData = configDataTemp;*/
    }

    /**
     *  <p>
     *      Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *      resources they need ect.
     *  </p>
     */
    public void present() {


        initNetwork();
        initGUI();
    }

    private void initNetwork() {

                // initialize the live network that will be writte on by the receiver commands

        // TODO Ctor injection with the Ports that are within the networks
        final INetwork liveNetwork = new LiveNetwork(new ConcurrentDirectedSparseGraph<>());

        // TODO Where to put this???
        final Timeline updateTime = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            //refresh();
            //Platform.runLater(() -> repaint());
            liveNetwork.getViewPort().setViewTime(Instant.now().toEpochMilli());
        }));
        updateTime.setCycleCount(Timeline.INDEFINITE);
        updateTime.play();

        /*
        // initialize the replay network that will be written on by a networkTape if the device plays a replay
        final INetwork replayNetwork = new ReplayNetwork(new ConcurrentDirectedSparseGraph<>());
*/
        // initialize the writing port switch that use the writing port of the live network
        // as their initial default writing port
        final INetworkWritingPortSwitch writingPortSwitch = new NetworkWritingPortSwitch(liveNetwork.getWritingPort());

        // initialize the reading port switch that uses the reading port of the live network
        // as its initial default reading port
        final INetworkReadingPortSwitch readingPortSwitch = new NetworkReadingPortSwitch(liveNetwork.getReadingPort());

        // initialize the view port switch that uses the view port of the live network
        // as its initial default view port
        final INetworkViewPortSwitch viewPortSwitch = new NetworkViewPortSwitch(liveNetwork.getViewPort());

        // intialize the network device which is capable of recording and replaying any ongoing network session
        // on serveral screens
        final INetworkDevice networkObservationDevice = new NetworkDevice();

        // create a new empty tape to record something on
        final INetworkTape tape = new NetworkTape(24);
        // Tell the network observation device to start recording the
        // given network with 25fps on the created tape
        //networkObservationDevice.record(liveNetwork.getViewPort(), tape, 25);

        final ExecutorService commandExecutorService = Executors.newSingleThreadExecutor();
        //final CommandExecutor commandExecutor = new CommandExecutor();
        //commandExecutorService.execute(commandExecutor);

        final ExecutorService truffleFetchService = Executors.newSingleThreadExecutor();
        final TruffleReceiver truffleReceiver = new TruffleCrook(writingPortSwitch, Collections.emptyList());
        truffleFetchService.execute(truffleReceiver);

        truffleReceiver.connect();

        final TruffleExecutor executor = new TruffleExecutor();
        commandExecutorService.execute(executor);
        //truffleReceiver.addListener(commandExecutor.asTruffleCommandListener());
        truffleReceiver.addListener(executor);

        commandExecutor = new CommandExecutor();
        ExecutorService ces = Executors.newSingleThreadExecutor();
        ces.execute(commandExecutor);

        // play that ongoing recording on the given viewportswitch
        //networkObservationDevice.play(tape, viewPortSwitch);

        // track the live network on the given viewportswitch
        networkObservationDevice.goLive(liveNetwork, viewPortSwitch);

        viewPort = viewPortSwitch;
    }


    private void initGUI() {

        Stage primaryStage = getPrimaryStage();

        // setting up main window
        MainViewController mainView = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainView);
        RootWindowController rootWindow = new RootWindowController(primaryStage, mainScene);
        //primaryStage.setScene(mainScene);
        //primaryStage.show();
        rootWindow.show();

       /* Platform.runLater(new Runnable() {

        });*/


        final AnchorPane pane = new AnchorPane();

        final Node node = new NetworkViewScreen(viewPort, 50, commandExecutor, pane);

        mainView.setCenter(pane);


        pane.getChildren().add(node);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);


        // setting up general statistics overlay
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        pane.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);

        // setting up menubar
        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml");
        pane.getChildren().add(mainToolBarController);

        // setting up node statistics overlay
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        pane.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
    }
}
