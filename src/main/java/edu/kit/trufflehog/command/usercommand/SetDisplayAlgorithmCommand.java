package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.ILayoutFactory;
import edu.kit.trufflehog.model.graph.INode;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;


/**
 * <p>
 *      Command to change the display algorithm ({@link Layout}). The factory provided during
 *      the creation is passed to the view so that the view can create appropriate layouts for the graph.
 * </p>
 */
public class SetDisplayAlgorithmCommand implements IUserCommand{
    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;
    private INetworkViewPort viewPort;

    /**
     * <p>
     *     Creates a new command.
     * </p>
     * @param factory {@link Transformer} implementation of the desired {@link Layout} implementation
     * @param viewPort {@link INetworkViewPort} implementation managing the graph layouts
     */
    public SetDisplayAlgorithmCommand(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> factory, INetworkViewPort viewPort) {
        layoutFactory = factory;
        this.viewPort = viewPort;
    }

    @Override
    public void execute() {
        viewPort.setLayoutFactory(layoutFactory);
    }
}
