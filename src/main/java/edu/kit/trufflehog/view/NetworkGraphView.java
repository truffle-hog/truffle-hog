package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationServer;
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
 *     This class is an implementation of the {@link NetworkGraphViewController} abstraction. It is
 *     used to wrap a layout {@link Layout} of the graph data model. Thereby it
 *     can response to any changes in the layout that are reflected by modifications in the
 *     underlying data viewmodel {@link INetworkViewPort}.
 * </p>
 * <p>
 *     The NetworkGraphView also incorporates an instance of an implementation of a
 *     {@link VisualizationServer}.
 * </p>
 *
 *
 */
public class NetworkGraphView extends NetworkGraphViewController {

    /** The wrapped instance of the jung graph visualization. **/
    private VisualizationViewer<INode, IConnection> jungView;
    /** The factory used for refreshing the layout of the underlying graph. **/
    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

    /**
     * <p>
     *     	 Instantiates a new NetworkGraphView with a given layout. A
     *     	 default layout factory will be used.
     * </p>
     *
     * @param layout the layout to be used for visualization of the graph
     */
    public NetworkGraphView(final Layout<INode, IConnection> layout) {



    }

	/**
	 * <p>
	 *     	 Instantiates a new NetworkGraphView with a given layout factory.
	 *
	 * @param layoutFactory the layout factory to be used for visualization of
	 *                         the graph
	 */
	public NetworkGraphView(final Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory, INetworkViewPort graph) {

		this.layoutFactory = layoutFactory;

		jungView = new VisualizationViewer<>(layoutFactory.transform(graph.getGraph()));

	}

    /**
     * <p>
     * 		Instantiates a new NetworkGraphView with a given layout and
     * 		layout factory.
     * </p>
     *
     * @param layout the layout to be used for visualization of the graph
     * @param layoutFactory the layout factory to be used for refreshing the layout
     */
    public NetworkGraphView(final Layout<INode, IConnection> layout,
                            final Transformer<Graph<INode, IConnection>,
                                    Layout<INode, IConnection>> layoutFactory) {


		throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public final void setDoubleBuffered(final boolean b) {
        jungView.setDoubleBuffered(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isDoubleBuffered() {
        return jungView.isDoubleBuffered();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final VisualizationModel<INode, IConnection> getModel() {
        return jungView.getModel();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setModel(final VisualizationModel<INode, IConnection>
                                     visualizationModel) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void stateChanged(final ChangeEvent changeEvent) {

		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setRenderer(Renderer<INode, IConnection> renderer) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final Renderer<INode, IConnection> getRenderer() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setGraphLayout(final Layout<INode, IConnection> layout) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final Layout<INode, IConnection> getGraphLayout() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final Map<RenderingHints.Key, Object> getRenderingHints() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setRenderingHints(final Map<RenderingHints.Key, Object> map) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void addPreRenderPaintable(final Paintable paintable) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void removePreRenderPaintable(final Paintable paintable) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void addPostRenderPaintable(final Paintable paintable) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void removePostRenderPaintable(final Paintable paintable) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void addChangeListener(final ChangeListener changeListener) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void removeChangeListener(final ChangeListener changeListener) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final ChangeListener[] getChangeListeners() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void fireStateChanged() {
        jungView.fireStateChanged();	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final PickedState<INode> getPickedVertexState() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public final PickedState<IConnection> getPickedEdgeState() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setPickedVertexState(final PickedState<INode> pickedState) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setPickedEdgeState(final PickedState<IConnection> pickedState) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final GraphElementAccessor<INode, IConnection> getPickSupport() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setPickSupport(
            final GraphElementAccessor<INode, IConnection>
                    graphElementAccessor) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final Point2D getCenter() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final RenderContext<INode, IConnection> getRenderContext() {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setRenderContext(final RenderContext<INode, IConnection>
                                             renderContext) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void repaint() {
        jungView.repaint();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void addCommand(final GraphInteraction interaction,
                           final IUserCommand command) {
		throw new UnsupportedOperationException("Not implemented yet!");
    }
}
