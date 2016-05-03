package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.command.usercommand.*;
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.interaction.ToolbarInteraction;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.LiveUpdater;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
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
import edu.kit.trufflehog.view.*;
import edu.kit.trufflehog.view.jung.visualization.FXVisualizationViewer;
import edu.kit.trufflehog.view.jung.visualization.SceneGestures;
import edu.kit.trufflehog.viewmodel.FilterViewModel;
import edu.kit.trufflehog.viewmodel.GeneralStatisticsViewModel;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import edu.uci.ics.jung.graph.util.Graphs;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private TruffleReceiver truffleReceiver;
    private INetworkViewPortSwitch viewPortSwitch;
    private INetworkReadingPortSwitch readingPortSwitch;
    private INetworkDevice networkDevice;
    private INetwork liveNetwork;
    private INetworkWritingPortSwitch writingPortSwitch;
    private final MacroFilter macroFilter = new MacroFilter();

    private FilterViewModel filterViewModel;

    private final CommandExecutor commandExecutor = new CommandExecutor();

    /**
     * <p>
     *     Creates a new instance of a Presenter.
     * </p>
     */
    public Presenter(Stage primaryStage) {
        this.primaryStage = primaryStage;

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

       // primaryStage.setOnCloseRequest(event -> finish());


    }


    /**
     * <p>
     *     Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *     resources they need ect.
     * </p>
     */
    public void run() {

        initModel();
        initServices();
        initDatabase();
        initGUI();

        //ObservableLayout<INode, IConnection> layouts = new ObservableLayout<>(new FRLayout<>(sgv.g));
        //layouts.setSize(new Dimension(300,300)); // sets the initial size of the space



       // truffleReceiver.connect();


        primaryStage.setOnCloseRequest(e -> {

            //viewer.setCache(false);

            Platform.exit();

            //final ExecutorService service = Executors.newSingleThreadExecutor();
            // Close all databases and other resources accessing the hard drive that need to be closed.
            configData.close();

            // Disconnect the truffleReceiver
            if (truffleReceiver != null) {
                truffleReceiver.disconnect();
            }

            // Kill all threads and the thread pool with it
            LoggedScheduledExecutor.getInstance().shutdownNow();

            System.gc();
            // Shut down the system
            System.exit(0);
        });

/*        viewBuilder.build(viewPortSwitch, liveNetwork, networkDevice, commandExecutor.asUserCommandListener(),
                new UpdateFilterCommand(liveNetwork.getRWPort(), macroFilter));*/
    }

    private void initModel() {

        initNetwork();

        filterViewModel = new FilterViewModel(configData);
    }

    private void initServices() {

        final ExecutorService truffleFetchService = Executors.newSingleThreadExecutor();

        // TODO register the truffleReceiver somewhere so we can start or stop it.
        // TODO remove this awful workaround catching an ERROR :o
        try {
            truffleReceiver = new UnixSocketReceiver(liveNetwork.getWritingPort(), macroFilter);
        } catch (UnsatisfiedLinkError e) {
            truffleReceiver = new TruffleCrook(liveNetwork.getWritingPort(), macroFilter);
        }


        //this.viewBuilder = new ViewBuilder(configData, this.primaryStage, this.viewPortMap, this.truffleReceiver);
        truffleFetchService.execute(truffleReceiver);


        // Initialize the command executor and register it.
        final ExecutorService commandExecutorService = Executors.newSingleThreadExecutor();
        commandExecutorService.execute(commandExecutor);
        truffleReceiver.addListener(commandExecutor.asTruffleCommandListener());

        final ExecutorService nodeStatisticsUpdaterService = Executors.newSingleThreadExecutor();
        final NodeStatisticsUpdater nodeStatisticsUpdater = new NodeStatisticsUpdater(readingPortSwitch, viewPortSwitch);
        nodeStatisticsUpdaterService.execute(nodeStatisticsUpdater);

    }

    private void initGUI() {

        final AnchorPane root = new AnchorPane();
        final Scene scene = new Scene(root);

        final FXVisualizationViewer viewer = new FXVisualizationViewer(liveNetwork.getViewPort().getDelegate(), liveNetwork.getViewPort());
        //final SceneGestures sceneGestures = new SceneGestures(viewer, viewer.getCanvas());
        root.getChildren().add(viewer);

        AnchorPane.setLeftAnchor(viewer, 0d);
        AnchorPane.setRightAnchor(viewer, 0d);
        AnchorPane.setBottomAnchor(viewer, 0d);
        AnchorPane.setTopAnchor(viewer, 0d);

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                viewer::refreshLayout);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN),
                viewer::selectAllNodes);
        //scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        //scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        //scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);

        final PacketDataViewController packetDataController = new PacketDataViewController(FXCollections.observableArrayList());
        packetDataController.setVisible(false);
        root.getChildren().add(packetDataController);
        AnchorPane.setLeftAnchor(packetDataController, 0d);

        final StatisticsViewModel statisticsViewModel = new StatisticsViewModel();
        final StatisticsViewController statisticsViewController = new StatisticsViewController(statisticsViewModel, primaryStage);
        root.getChildren().add(statisticsViewController);
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem getPackageItem = new MenuItem("Get Packages");

        //TODO: make more beautiful, add possibility of multiple node selection (if needed)
        getPackageItem.setOnAction(event -> {
            packetDataController.setVisible(true);
            PacketDataLoggingComponent loggingComponent = null;
            NodeInfoComponent tempInfoComponent = null;
            for (INode node:viewer.getPickedVertexState().getPicked()) {
                if (loggingComponent == null) {
                    loggingComponent = node.getComponent(PacketDataLoggingComponent.class);
                    tempInfoComponent = node.getComponent(NodeInfoComponent.class);
                } else {
                    break;
                }
            }

            final NodeInfoComponent infoComponent = tempInfoComponent;
            if (loggingComponent != null) {
                packetDataController.register(loggingComponent);

                if (infoComponent != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Node: ");
                    if (infoComponent.getDeviceName() != null) sb.append(infoComponent.getDeviceName());
                    if (infoComponent.getMacAddress() != null) {
                        sb.append(" ");
                        sb.append(infoComponent.getMacAddress().toString());
                    }
                    if (infoComponent.getIPAddress() != null) {
                        sb.append(" ");
                        sb.append(infoComponent.getIPAddress().toString());
                    }

                    packetDataController.setName(sb.toString());
                } else {
                    packetDataController.setName("Node: no info");
                }
            }
        });
        contextMenu.getItems().add(getPackageItem);
        AnchorPane.setTopAnchor(statisticsViewController, 10d);
        AnchorPane.setRightAnchor(statisticsViewController, 10d);
        viewer.addCommand(GraphInteraction.SELECTION, new SelectionCommand(statisticsViewModel));
        viewer.addCommand(GraphInteraction.SELECTION_CONTEXTMENU, new SelectionContextMenuCommand(viewer, contextMenu));
        viewer.addListener(commandExecutor.asUserCommandListener());

        // need to create a hashmap for filterinputs and filters for the session... maybe there will be another solution sometime
        final Map<FilterInput, IFilter> filterMap = new HashMap<>();

        final FilterEditingMenuView filterEditingMenuView = new FilterEditingMenuView(configData, filterViewModel);
        filterEditingMenuView.setVisible(false);
        AnchorPane.setRightAnchor(filterEditingMenuView, 0d);
        filterEditingMenuView.addCommand(FilterInteraction.ADD, new AddFilterCommand(configData, liveNetwork.getRWPort(), macroFilter, filterMap));
        filterEditingMenuView.addListener(commandExecutor.asUserCommandListener());

        final FilterOverlayView filterOverlayView = new FilterOverlayView(configData.getAllLoadedFilters(), filterEditingMenuView, viewer.getPickedVertexState());
        filterOverlayView.setVisible(false);
        root.getChildren().add(filterOverlayView);
        root.getChildren().add(filterEditingMenuView);
        AnchorPane.setLeftAnchor(filterOverlayView, 0d);
        filterOverlayView.addCommand(FilterInteraction.REMOVE, new RemoveFilterCommand(configData, liveNetwork.getRWPort(), macroFilter, filterMap));
        filterOverlayView.addCommand(FilterInteraction.UPDATE, new UpdateFilterCommand(configData, liveNetwork.getRWPort(), macroFilter, filterMap));
        filterOverlayView.addListener(commandExecutor.asUserCommandListener());

        final ToolbarView toolbarView = new ToolbarView(filterOverlayView);
        toolbarView.addCommand(ToolbarInteraction.CONNECT, new ConnectToSPPProfinetCommand(truffleReceiver));
        toolbarView.addCommand(ToolbarInteraction.DISCONNECT, new DisconnectSPPProfinetCommand(truffleReceiver));
        root.getChildren().add(toolbarView);
        AnchorPane.setBottomAnchor(toolbarView, 5d);
        AnchorPane.setLeftAnchor(toolbarView, 5d);
        toolbarView.addListener(commandExecutor.asUserCommandListener());



        final GeneralStatisticsViewModel generalStatisticsViewModel = new GeneralStatisticsViewModel();
        final GeneralStatisticsViewController generalStatisticsViewController = new GeneralStatisticsViewController(generalStatisticsViewModel);
        root.getChildren().add(generalStatisticsViewController);

        // FIXME This part is a bit verbose!!! _____________ START
        StringProperty timeProperty = new SimpleStringProperty("");
        StringProperty throughputStringProperty = new SimpleStringProperty();
        throughputStringProperty.bindBidirectional(viewPortSwitch.getThroughputProperty(), new DecimalFormat("0.00"));

        generalStatisticsViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel.StringEntry<>("Population", viewPortSwitch.getPopulationProperty())));
        generalStatisticsViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel.StringEntry<>("Packages per second", throughputStringProperty)));
        generalStatisticsViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel.StringEntry<>("Running", timeProperty)));
        //generalStatisticsOverlay.setVisible(true);

        //TODO improve this!
        viewPortSwitch.getViewTimeProperty().addListener((observable, oldValue, newValue) -> {
            StringBuilder sb = new StringBuilder();
            long ms = newValue.longValue();
            long hours = TimeUnit.MILLISECONDS.toHours(ms);
            ms -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
            ms -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);

            sb.append(hours);
            sb.append("h ");
            sb.append(minutes);
            sb.append("m ");
            sb.append(seconds);
            sb.append("s");

            timeProperty.setValue(sb.toString());
        });
        AnchorPane.setBottomAnchor(generalStatisticsViewController, 10d);
        AnchorPane.setRightAnchor(generalStatisticsViewController, 10d);

        // FIXME This part is a bit verbose!!! _____________ END




        primaryStage.setScene(scene);
        primaryStage.show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void initDatabase() {



    }

    private void initNetwork() {

        // initialize the live network that will be writte on by the receiver commands

        // create a Graph


        // TODO check if synchronized is really needed
        final Graph<INode, IConnection> graph = Graphs.synchronizedDirectedGraph(new DirectedSparseGraph<>());

        final ObservableUpdatableGraph<INode, IConnection> og = new ObservableUpdatableGraph<>(graph, new LiveUpdater());

        // TODO Ctor injection with the Ports that are within the networks
        liveNetwork = new LiveNetwork(og);

        /*
        // initialize the replay network that will be written on by a networkTape if the device plays a replay
        final INetwork replayNetwork = new ReplayNetwork(new ConcurrentDirectedSparseGraph<>());
*/
        // initialize the writing port switch that use the writing port of the live network
        // as their initial default writing port
        writingPortSwitch = new NetworkWritingPortSwitch(liveNetwork.getWritingPort());

        // initialize the reading port switch that uses the reading port of the live network
        // as its initial default reading port
        readingPortSwitch = new NetworkReadingPortSwitch(liveNetwork.getReadingPort());

        // initialize the view port switch that uses the view port of the live network
        // as its initial default view port
        viewPortSwitch = new NetworkViewPortSwitch(liveNetwork.getViewPort());

        // intialize the network device which is capable of recording and replaying any ongoing network session
        // on serveral screens
        networkDevice = new NetworkDevice();

        // create a new empty tape to record something on
        // Tell the network observation device to start recording the
        // given network with 25fps on the created tape



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
        if (configData != null) {
            configData.close();
        }

        // Disconnect the truffleReceiver
        if (truffleReceiver != null) {
            truffleReceiver.disconnect();
        }

        // Kill all threads and the thread pool with it
        LoggedScheduledExecutor.getInstance().shutdownNow();

        // Shut down the system
       // System.exit(0);
    }
}