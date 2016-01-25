package edu.kit.ipd.trufflehog.view.graph;

import jung.visualization.VisualizationServer;
import javafx.embed.swing.SwingNode;
import jung.algorithms.layout.Layout;

public abstract class INetworkGraphView extends SwingNode implements VisualizationServer {

	private INetworkGraphLayout layout;

}
