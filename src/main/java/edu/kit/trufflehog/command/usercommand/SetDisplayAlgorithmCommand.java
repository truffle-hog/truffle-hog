package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;


/**
 * <p>
 *      Command to change the display algorithm ({@link Layout}). The factory provided during
 *      the creation is passed to the view so that the view can create appropriate layouts for the graph.
 * </p>
 */
public class SetDisplayAlgorithmCommand implements IUserCommand<Void> {
    private final Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;
    private final INetworkViewPort viewPort;

    /**
     * <p>
     *     Creates a new command.
     * </p>
     * @param factory {@link Transformer} implementation of the desired {@link Layout} implementation
     * @param viewPort {@link INetworkViewPort} implementation managing the graph layouts
     */
    public SetDisplayAlgorithmCommand(final Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> factory, final INetworkViewPort viewPort) {
        layoutFactory = factory;
        this.viewPort = viewPort;
    }

    @Override
    public void execute() {
        viewPort.setLayoutFactory(layoutFactory);
    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Really don't do it!");
    }
}
