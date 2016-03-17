package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.command.usercommand.UpdateFilterCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.LiveUpdater;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkReadingPortSwitch;
import edu.kit.trufflehog.model.network.recording.INetworkViewPortSwitch;
import edu.kit.trufflehog.model.network.recording.INetworkWritingPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkDevice;
import edu.kit.trufflehog.model.network.recording.NetworkReadingPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkViewPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkWritingPortSwitch;
import edu.kit.trufflehog.service.NodeStatisticsUpdater;
import edu.kit.trufflehog.service.executor.CommandExecutor;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleCrook;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import edu.uci.ics.jung.graph.util.Graphs;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <p>
 *     The Presenter builds TruffleHog. It instantiates all necessary instances of classes, registers these instances
 *     with each other, distributes resources (parameters) to them etc. In other words it is the glue code of
 *     TruffleHog. There should always only be one instance of a Presenter around.
 * </p>
 */
public class Presenter {
    private static final Logger logger = LogManager.getLogger();

    private final ConfigData configData;
    private final FileSystem fileSystem;
    private final ScheduledExecutorService executorService;
    private final Stage primaryStage;
    private ViewBuilder viewBuilder;
    private TruffleReceiver truffleReceiver;
    private Map<String, INetworkViewPort> viewPortMap;
    private INetworkViewPortSwitch viewPortSwitch;
    private INetworkDevice networkDevice;
    private INetwork liveNetwork;
    private INetworkWritingPortSwitch writingPortSwitch;
    private final MacroFilter macroFilter = new MacroFilter();

    private final CommandExecutor commandExecutor = new CommandExecutor();

    /**
     * <p>
     *     Creates a new instance of a Presenter.
     * </p>
     */
    public Presenter(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.viewPortMap = new HashMap<>();

        if (this.primaryStage == null) {
            throw new NullPointerException("primary stage should not be null");
        }

        this.fileSystem = new FileSystem();
        this.executorService = LoggedScheduledExecutor.getInstance();

        ConfigData configDataTemp;
        try {
            configDataTemp = new ConfigData(fileSystem);
        } catch (NullPointerException e) {
            configDataTemp = null;
            logger.error("Unable to set config data model", e);
        }
        configData = configDataTemp;

        primaryStage.setOnCloseRequest(event -> finish());

        this.viewBuilder = new ViewBuilder(configData, this.primaryStage, this.viewPortMap);
    }

    /**
     * <p>
     *     Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *     resources they need ect.
     * </p>
     */
    public void present() {
        initNetwork();
        viewBuilder.build(viewPortSwitch, liveNetwork, networkDevice, commandExecutor.asUserCommandListener(),
                new UpdateFilterCommand(liveNetwork.getRWPort(), macroFilter));
    }

    private void initNetwork() {

        // initialize the live network that will be writte on by the receiver commands

        // create a Graph

        final Graph<INode, IConnection> graph = Graphs.synchronizedDirectedGraph(new DirectedSparseGraph<>());

        final ObservableUpdatableGraph<INode, IConnection> og = new ObservableUpdatableGraph<>(graph, new LiveUpdater());

        // TODO Ctor injection with the Ports that are within the networks
        liveNetwork = new LiveNetwork(og);

        // TODO Add real thing too, perhaps I misunderstood the viewport, need to talk to somebody in person ( - Julian)
        viewPortMap.put("Demo", liveNetwork.getViewPort());

        // TODO Where to put this???
/*        final Timeline updateTime = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            //refresh();
            //Platform.runLater(() -> repaint());
            liveNetwork.getViewPort().setViewTime(Instant.now().toEpochMilli());
        }));
        updateTime.setCycleCount(Timeline.INDEFINITE);
        updateTime.goReplay();*/

        /*
        // initialize the replay network that will be written on by a networkTape if the device plays a replay
        final INetwork replayNetwork = new ReplayNetwork(new ConcurrentDirectedSparseGraph<>());
*/
        // initialize the writing port switch that use the writing port of the live network
        // as their initial default writing port
        writingPortSwitch = new NetworkWritingPortSwitch(liveNetwork.getWritingPort());

        // initialize the reading port switch that uses the reading port of the live network
        // as its initial default reading port
        final INetworkReadingPortSwitch readingPortSwitch = new NetworkReadingPortSwitch(liveNetwork.getReadingPort());

        // initialize the view port switch that uses the view port of the live network
        // as its initial default view port
        viewPortSwitch = new NetworkViewPortSwitch(liveNetwork.getViewPort());

        // intialize the network device which is capable of recording and replaying any ongoing network session
        // on serveral screens
        networkDevice = new NetworkDevice();

        // create a new empty tape to record something on
        // Tell the network observation device to start recording the
        // given network with 25fps on the created tape

        final ExecutorService truffleFetchService = Executors.newSingleThreadExecutor();

        // TODO register the truffleReceiver somewhere so we can start or stop it.

        truffleReceiver = new TruffleCrook(writingPortSwitch, macroFilter);
        //truffleReceiver = new UnixSocketReceiver(writingPortSwitch, macroFilter);
        truffleFetchService.execute(truffleReceiver);
        truffleReceiver.connect();

        // Initialize the command executor and register it.
        final ExecutorService commandExecutorService = Executors.newSingleThreadExecutor();
        commandExecutorService.execute(commandExecutor);
        truffleReceiver.addListener(commandExecutor.asTruffleCommandListener());

        final ExecutorService nodeStatisticsUpdaterService = Executors.newSingleThreadExecutor();
        final NodeStatisticsUpdater nodeStatisticsUpdater = new NodeStatisticsUpdater(liveNetwork);
        nodeStatisticsUpdaterService.execute(nodeStatisticsUpdater);

        // goReplay that ongoing recording on the given viewportswitch
        //networkDevice.goReplay(tape, viewPortSwitch);

        // track the live network on the given viewportswitch
        networkDevice.goLive(liveNetwork, viewPortSwitch);
    }

    /**
     * This method shuts down any services that are still running properly.
     */
    public void finish() {
        // Exit the view
        Platform.exit();

        // Close all databases and other resources accessing the hard drive that need to be closed.
        configData.close();

        // Disconnect the truffleReceiver
        if (truffleReceiver != null) {
            truffleReceiver.disconnect();
        }

        // Kill all threads and the thread pool with it
        LoggedScheduledExecutor.getInstance().shutdownNow();

        // Shut down the system
        System.exit(0);
    }

    //TODO remove someday but for now leave as reference
   /* private void initGUI() {

        // setting up main window
        MainViewController mainView = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainView);
        RootWindowController rootWindow = new RootWindowController(primaryStage, mainScene, "icon.png", menuBar);
        //primaryStage.setScene(mainScene);
        //primaryStage.show();
        rootWindow.show();

        final Node node = new NetworkViewScreen(viewPort, 50);

        final AnchorPane pane = new AnchorPane();

        mainView.setCenter(pane);

        final Slider slider = new Slider(0, 100, 0);
        slider.setTooltip(new Tooltip("replay"));
        tape.getCurrentReadingFrameProperty().bindBidirectional(slider.valueProperty());
        tape.getFrameCountProperty().bindBidirectional(slider.maxProperty());

        final ToggleButton liveButton = new ToggleButton("Live");
        liveButton.setDisable(true);
        final ToggleButton playButton = new ToggleButton("Play");
        playButton.setDisable(false);
        final ToggleButton stopButton = new ToggleButton("Stop");
        stopButton.setDisable(false);
        final ToggleButton recButton = new ToggleButton("Rec");
        recButton.setDisable(false);

        liveButton.setOnAction(h -> {
            networkDevice.goLive(liveNetwork, viewPortSwitch);
            liveButton.setDisable(true);
        });

        playButton.setOnAction(handler -> {
            networkDevice.goReplay(tape, viewPortSwitch);
            liveButton.setDisable(false);
        });

        final IUserCommand startRecordCommand = new StartRecordCommand(networkDevice, liveNetwork, tape);
        recButton.setOnAction(h -> startRecordCommand.execute());

        slider.setStyle("-fx-background-color: transparent");

        final ToolBar toolBar = new ToolBar();
        toolBar.getItems().add(stopButton);
        toolBar.getItems().add(playButton);
        toolBar.getItems().add(recButton);
        toolBar.getItems().add(liveButton);
        toolBar.setStyle("-fx-background-color: transparent");
        //  toolBar.getItems().add(slider);

        final FlowPane flowPane = new FlowPane();

        flowPane.getChildren().addAll(toolBar, slider);

        mainView.setBottom(flowPane);

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
        ToolBarViewController mainToolBarController = new ToolBarViewController("main_toolbar.fxml");
        pane.getChildren().add(mainToolBarController);

        // setting up node statistics overlay
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");

        nodeStatisticsOverlay.add(new Label("Max Connection Size"), 0, 0);
        nodeStatisticsOverlay.add(new Label("Max Throughput"), 0, 1);

        final PlatformIntegerBinding maxConBinding = new PlatformIntegerBinding(viewPortSwitch.getMaxConnectionSizeProperty());
        final PlatformIntegerBinding maxThroughBinding = new PlatformIntegerBinding(viewPortSwitch.getMaxThroughputProperty());

        final Label connectionSizeLabel = new Label();
        connectionSizeLabel.textProperty().bind(maxConBinding.asString());

        final Label throughputLabel = new Label();
        throughputLabel.textProperty().bind(maxThroughBinding.asString());

        nodeStatisticsOverlay.add(connectionSizeLabel, 1, 0);
        nodeStatisticsOverlay.add(throughputLabel, 1, 1);



        pane.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);

    }*/

}