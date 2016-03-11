package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.graph.FXVisualizationViewer;
import edu.kit.trufflehog.view.graph.control.FXDefaultModalGraphMouse;
import edu.kit.trufflehog.view.graph.control.FXModalGraphMouse;
import edu.kit.trufflehog.view.graph.decorators.FXEdgeShape;
import edu.kit.trufflehog.view.graph.renderers.FXRenderer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.AffineTransformer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jan on 13.01.16.
 */
public class NetworkViewScreen extends NetworkGraphViewController implements ItemListener {

	private FXVisualizationViewer<INode, IConnection> jungView;

	private INetworkViewPort viewPort;

//	private final javafx.animation.Timeline timeLine;

	private FXModalGraphMouse graphMouse;

	private Timeline refresher;

    /** The commands that are mapped to their interactions. **/
    private final Map<GraphInteraction, IUserCommand> interactionMap =
            new EnumMap<>(GraphInteraction.class);

	public NetworkViewScreen(INetworkViewPort port, long refreshRate) {

		refresher = new Timeline(new KeyFrame(Duration.millis(refreshRate), event -> {
			//refresh();
			//Platform.runLater(() -> repaint());
			repaint();
		}));
		refresher.setCycleCount(Timeline.INDEFINITE);
		//fiveSecondsWonder.playGraphTape();
		this.viewPort = port;
		initialize();
		refresher.play();
	}

	public void initialize() {

		jungView = new FXVisualizationViewer<>(this.viewPort);
		//jungView.getRenderContext().setEdgeLabelRenderer((EdgeLabelRenderer) new FXEdgeLabelRenderer<>());
		//jungView.revalidate();
		//createAndSetSwingContent(this, jungView);
		jungView.setRenderer(new FXRenderer<>());

/*		timeLine = new Timeline(new KeyFrame(Duration.millis(300), event -> jungView.repaint()));
		timeLine.setCycleCount(8);*/

		SwingUtilities.invokeLater(() -> this.setContent(jungView));

/*		Transformer<String, Stroke> edgeStroke = new Transformer<String, Stroke>() {
			float dash[] = { 10.0f };
			public Stroke transform(String s) {
				return new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
			}
		};*/

		initRenderers();

		jungView.setBackground(new Color(0xE8EAF6));
		//jungView.setBackground(new Color(0x5e6d67));
		jungView.setPreferredSize(new Dimension(350, 350));
		// Show vertex and edge labels

		// Create a graph mouse and add it to the visualization component
		graphMouse = new FXDefaultModalGraphMouse();
		graphMouse.setMode(FXModalGraphMouse.Mode.PICKING);

		jungView.setGraphMouse(graphMouse);

	}

	private void initRenderers() {

		jungView.getRenderContext().setVertexLabelTransformer(iNode -> iNode.toString() + " max Size: " + viewPort.getMaxThroughput());
		//jungView.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

/*		jungView.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(
                        getPickedVertexState(), new Color(0xa1928b), new Color(0xccc1bb)));*/

        jungView.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(
                        getPickedVertexState(), new Color(0xab7d63), new Color(0xf0caa3)));

        jungView.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(getPickedEdgeState(), new Color(0x7f7784), new Color(0xf0caa3)));

        jungView.getRenderContext().setVertexIncludePredicate(iNode -> !iNode.element.getAddress().isMulticast());

        jungView.getRenderContext().setEdgeShapeTransformer(new FXEdgeShape.QuadCurve());

		jungView.getRenderContext().setEdgeStrokeTransformer(iConnection -> {
			//	return new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
              //      BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
//				final long maxSize = layout.getNetworkGraph().getMaxConnectionSize();

			final ViewComponent rendererComponent = iConnection.getComponent(ViewComponent.class);

            if (iConnection.getDest().getAddress().isMulticast()) {

                return rendererComponent.getRenderer().getStroke();
            }

            final EdgeStatisticsComponent statComp = iConnection.getComponent(EdgeStatisticsComponent.class);
            // TODO maybe check for NULL
            int currentSize = statComp.getTraffic();
            long maxSize = viewPort.getMaxConnectionSize();
            float relation = (float) currentSize / (float) maxSize;
            float strokeWidth = 6.0f * relation;

            return new BasicStroke(strokeWidth);
        });

		jungView.getRenderContext().setVertexShapeTransformer(iNode -> {
            final NodeStatisticsComponent statComp = iNode.getComponent(NodeStatisticsComponent.class);
            final ViewComponent viewComponent = iNode.getComponent(ViewComponent.class);

            int currentSize = statComp.getThroughput();
            long maxSize = viewPort.getMaxThroughput();
            double relation = (double) currentSize / (double) maxSize;
            double sizeMulti = (50.0 * relation) + 10;

            if (viewComponent != null) {
                final IRenderer renderer = iNode.getComponent(ViewComponent.class).getRenderer();
                return AffineTransform.getScaleInstance(sizeMulti/100, sizeMulti/100).createTransformedShape(renderer.getShape());
            } else {
                final IRenderer renderer = new NodeRenderer();
                final ViewComponent vc = new ViewComponent(renderer);
                iNode.add(vc);
                return renderer.getShape();
            }
        });

        final Color base = new Color(0x7f7784);
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), hsbVals);

        final Color basePicked = new Color(0xf0caa3);
        final float[] hsbValsPicked = new float[3];
        Color.RGBtoHSB(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), hsbValsPicked);

/*        jungView.getRenderContext().setVertexShapeTransformer(iNode -> {

            final NodeRenderer rendererComponent = iNode.getComponent(NodeRenderer.class);

            if (rendererComponent == null) {
                return new ConstantT
            }

            return new Shape() {

            };

        });*/

		jungView.getRenderContext().setEdgeDrawPaintTransformer(iConnection -> {

            final ViewComponent viewComponent = iConnection.getComponent(ViewComponent.class);

			viewComponent.getRenderer().updateState();

            if (getPickedEdgeState().isPicked(iConnection)) {
                return viewComponent.getRenderer().getColorPicked();
            } else {
                return viewComponent.getRenderer().getColorUnpicked();
            }
        });
	}

	public void setGraphMouse(FXVisualizationViewer.FXGraphMouse graphMouse) {
		jungView.setGraphMouse(graphMouse);
	}

	public FXVisualizationViewer.FXGraphMouse getGraphMouse() {
		return jungView.getGraphMouse();
	}

	public void addGraphMouseListener(GraphMouseListener<INode> gel) {
		jungView.addGraphMouseListener(gel);
	}

	public void addKeyListener(KeyListener l) {
		jungView.addKeyListener(l);
	}

	public void setEdgeToolTipTransformer(Transformer<IConnection, String> edgeToolTipTransformer) {
		jungView.setEdgeToolTipTransformer(edgeToolTipTransformer);
	}

	public void setMouseEventToolTipTransformer(Transformer<MouseEvent, String> mouseEventToolTipTransformer) {
		jungView.setMouseEventToolTipTransformer(mouseEventToolTipTransformer);
	}

	public void setVertexToolTipTransformer(Transformer<INode, String> vertexToolTipTransformer) {
		jungView.setVertexToolTipTransformer(vertexToolTipTransformer);
	}

	public String getToolTipText(MouseEvent event) {
		return jungView.getToolTipText(event);
	}

	@Override
	public void setDoubleBuffered(boolean doubleBuffered) {
		jungView.setDoubleBuffered(doubleBuffered);
	}

	@Override
	public boolean isDoubleBuffered() {
		return jungView.isDoubleBuffered();
	}

	public Dimension getSize() {
		return jungView.getSize();
	}

	@Override
	public VisualizationModel<INode, IConnection> getModel() {
		return jungView.getModel();
	}

	@Override
	public void setModel(VisualizationModel<INode, IConnection> model) {
		jungView.setModel(model);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		jungView.stateChanged(e);
	}

	@Override
	public void setRenderer(Renderer<INode, IConnection> r) {
		jungView.setRenderer(r);
	}

	@Override
	public Renderer<INode, IConnection> getRenderer() {
		return jungView.getRenderer();
	}

	@Override
	public void setGraphLayout(Layout<INode, IConnection> layout) {
		jungView.setGraphLayout(layout);
	}

	public void scaleToLayout(ScalingControl scaler) {
		jungView.scaleToLayout(scaler);
	}

	@Override
	public Layout<INode, IConnection> getGraphLayout() {
		return jungView.getGraphLayout();
	}

	@Override
	public Map<RenderingHints.Key, Object> getRenderingHints() {
		return jungView.getRenderingHints();
	}

	@Override
	public void setRenderingHints(Map<RenderingHints.Key, Object> renderingHints) {
		jungView.setRenderingHints(renderingHints);
	}

	@Override
	public void addPreRenderPaintable(Paintable paintable) {
		jungView.addPreRenderPaintable(paintable);
	}

	public void prependPreRenderPaintable(Paintable paintable) {
		jungView.prependPreRenderPaintable(paintable);
	}

	@Override
	public void removePreRenderPaintable(Paintable paintable) {
		jungView.removePreRenderPaintable(paintable);
	}

	@Override
	public void addPostRenderPaintable(Paintable paintable) {
		jungView.addPostRenderPaintable(paintable);
	}

	public void prependPostRenderPaintable(Paintable paintable) {
		jungView.prependPostRenderPaintable(paintable);
	}

	@Override
	public void removePostRenderPaintable(Paintable paintable) {
		jungView.removePostRenderPaintable(paintable);
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		jungView.addChangeListener(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		jungView.removeChangeListener(l);
	}

	@Override
	public ChangeListener[] getChangeListeners() {
		return jungView.getChangeListeners();
	}

	@Override
	public void fireStateChanged() {
		jungView.fireStateChanged();
	}

	@Override
	public PickedState<INode> getPickedVertexState() {
		return jungView.getPickedVertexState();
	}

	@Override
	public PickedState<IConnection> getPickedEdgeState() {
		return jungView.getPickedEdgeState();
	}

	@Override
	public void setPickedVertexState(PickedState<INode> pickedVertexState) {
		jungView.setPickedVertexState(pickedVertexState);
	}

	@Override
	public void setPickedEdgeState(PickedState<IConnection> pickedEdgeState) {
		jungView.setPickedEdgeState(pickedEdgeState);
	}

	@Override
	public GraphElementAccessor<INode, IConnection> getPickSupport() {
		return jungView.getPickSupport();
	}

	@Override
	public void setPickSupport(GraphElementAccessor<INode, IConnection> pickSupport) {
		jungView.setPickSupport(pickSupport);
	}

	@Override
	public Point2D getCenter() {
		return jungView.getCenter();
	}

	@Override
	public RenderContext<INode, IConnection> getRenderContext() {
		return jungView.getRenderContext();
	}

	@Override
	public void setRenderContext(RenderContext<INode, IConnection> renderContext) {
		jungView.setRenderContext(renderContext);
	}

	@Override
	public void repaint() {
		jungView.repaint();
	}

	@Override
	public void setRefreshRate(int rate) {
		throw new UnsupportedOperationException("Operation not implemented yet");
	}

	@Override
	public void enableSmartRefresh(int maxRate) {
		throw new UnsupportedOperationException("Operation not implemented yet");
	}

	@Override
	public void disableSmartRefresh() {
		throw new UnsupportedOperationException("Operation not implemented yet");
	}

	@Override
	public void addCommand(GraphInteraction interaction, IUserCommand command) {

        interactionMap.put(interaction, command);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

        // if ItemEvent is Vertex Selection

/*        final IUserCommand<PickedState> command = interactionMap.get(GraphInteraction.VERTEX_SELECTED);

        if (command != null) {

            command.setSelection(getPickedVertexState());
        }*/
        interactionMap.get(GraphInteraction.VERTEX_SELECTED).setSelection(getPickedVertexState());

        // else if ItemEvent is Connection Selection

		throw new UnsupportedOperationException("Operation not implemented yet");
	}
}
