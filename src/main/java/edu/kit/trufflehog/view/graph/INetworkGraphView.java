package edu.kit.trufflehog.view.graph;

import edu.kit.trufflehog.interaction.GraphViewInteraction;
import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.model.graph.INetworkGraphLayout;
import edu.kit.trufflehog.model.graph.INode;
import edu.kit.trufflehog.view.IViewController;
import edu.uci.ics.jung.visualization.VisualizationServer;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

/**
 * <p>
 * 		INetworkGraphView is the basic abstraction for every implementation of a network graph
 * 		view. By extending the Swing wrapper class {@link SwingNode} its possible to place
 * 		an instance of a INetworkGraphView into the existing javafx environment easily.
 * </p>
 * <p>
 *
 * </p>
 */
public abstract class INetworkGraphView extends SwingNode
		implements VisualizationServer<INode, IConnection>,
		IViewController<GraphViewInteraction> {

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
	public abstract void setLayoutFactory(Transformer<INetworkGraph, INetworkGraphLayout> layoutFactory);

}
