package edu.kit.trufflehog.view.graph;

import edu.kit.trufflehog.interaction.GraphViewInteraction;
import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.ANetworkGraph;
import edu.kit.trufflehog.model.graph.INetworkGraphLayout;
import edu.kit.trufflehog.model.graph.INode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * <p>
 *     This class is an implementation of the {@link INetworkGraphView} abstraction. It is
 *     used to wrap a layout {@link INetworkGraphLayout} of the graph data model. Thereby it
 *     can response to any changes in the layout that are reflected by modifications in the
 *     underlying data model {@link ANetworkGraph}.
 * </p>
 * <p>
 *     The NetworkGraphView also incorporates an instance of an implementation of a
 *     {@link VisualizationServer}.
 * </p>
 *
 *
 */
public class NetworkGraphView extends INetworkGraphView {

	/** The wrapped instance of the jung graph visualization. **/
	private VisualizationViewer<INode, IConnection> jungView;
	/** The factory used for refreshing the layout of the underlying graph. **/
	private Transformer<ANetworkGraph, INetworkGraphLayout> layoutFactory;

	/**
	 * <p>
	 *     	 Instantiates a new NetworkGraphView with a given layout. A
	 *     	 default layout factory will be used.
	 * </p>
	 *
	 * @param layout the layout to be used for visualization of the graph
	 */
	public NetworkGraphView(final INetworkGraphLayout layout) {

	}
	/**
	 * <p>
	 * 		Instantiates a new NetworkGraphView with a given layout and
	 * 		layout factory.
	 * </p>
	 *
	 * @param layout the layout to be used for visualization of the graph
	 * @param layoutFactory the layout factory to be used for refreshing the layout {@see }
	 */
	public NetworkGraphView(final INetworkGraphLayout layout,
							final Transformer<ANetworkGraph,
									INetworkGraphLayout>	layoutFactory) {



	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public final void setDoubleBuffered(boolean b) {
		jungView.setDoubleBuffered(b);
	}

	@Override
	public final boolean isDoubleBuffered() {
		return jungView.isDoubleBuffered();
	}

	@Override
	public final VisualizationModel<INode, IConnection> getModel() {
		return jungView.getModel();
	}

	@Override
	public void setModel(final VisualizationModel<INode, IConnection>
									 visualizationModel) {

	}

	@Override
	public final void stateChanged(final ChangeEvent changeEvent) {

		timeLine.playFromStart();

	}

	@Override
	public void setRenderer(Renderer<INode, IConnection> renderer) {

	}

	@Override
	public final Renderer<INode, IConnection> getRenderer() {
		return null;
	}

	@Override
	public void setGraphLayout(final Layout<INode, IConnection> layout) {

	}

	@Override
	public final Layout<INode, IConnection> getGraphLayout() {
		return null;
	}

	@Override
	public final Map<RenderingHints.Key, Object> getRenderingHints() {
		return null;
	}

	@Override
	public void setRenderingHints(final Map<RenderingHints.Key, Object> map) {

	}

	@Override
	public void addPreRenderPaintable(final Paintable paintable) {

	}

	@Override
	public void removePreRenderPaintable(final Paintable paintable) {

	}

	@Override
	public void addPostRenderPaintable(final Paintable paintable) {

	}

	@Override
	public void removePostRenderPaintable(final Paintable paintable) {

	}

	@Override
	public void addChangeListener(final ChangeListener changeListener) {

	}

	@Override
	public void removeChangeListener(final ChangeListener changeListener) {

	}

	@Override
	public final ChangeListener[] getChangeListeners() {
		return new ChangeListener[0];
	}

	@Override
	public final void fireStateChanged() {
		jungView.fireStateChanged();	}

	@Override
	public final PickedState<INode> getPickedVertexState() {
		return null;
	}

	@Override
	public final PickedState<IConnection> getPickedEdgeState() {
		return null;
	}

	@Override
	public void setPickedVertexState(final PickedState<INode> pickedState) {

	}

	@Override
	public void setPickedEdgeState(final PickedState<IConnection> pickedState) {

	}

	@Override
	public final GraphElementAccessor<INode, IConnection> getPickSupport() {
		return null;
	}

	@Override
	public void setPickSupport(
			final GraphElementAccessor<INode, IConnection>
					graphElementAccessor) {

	}

	@Override
	public final Point2D getCenter() {
		return null;
	}

	@Override
	public final RenderContext<INode, IConnection> getRenderContext() {
		return null;
	}

	@Override
	public void setRenderContext(final RenderContext<INode, IConnection>
											 renderContext) {

	}

    @Override
    public final void repaint() {
		jungView.repaint();
    }

	@Override
	public final void refreshLayout() {
	}

	@Override
	public final void setLayoutFactory(final Transformer<ANetworkGraph,
			INetworkGraphLayout> layoutFactory) {

	}

	@Override
	public void addCommand(final GraphViewInteraction interaction,
						   final IUserCommand command) {

	}
}
