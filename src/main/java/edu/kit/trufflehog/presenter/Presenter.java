package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import edu.kit.trufflehog.model.network.recording.*;
import edu.kit.trufflehog.service.executor.CommandExecutor;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver;
import edu.kit.trufflehog.view.MainToolBarController;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.OverlayViewController;
import edu.kit.trufflehog.view.RootWindowController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private static Presenter presenter;
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
    }

    /**
     *  <p>
     *      Present TruffleHog. Create all necessary objects, register them with each other, bind them, pass them the
     *      resources they need ect.
     *  </p>
     */
    public void present() {

    //    initGUI();
        initNetwork();
    }

    private void initNetwork() {

        // initialize the live network that will be writte on by the receiver commands

        // TODO Ctor injection with the Ports that are within the networks
        final INetwork liveNetwork = new LiveNetwork(new ConcurrentDirectedSparseGraph<>());
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
        networkObservationDevice.record(liveNetwork.getViewPort(), tape, 25);

        final ExecutorService commandExecutorService = Executors.newSingleThreadExecutor();
        final CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutorService.execute(commandExecutor);

        final ExecutorService truffleFetchService = Executors.newSingleThreadExecutor();
        final TruffleReceiver truffleReceiver = new UnixSocketReceiver(writingPortSwitch, Collections.emptyList());
        truffleFetchService.execute(truffleReceiver);

        truffleReceiver.connect();

        truffleReceiver.addListener(commandExecutor.asTruffleCommandListener());

        // play that ongoing recording on the given viewportswitch
        networkObservationDevice.play(tape, viewPortSwitch);

        // track the live network on the given viewportswitch
        networkObservationDevice.goLive(liveNetwork, viewPortSwitch);
    }

    private void initGUI() {
        Stage primaryStage = getPrimaryStage();

        // setting up main window
        MainViewController mainView = new MainViewController("main_view.fxml");
        Scene mainScene = new Scene(mainView);
        RootWindowController rootWindow = new RootWindowController(primaryStage, mainScene);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // setting up general statistics overlay
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        mainView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);

        // setting up menubar
        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml");
        mainView.getChildren().add(mainToolBarController);

        // setting up node statistics overlay
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        mainView.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
    }

}
