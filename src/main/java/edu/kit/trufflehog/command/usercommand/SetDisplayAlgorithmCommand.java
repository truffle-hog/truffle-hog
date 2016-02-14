package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.graph.ILayoutFactory;
import edu.kit.trufflehog.model.graph.INetworkGraphLayout;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;

/**
 * <p>
 *      Command to change the display algorithm ({@link INetworkGraphLayout}). The factory provided during
 *      the creation is passed to the view so that the view can create appropriate layouts for the graph.
 * </p>
 */
public class SetDisplayAlgorithmCommand implements IUserCommand{
    private ILayoutFactory layoutFactory;
    private NetworkGraphViewController graphView;

    /**
     * <p>
     *     Creates a new command.
     * </p>
     * @param factory {@link ILayoutFactory} implementation of the desired {@link INetworkGraphLayout} implementation
     * @param view {@link NetworkGraphViewController} implementation managing the graph layouts
     */
    SetDisplayAlgorithmCommand(ILayoutFactory factory, NetworkGraphViewController view) {
        layoutFactory = factory;
        graphView = view;
    }

    @Override
    public void execute() {
        graphView.setLayoutFactory(layoutFactory);
    }
}
