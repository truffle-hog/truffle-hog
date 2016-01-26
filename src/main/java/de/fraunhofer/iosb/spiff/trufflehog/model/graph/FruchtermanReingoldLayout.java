package de.fraunhofer.iosb.spiff.trufflehog.model.graph;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Point2D;

/**<p>
 * Uses the Fruchterman-Reingold-algorithm from the jung library to present the {@link INetworkGraph}.
 * </p>
 */
public class FruchtermanReingoldLayout implements INetworkGraphLayout {

	/**<p>
     * Creates a new layout to present a given INetworkGraph.
     * </p>
	 * @param graph {@link INetworkGraph} to be drawn.
	 */
	public void FruchtermanReingoldLayout(INetworkGraph graph) {

	}

	@Override
	public void initialize() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void setGraph(Graph graph) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public Graph getGraph() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void setSize(Dimension d) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public Dimension getSize() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void lock(Object o, boolean state) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public boolean isLocked(Object o) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void setLocation(Object o, Point2D location) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void setInitializer(Transformer initializer) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public Object transform(Object o) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}
}
