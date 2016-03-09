package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.IRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastLayeredEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.graph.decorators.FXEdgeShape;
import edu.kit.trufflehog.view.graph.renderers.FXRenderer;
import edu.kit.trufflehog.view.graph.FXVisualizationViewer;
import edu.kit.trufflehog.view.graph.control.FXDefaultModalGraphMouse;
import edu.kit.trufflehog.view.graph.control.FXModalGraphMouse;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Created by jan on 13.01.16.
 */
public class NetworkViewScreen extends NetworkGraphViewController {

	private FXVisualizationViewer<INode, IConnection> jungView;

	private INetworkViewPort viewPort;

//	private final javafx.animation.Timeline timeLine;

	private FXModalGraphMouse graphMouse;

	private Timeline refresher;

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

		jungView.setBackground(new Color(0x9dc4bf));
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

            //final Ellipse2D circle = new Ellipse2D.Double(-1, -1, 2, 2);
            // in this case, the vertex is twice as large

            //System.out.println(layout.transform(iNode));

            final NodeStatisticsComponent statComp = iNode.getComponent(NodeStatisticsComponent.class);
            int currentSize = statComp.getThroughput();
            long maxSize = viewPort.getMaxThroughput();
            double relation = (double) currentSize / (double) maxSize;
            double sizeMulti = (50.0 * relation) + 10;
            return new Ellipse2D.Double(-sizeMulti, -sizeMulti, 2*sizeMulti, 2*sizeMulti);
            //return AffineTransform.getScaleInstance(sizeMulti, sizeMulti).createTransformedShape(circle);
        });

        final Color base = new Color(0x7f7784);
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), hsbVals);

        final Color basePicked = new Color(0xf0caa3);
        final float[] hsbValsPicked = new float[3];
        Color.RGBtoHSB(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), hsbValsPicked);

/*        jungView.getRenderContext().setVertexShapeTransformer(iNode -> {

            final NodeRendererComponent rendererComponent = iNode.getComponent(NodeRendererComponent.class);

            if (rendererComponent == null) {
                return new ConstantT
            }

            return new Shape() {

            };

        });*/

		jungView.getRenderContext().setEdgeDrawPaintTransformer(iConnection -> {

            final ViewComponent viewComponent = iConnection.getComponent(ViewComponent.class);

            if (getPickedEdgeState().isPicked(iConnection)) {
                return viewComponent.getRenderer().getColorPicked();
            } else {
                return viewComponent.getRenderer().getColorUnpicked();
            }
        });
	}


	@Override
	public void setDoubleBuffered(boolean b) {
		jungView.setDoubleBuffered(b);
	}

	@Override
	public boolean isDoubleBuffered() {
		return jungView.isDoubleBuffered();
	}

	@Override
	public VisualizationModel<INode, IConnection> getModel() {
		return jungView.getModel();
	}

	@Override
	public void setModel(VisualizationModel<INode, IConnection> visualizationModel) {

	}

	@Override
	public void stateChanged(ChangeEvent changeEvent) {

		jungView.stateChanged(changeEvent);
		//timeLine.playFromStart();

	}

	@Override
	public void setRenderer(Renderer<INode, IConnection> renderer) {

	}

	@Override
	public Renderer<INode, IConnection> getRenderer() {
		return null;
	}

	@Override
	public void setGraphLayout(Layout<INode, IConnection> layout) {

	}

	@Override
	public Layout<INode, IConnection> getGraphLayout() {
		return null;
	}

	@Override
	public Map<RenderingHints.Key, Object> getRenderingHints() {
		return null;
	}

	@Override
	public void setRenderingHints(Map<RenderingHints.Key, Object> map) {

	}

	@Override
	public void addPreRenderPaintable(Paintable paintable) {

	}

	@Override
	public void removePreRenderPaintable(Paintable paintable) {

	}

	@Override
	public void addPostRenderPaintable(Paintable paintable) {

	}

	@Override
	public void removePostRenderPaintable(Paintable paintable) {

	}

	@Override
	public void addChangeListener(ChangeListener changeListener) {

	}

	@Override
	public void removeChangeListener(ChangeListener changeListener) {

	}

	@Override
	public ChangeListener[] getChangeListeners() {
		return new ChangeListener[0];
	}

	@Override
	public void fireStateChanged() {
		jungView.fireStateChanged();	}

	@Override
	public PickedState<INode> getPickedVertexState() {
		return jungView.getPickedVertexState();
	}

	@Override
	public PickedState<IConnection> getPickedEdgeState() {
		return jungView.getPickedEdgeState();
	}

	@Override
	public void setPickedVertexState(PickedState<INode> pickedState) {

        jungView.setPickedVertexState(pickedState);
	}

	@Override
	public void setPickedEdgeState(PickedState<IConnection> pickedState) {
        jungView.setPickedEdgeState(pickedState);
	}

	@Override
	public GraphElementAccessor<INode, IConnection> getPickSupport() {
		return null;
	}

	@Override
	public void setPickSupport(
			GraphElementAccessor<INode, IConnection> graphElementAccessor) {

	}

	@Override
	public Point2D getCenter() {
		return null;
	}

	@Override
	public RenderContext<INode, IConnection> getRenderContext() {
		return null;
	}

	@Override
	public void setRenderContext(RenderContext<INode, IConnection> renderContext) {

	}

    @Override
    public void repaint() {


/*		if (jungView.getModel().getGraphLayout() instanceof Caching) {

			((Caching) jungView.getModel().getGraphLayout()).clear();

		}*/

		jungView.repaint();
    }

	@Override
	public void setRefreshRate(int rate) {

	}

	@Override
	public void enableSmartRefresh(int maxRate) {

	}

	@Override
	public void disableSmartRefresh() {

	}

	@Override
	public void addCommand(GraphInteraction interaction, IUserCommand command) {

	}
}
