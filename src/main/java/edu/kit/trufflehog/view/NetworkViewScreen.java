package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.graph.FXVisualizationViewer;
import edu.kit.trufflehog.view.graph.control.FXDefaultModalGraphMouse;
import edu.kit.trufflehog.view.graph.control.FXModalGraphMouse;
import edu.kit.trufflehog.view.graph.decorators.FXEdgeShape;
import edu.kit.trufflehog.view.graph.renderers.FXRenderer;
import edu.kit.trufflehog.view.graph.renderers.FXVertexLabelRenderer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by jan on 13.01.16.
 */
public class NetworkViewScreen extends NetworkGraphViewController implements ItemListener {

    private static final Logger logger = LogManager.getLogger(NetworkViewScreen.class);

	private FXVisualizationViewer<INode, IConnection> jungView;

	private INetworkViewPort viewPort;

	private FXModalGraphMouse graphMouse;

    private Dimension dimension;

	private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

	private final Timeline refresher;

    /** The commands that are mapped to their interactions. **/
    private final Map<GraphInteraction, IUserCommand> interactionMap =
            new EnumMap<>(GraphInteraction.class);

	public NetworkViewScreen(INetworkViewPort port, long refreshRate) {

        this(port, refreshRate, new Dimension(600,600));
	}

    public NetworkViewScreen(INetworkViewPort port, long refreshRate, Dimension dimension) {

        this.dimension = dimension;
        this.viewPort = port;
        jungView = new FXVisualizationViewer<>(this.viewPort, dimension);
        initialize();

        this.layoutFactory = new FRLayoutFactory();

 /*       refresher = new Timeline(new KeyFrame(Duration.millis(refreshRate), event -> {
            Platform.runLater(this::repaint);
        }));

        refresher.setCycleCount(50);
        refresher.play();*/

        refresher = new Timeline(new KeyFrame(Duration.millis(refreshRate), event -> {
            Platform.runLater(this::repaint);
        }));

        port.addGraphEventListener(e -> {

            Platform.runLater(() -> {

                if (e.getType() == GraphEvent.Type.VERTEX_ADDED || e.getType() == GraphEvent.Type.VERTEX_CHANGED) {

                    final INode node = ((GraphEvent.Vertex<INode, IConnection>) e).getVertex();
                    node.getComponent(ViewComponent.class).animate();
                    refresher.setCycleCount(node.getComponent(ViewComponent.class).getRenderer().animationTime());
                    repaint();
                    refresher.playFromStart();

                } else if (e.getType() == GraphEvent.Type.EDGE_ADDED || e.getType() == GraphEvent.Type.EDGE_CHANGED) {

                    final IConnection connection = ((GraphEvent.Edge<INode, IConnection>) e).getEdge();
                    connection.getComponent(ViewComponent.class).animate();
                    refresher.setCycleCount(connection.getComponent(ViewComponent.class).getRenderer().animationTime());
                    repaint();
                    refresher.playFromStart();
                }
            });
        });

        // Add this view screen as listener to the picked state, so we can send commands, when the picked state
        // changes.
        getPickedVertexState().addItemListener(this);
        getPickedEdgeState().addItemListener(this);
    }

    public void initialize() {



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

		jungView.setBackground(new Color(0x3e4451));
		//jungView.setBackground(new Color(0xE8EAF6));
		//jungView.setBackground(new Color(0x5e6d67));
		//jungView.setPreferredSize(new Dimension(1000, 1000));
		// Show vertex and edge labels

		// Create a graph mouse and add it to the visualization component
		graphMouse = new FXDefaultModalGraphMouse();
		graphMouse.setMode(FXModalGraphMouse.Mode.PICKING);

		jungView.setGraphMouse(graphMouse);

	}

	public void refreshLayout() {

        jungView.setSize(dimension);
        jungView.setGraphLayout(layoutFactory.transform(jungView.getGraphLayout().getGraph()));

	}

	public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory) {

		this.layoutFactory = layoutFactory;
	}

	private void initRenderers() {


        jungView.getRenderContext().setVertexIconTransformer(new Transformer<INode, Icon>() {

            @Override
            public Icon transform(INode node) {

                final ViewComponent viewComponent = node.getComponent(ViewComponent.class);

                if (getPickedVertexState().isPicked(node)) {
                    return viewComponent.getRenderer().getIconPicked();
                } else {
                    return viewComponent.getRenderer().getIconUnpicked();
                }

            }
        });

		jungView.getRenderContext().setVertexLabelTransformer(node -> node.getAddress().toString());

		// TODO null check for component
		jungView.getRenderContext().setEdgeLabelTransformer(edge -> String.valueOf(edge.getComponent(EdgeStatisticsComponent.class).getTraffic()));

        jungView.getRenderContext().setVertexIncludePredicate(iNode -> !iNode.element.getAddress().isMulticast());

        jungView.getRenderContext().setVertexLabelRenderer(new FXVertexLabelRenderer(new Color(0x98c379), new Color(0xffffff)));

        jungView.getRenderContext().setEdgeShapeTransformer(new FXEdgeShape.QuadCurve());

		jungView.getRenderContext().setEdgeStrokeTransformer(iConnection -> {

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
            int currentSize = statComp.getCommunicationCount();
            long maxSize = viewPort.getMaxThroughput();

            double relation = (double) currentSize / (double) maxSize;
            double sizeMulti = (50.0 * relation) + 10;
            return new Ellipse2D.Double(-sizeMulti, -sizeMulti, 2*sizeMulti, 2*sizeMulti);

        });

        final Color base = new Color(0x7f7784);
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), hsbVals);

        final Color basePicked = new Color(0xf0caa3);
        final float[] hsbValsPicked = new float[3];
        Color.RGBtoHSB(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), hsbValsPicked);

        jungView.getRenderContext().setVertexFillPaintTransformer(node -> {

			final FilterPropertiesComponent fpc = node.getComponent(FilterPropertiesComponent.class);

			if (fpc != null) {

				if (fpc.getFilterColor() != null) {

                    if (getPickedVertexState().isPicked(node)) {

                       return Color.WHITE;

                    } else {
                        return fpc.getFilterColor();
                    }
				}

			}

            final ViewComponent viewComponent = node.getComponent(ViewComponent.class);

            if (getPickedVertexState().isPicked(node)) {
                return viewComponent.getRenderer().getColorPicked();
            } else {
                return viewComponent.getRenderer().getColorUnpicked();
            }
        });

		jungView.getRenderContext().setEdgeDrawPaintTransformer(iConnection -> {

            final ViewComponent viewComponent = iConnection.getComponent(ViewComponent.class);

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
	public void addChangeListener(javax.swing.event.ChangeListener l) {
		jungView.addChangeListener(l);
	}

	@Override
	public void removeChangeListener(javax.swing.event.ChangeListener l) {
		jungView.removeChangeListener(l);
	}

	@Override
	public javax.swing.event.ChangeListener[] getChangeListeners() {
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
    synchronized
	public void itemStateChanged(ItemEvent e) {

        // if ItemEvent is Vertex Selection

        final IUserCommand selectionCommand = interactionMap.get(GraphInteraction.SELECTION);

		if (selectionCommand == null) {
			logger.warn("There is no command registered to: " + GraphInteraction.SELECTION);
            return;
		}

        selectionCommand.setSelection(new ImmutablePair<>(new HashSet<>(getPickedVertexState().getPicked()), new HashSet<>(getPickedEdgeState().getPicked())));

        notifyListeners(selectionCommand);
	}
}
