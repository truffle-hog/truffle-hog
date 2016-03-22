package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.SelectionCommand;
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.interaction.ListenerInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.*;
import edu.kit.trufflehog.viewmodel.FilterViewModel;
import edu.kit.trufflehog.viewmodel.GeneralStatisticsViewModel;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import edu.uci.ics.jung.visualization.picking.PickedState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     The BaseNetworkFactory is a network + view factory that  is able to create any network along with its view that
 *     TruffleHog supports. That means that the cloning of the views through the split screens happens here. It is a
 *     factory to produce any view desired.
 * </p>
 * <p>
 *     This class works together with the {@link Presenter} to build TruffleHog. Any logic that should be executed more
 *     than once when a screen is split should be within this class. If on the other hand there is logic that should
 *     only be executed exactly once at the program start, it belongs in the Presenter.
 * </p>
 * <p>
 *     For example, the {@link ConfigData} is only created once, thus its creation is in the Presenter, whereas views
 *     have to be recreated at every screen split, thus there can be multiple instances of them in TruffleHog and that
 *     is why they are created in this class.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class BaseNetworkFactory {
    private final ConfigData configData;
    private final ViewSplitter viewSplitter;

    private final Scene scene;
    private final StackPane stackPane;
    private final INetworkDevice networkDevice;
    private final INetwork liveNetwork;
    private final Map<NetworkType, INetworkViewPort> viewPortMap;
    private final Map<IInteraction, IUserCommand> commandMap;
    private final Map<IInteraction, IListener> listenerMap;

    private final List<NetworkType> liveItems;
    private final ObservableList<String> captureItems;

    /**
     * <p>
     *     Creates a new BaseNetworkFactory.
     * </p>
     *
     * @param configData The {@link ConfigData} object that is used to access the model's saving mechanism.
     * @param scene The main scene where everything is drawn upon.
     * @param stackPane The stackPane to put the popover menus on.
     * @param networkDevice TODO fill
     * @param liveNetwork TODO fill
     * @param viewPortMap A map with all viewports TruffleHog supports.
     * @param commandMap A map with all commands TruffleHog supports.
     * @param listenerMap A map with all listeners TruffleHog has.
     */
    public BaseNetworkFactory(final ConfigData configData,
                              final Scene scene,
                              final StackPane stackPane,
                              final INetworkDevice networkDevice,
                              final INetwork liveNetwork,
                              final Map<NetworkType, INetworkViewPort> viewPortMap,
                              final Map<IInteraction, IUserCommand> commandMap,
                              final Map<IInteraction, IListener> listenerMap) {
        this.configData = configData;
        this.scene = scene;
        this.stackPane = stackPane;
        this.networkDevice = networkDevice;
        this.liveNetwork = liveNetwork;
        this.viewPortMap = viewPortMap;
        this.commandMap = commandMap;
        this.listenerMap = listenerMap;

        this.viewSplitter = new ViewSplitter(this);

        this.liveItems = new ArrayList<>(viewPortMap.keySet());
        captureItems = FXCollections.observableArrayList("capture-932", "capture-724", "capture-457", "capture-167");
    }

    /**
     * <p>
     *     Creates an instance of any {@link BaseView} that TruffleHog supports.
     * </p>
     *
     * @param networkType The {@link BaseView} that should be created.
     * @return The created base view.
     */
    public BaseView createInstance(NetworkType networkType) {
        if (networkType == NetworkType.START) {
            return createStartView();
        } else if (networkType == NetworkType.DEMO) {
            return createDemoView();
        } else if (networkType == NetworkType.PROFINET) {
            return createStartView();
        } else if (networkType == NetworkType.CAPTURE) {
            return createStartView();
        }

        return null;
    }

    ViewSplitter getViewSplitter() {
        return viewSplitter;
    }

    private BaseView createStartView() {
        return new StartViewController(configData.getProperty("START_VIEW"), liveItems, captureItems, configData,
                viewSplitter);
    }

    /**
     * <p>
     *     Creates the live network view. This is where everything happens - the graph is displayed in quasi real time.
     * </p>
     *
     * @return The fully configured live network view.
     */
    private BaseView createDemoView() {
        final NetworkViewScreen networkViewScreen = new NetworkViewScreen(viewPortMap.get(NetworkType.DEMO),
                30, new Dimension(700, 700));
        final StatisticsViewModel statViewModel = new StatisticsViewModel();

        networkViewScreen.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));
        networkViewScreen.addCommand(GraphInteraction.SELECTION, new SelectionCommand(statViewModel));

        networkViewScreen.setLayoutFactory(new FRLayoutFactory());

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                networkViewScreen::refreshLayout);

        FilterViewModel filterViewModel = new FilterViewModel(configData);

        AnchorPane viewSettingsMenu = createSettingsOverlay();
        FilterEditingMenuViewController filterEditMenu = createEditFilterMenu(stackPane, filterViewModel);
        AnchorPane filterMenu = createFilterMenu(filterEditMenu, networkViewScreen.getPickedVertexState());
        AnchorPane captureMenu = createRecordOverlay(networkDevice, liveNetwork);
        BorderPane selectionStatistics = createSelectionStatisticsOverlay(statViewModel);
        BorderPane generalStatistics = createGeneralStatisticsOverlay(viewPortMap.get(NetworkType.DEMO));
        ToolBar toolBar = createToolbar();

        return new NetworkViewController(configData.getProperty("NETWORK_VIEW"), viewSplitter, networkViewScreen,
                viewSettingsMenu, filterMenu, captureMenu, selectionStatistics, generalStatistics, toolBar);
    }

    /**
     * <p>
     *     Create the filter menu that is used to display existing filters.
     * </p>
     *
     * @param filterEditMenu The filter edit menu through which filters can be created and updated.
     * @param pickedState The picked state that is used to get the current selection of nodes.
     * @return The fully configured FilterOverlayViewController.
     */
    private FilterOverlayViewController createFilterMenu(FilterEditingMenuViewController filterEditMenu,
                                                         PickedState<INode> pickedState) {
        FilterOverlayViewController filterMenu = new FilterOverlayViewController(configData.getProperty("FILTER_MENU"),
                pickedState, configData.getAllLoadedFilters(), filterEditMenu);

        filterMenu.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));
        filterMenu.addCommand(FilterInteraction.UPDATE, commandMap.get(FilterInteraction.UPDATE));
        filterMenu.addCommand(FilterInteraction.REMOVE, commandMap.get(FilterInteraction.REMOVE));
        filterMenu.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));

        return filterMenu;
    }

    /**
     * <p>
     *     Creates the filter edit menu that is used to edit and add filters.
     * </p>
     *
     * @param stackPane The stack pane atop which the filter edit menu should be displayed.
     * @param filterViewModel The filter view model needed to create and update filters.
     * @return The fully configured FilterEditingMenuViewController.
     */
    private FilterEditingMenuViewController createEditFilterMenu(StackPane stackPane, FilterViewModel filterViewModel) {
        FilterEditingMenuViewController filterEditMenu = new FilterEditingMenuViewController(
                configData.getProperty("FILTER_EDIT_MENU"), configData, filterViewModel, stackPane);

        filterEditMenu.addCommand(FilterInteraction.ADD, commandMap.get(FilterInteraction.ADD));
        filterEditMenu.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));

        return filterEditMenu;
    }

    /**
     * <p>
     *     Creates the view settings menu.
     * </p>
     *
     * @return The fully configured view settings menu.
     */
    private ViewSettingsViewController createSettingsOverlay() {
        return new ViewSettingsViewController(configData.getProperty("VIEW_SETTINGS_MENU"));
    }

    /**
     * <p>
     *     Creates the recording menu to record the current network.
     * </p>
     *
     * @param networkDevice TODO fill
     * @param liveNetwork TODO fill
     * @return The fully configured record menu.
     */
    private RecordMenuViewController createRecordOverlay(final INetworkDevice networkDevice, final INetwork liveNetwork) {
        return new RecordMenuViewController(configData.getProperty("RECORD_MENU"), networkDevice, liveNetwork);
    }

    /**
     * <p>
     *     Creates the playback menu to playback a network graph tape.
     * </p>
     *
     * @return The fully configured playback menu.
     */
    private PlaybackMenuViewController createPlaybackOverlay() {
        return new PlaybackMenuViewController(configData.getProperty("PLAYBACK_MENU"));
    }

    /**
     * <p>
     *     Creates the selection statistics overlay that is used to display selection specific statistics about the
     *     current state of the network.
     * </p>
     *
     * @param statViewModel The statistics view model associated with the StatisticsViewController.
     * @return The fully configured StatisticsViewController.
     */
    private StatisticsViewController createSelectionStatisticsOverlay(final StatisticsViewModel statViewModel) {
        return new StatisticsViewController(statViewModel);
    }

    /**
     * <p>
     *     Creates the general statistics overlay that is used to display general statistics about the current state
     *     of the network.
     * </p>
     *
     * @param viewPort The view port for the network to show the statistics for.
     * @return The fully configured GeneralStatisticsViewController.
     */
    private GeneralStatisticsViewController createGeneralStatisticsOverlay(INetworkViewPort viewPort) {
        final GeneralStatisticsViewModel generalStatViewModel = new GeneralStatisticsViewModel();
        final GeneralStatisticsViewController generalStatisticsOverlay = new GeneralStatisticsViewController(
                configData.getProperty("GENERAL_STATS_VIEW"), generalStatViewModel);

        StringProperty timeProperty = new SimpleStringProperty("");
        StringProperty throughputStringProperty = new SimpleStringProperty();
        throughputStringProperty.bindBidirectional(viewPort.getThroughputProperty(), new DecimalFormat("0.00"));

        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("GS_POPULATION"), viewPort.getPopulationProperty())));
        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("GS_PPS"), throughputStringProperty)));
        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("GS_RUNNING"), timeProperty)));

        //TODO improve this!
        viewPort.getViewTimeProperty().addListener((observable, oldValue, newValue) -> {
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

        return generalStatisticsOverlay;
    }

    /**
     * <p>
     *     Creates the toolbar for the network view that is shown on the bottom right.
     * </p>
     *
     * @return The toolbar for the network view that is shown on the bottom right.
     */
    private ToolBarViewController createToolbar() {
        return new ToolBarViewController(configData.getProperty("LIVE_VIEW_TOOLBAR"));
    }
}
