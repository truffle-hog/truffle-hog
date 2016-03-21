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
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.*;
import edu.kit.trufflehog.viewmodel.GeneralStatisticsViewModel;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class BaseNetworkFactory {
    private final ConfigData configData;
    private final Scene scene;
    private final Map<BaseNetwork, INetworkViewPort> viewPortMap;
    private final Map<IInteraction, IUserCommand> commandMap;
    private final Map<IInteraction, IListener> listenerMap;

    private final MultiViewManager multiViewManager;

    public BaseNetworkFactory(final ConfigData configData,
                              final Scene scene,
                              final Map<BaseNetwork, INetworkViewPort> viewPortMap,
                              final Map<IInteraction, IUserCommand> commandMap,
                              final Map<IInteraction, IListener> listenerMap) {
        this.configData = configData;
        this.scene = scene;
        this.viewPortMap = viewPortMap;
        this.commandMap = commandMap;
        this.listenerMap = listenerMap;

        this.multiViewManager = new MultiViewManager();
    }

    public BaseView createInstance(BaseNetwork baseNetwork) {
        return null;
    }

    private BaseView createLiveView() {
        final NetworkViewScreen networkViewScreen = new NetworkViewScreen(viewPortMap.get(BaseNetwork.LIVE),
                30, new Dimension(700, 700));
        final StatisticsViewModel statViewModel = new StatisticsViewModel();

        networkViewScreen.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));
        networkViewScreen.addCommand(GraphInteraction.SELECTION, new SelectionCommand(statViewModel));

        networkViewScreen.setLayoutFactory(new FRLayoutFactory());

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                networkViewScreen::refreshLayout);


        AnchorPane viewSettingsMenu = createSettingsOverlay();
        AnchorPane filterMenu = createFilterMenu();
        AnchorPane captureMenu = createRecordOverlay();
        BorderPane selectionStatistics = createSelectionStatisticsOverlay(statViewModel);
        BorderPane generalStatistics = createGeneralStatisticsOverlay(viewPortMap.get(BaseNetwork.LIVE));
        ToolBar toolBar = createToolbar();

        return new LiveViewController(configData.getProperty("LIVE_VIEW"), multiViewManager, networkViewScreen,
                viewSettingsMenu, filterMenu, captureMenu, selectionStatistics, generalStatistics, toolBar);
    }

    private FilterOverlayViewController createFilterMenu() {
        // Build filter menu
        FilterOverlayViewController filterOverlayViewController =
                new FilterOverlayViewController(configData.getProperty("FILTER_MENU"), configData, stackPane,
                        networkViewScreen.getPickedVertexState());

        filterOverlayViewController.addListener(listenerMap.get(ListenerInteraction.USER_COMMAND));
        filterOverlayViewController.addCommand(FilterInteraction.UPDATE, commandMap.get(FilterInteraction.UPDATE));
        filterOverlayViewController.addCommand(FilterInteraction.ADD, commandMap.get(FilterInteraction.UPDATE));
        filterOverlayViewController.addCommand(FilterInteraction.REMOVE, commandMap.get(FilterInteraction.UPDATE));

        return filterOverlayViewController;
    }

    private ViewSettingsViewController createSettingsOverlay() {
        return new ViewSettingsViewController(configData.getProperty("VIEW_SETTINGS_MENU"));
    }

    private RecordMenuViewController createRecordOverlay(INetworkDevice networkDevice, INetwork liveNetwork) {
        return new RecordMenuViewController(configData.getProperty("RECORD_MENU"), networkDevice, liveNetwork);
    }

    private StatisticsViewController createSelectionStatisticsOverlay(final StatisticsViewModel statViewModel) {
        return new StatisticsViewController(statViewModel);
    }

    private GeneralStatisticsViewController createGeneralStatisticsOverlay(INetworkViewPort viewPort) {
        final GeneralStatisticsViewModel generalStatViewModel = new GeneralStatisticsViewModel();
        final GeneralStatisticsViewController generalStatisticsOverlay = new GeneralStatisticsViewController(generalStatViewModel);

        StringProperty timeProperty = new SimpleStringProperty("");
        StringProperty throughputStringProperty = new SimpleStringProperty();
        throughputStringProperty.bindBidirectional(viewPort.getThroughputProperty(), new DecimalFormat("0.00"));

        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("GS_POPULATION"), viewPort.getPopulationProperty())));
        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("GS_PPS"), throughputStringProperty)));
        generalStatViewModel.getRootItem().getChildren().add(new TreeItem<>(new GeneralStatisticsViewModel
                .StringEntry<>(configData.getProperty("RUNNING"), timeProperty)));

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

    private ToolBarViewController createToolbar() {
        return new ToolBarViewController(configData.getProperty("LIVE_VIEW_TOOLBAR"));
    }
}
