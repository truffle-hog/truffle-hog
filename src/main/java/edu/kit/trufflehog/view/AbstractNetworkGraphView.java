package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.ANetworkGraph;
import edu.kit.trufflehog.model.graph.INetworkGraphLayout;
import edu.kit.trufflehog.model.graph.INode;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import edu.uci.ics.jung.visualization.VisualizationServer;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

/**
 * <p>
 * 		AbstractNetworkGraphView is the basic abstraction for every implementation of a network graph
 * 		view. By extending the Swing wrapper class {@link SwingNode} its possible to place
 * 		an instance of a AbstractNetworkGraphView into the existing javafx environment easily.
 * </p>
 */
public abstract class AbstractNetworkGraphView extends SwingNode
		implements VisualizationServer<INode, IConnection>,
		IViewController<GraphInteraction> {

	/** The wrapped instance of view controller notifier. **/
	private final INotifier<IUserCommand> viewControllerNotifier =
			new ViewControllerNotifier();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addListener(final IListener<IUserCommand> listener) {

		viewControllerNotifier.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removeListener(final IListener<IUserCommand> listener) {
		viewControllerNotifier.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void notifyListeners(final IUserCommand message) {
		viewControllerNotifier.notifyListeners(message);
	}

	/**
	 * Refreshes the Layout of this graph, by reapplying a new instance of an
	 * {@link INetworkGraphLayout}. New instances are created by the layout factory that is
	 * set for this instance.
	 */
	public abstract void refreshLayout();

	/**
	 * Specifies a new layout factory for this graph. The type of layout factory determines
	 * which layout will be reinstantiated to refresh the graph view.
	 *
	 * @param layoutFactory the layout factory for instantiating new layouts
	 *                         of the given type
	 */
	public abstract void setLayoutFactory(Transformer<ANetworkGraph, INetworkGraphLayout> layoutFactory);

}
