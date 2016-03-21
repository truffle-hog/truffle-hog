package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.BaseView;
import edu.kit.trufflehog.view.FilterOverlayViewController;
import edu.kit.trufflehog.view.OverlayViewController;

import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class BaseNetworkFactory {
    //final IUserCommand<FilterInput> updateFilterCommand,
    //final IListener<IUserCommand> userCommandIListener;

    private final Map<IInteraction, IUserCommand> commandMap;
    private final Map<IInteraction, IListener> listenerMap;

    public BaseNetworkFactory(final Map<IInteraction, IUserCommand> commandMap) {
        this.commandMap = commandMap;
    }

    public BaseView createInstance(BaseViewType baseViewType) {
        return null;
    }

    private BaseView createLiveView() {

    }

    private OverlayViewController createFilterMenu() {
        // Build filter menu
        FilterOverlayViewController filterOverlayViewController = new FilterOverlayViewController("filter_menu_overlay.fxml",
                configData, stackPane, networkViewScreen.getPickedVertexState());

        filterOverlayViewController.addListener(userCommandListener);
        filterOverlayViewController.addCommand(FilterInteraction.UPDATE, commandMap.get(FilterInteraction.UPDATE));
        filterOverlayViewController.addCommand(FilterInteraction.ADD, commandMap.get(FilterInteraction.UPDATE));
        filterOverlayViewController.addCommand(FilterInteraction.REMOVE, commandMap.get(FilterInteraction.UPDATE));
    }
}
