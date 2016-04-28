/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.view.jung.visualization;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.SelectionContextMenuCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.jung.layout.ObservableLayout;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.IEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import edu.kit.trufflehog.util.Notifier;
import edu.kit.trufflehog.util.bindings.MyBindings;
import edu.kit.trufflehog.view.controllers.IViewController;
import edu.kit.trufflehog.view.controllers.ViewControllerNotifier;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * \brief
 * \details
 * \date 21.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class FXVisualizationViewer<V extends INode, E extends IConnection> extends AnchorPane implements VisualizationServer<V, E>, IViewController<GraphInteraction> {

    private static final Logger logger = LogManager.getLogger();

    private final DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    private final PannableCanvas canvas;
    private final NodeGestures nodeGestures;
    private final CanvasGestures canvasGestures;

    private final Set<V> selectedNodes = new HashSet<>();
    private final Set<E> selectedEdges = new HashSet<>();

    private final PickedState<V> pickedNodes = new MyPickedState<>(selectedNodes);

    private final INotifier<IUserCommand> viewControllerNotifier = new Notifier<IUserCommand>() {
        @Override
        public boolean addListener(IListener<IUserCommand> listener) {
            return super.addListener(listener);
        }
    };

    /** The commands that are mapped to their interactions. **/
    private final Map<GraphInteraction, IUserCommand> interactionMap =
            new EnumMap<>(GraphInteraction.class);

    private ObservableLayout<V, E> layout;

    private INetworkViewPort port;

    public FXVisualizationViewer(final ObservableLayout<V, E> layout, INetworkViewPort port) {



        this.port = port;
        // create canvas
        this.setStyle("-fx-background-color: #213245");

        for (double divide = .25; divide < 1; divide += .25) {
            final Line line = new Line(0, 200, this.getWidth(), this.getHeight());
            line.setFill(null);
            line.setStroke(Color.web("0x385172"));
            line.setStrokeWidth(1.3);
            line.getStrokeDashArray().addAll(5d, 10d);

            line.startYProperty().bind(this.heightProperty().multiply(divide));

            line.endXProperty().bind(this.widthProperty());
            line.endYProperty().bind(this.heightProperty().multiply(divide));

            this.getChildren().add(line);
        }

        for (double divide = .25; divide < 1; divide += .25) {
            final Line line = new Line(0, 0, this.getWidth(), this.getHeight());
            line.setFill(null);
            line.setStroke(Color.web("0x385172"));
            line.setStrokeWidth(1.3);
            line.getStrokeDashArray().addAll(5d, 10d);

            line.startXProperty().bind(this.widthProperty().multiply(divide));

            line.endXProperty().bind(this.widthProperty().multiply(divide));
            line.endYProperty().bind(this.heightProperty());

            this.getChildren().add(line);
        }



        //this.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("icon.png")), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, null, new BackgroundSize())));

        canvas = new PannableCanvas();

        this.setOnMouseClicked(e -> {

            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (selectedNodes.isEmpty() && selectedEdges.isEmpty()) {
                return;
            }

            selectedNodes.forEach(v -> v.getComponent(ViewComponent.class).getRenderer().isPicked(false));
            selectedEdges.forEach(v -> v.getComponent(ViewComponent.class).getRenderer().isPicked(false));
            selectedNodes.clear();
            selectedEdges.clear();

            onSelectionChanged();
            onSelectionContextMenu(0, 0);
        });



        canvasGestures = new CanvasGestures(canvas);

      //  canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, canvasGestures.getOnMousePressedEventHandler());
      //  canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, canvasGestures.getOnMouseDraggedEventHandler());

        // TODO make canvas transparent
        //canvas.setStyle("-fx-background-color: #1d1d1d");

        // we don't want the canvas on the top/left in this example => just
        // translate it a bit
        canvas.setTranslateX(100);
        canvas.setTranslateY(100);

        // create sample nodes which can be dragged
        nodeGestures = new NodeGestures( canvas);

        this.getChildren().add(canvas);
        this.layout = layout;

        //this.layout.getGraph().getVertices().forEach(v -> Platform.runLater(() -> this.initVertex(v)));
        //this.layout.getGraph().getEdges().forEach(e -> Platform.runLater(() -> this.initEdge(e)));

        this.layout.getObservableGraph().addGraphEventListener(e -> {

            //Platform.runLater(() -> {

                switch (e.getType()) {
                    case VERTEX_ADDED:
                        final V node = ((GraphEvent.Vertex<V, E>) e).getVertex();
                        Platform.runLater(() -> initVertex(node));
                        break;

                    case EDGE_ADDED:
                        final E edge = ((GraphEvent.Edge<V, E>) e).getEdge();
                        Platform.runLater(() -> initEdge(edge));
                        break;

                    case VERTEX_CHANGED:
                        break;

                    case EDGE_CHANGED:
                        final E changedEdge = ((GraphEvent.Edge<V, E>) e).getEdge();
                        Platform.runLater(() -> changedEdge.getComponent(ViewComponent.class).getRenderer().animate());
                        break;
                }
            //});
        });


    }

    // TODO check if synch is needed
    synchronized
    private void initEdge(E edge) {

        final Pair<V> pair = this.layout.getGraph().getEndpoints(edge);

        if (pair.getSecond().getAddress().isMulticast()) {

            // TODO check or do something with this (generify the getShape command?)
            final Circle destCircle = (Circle) pair.getFirst().getComponent(ViewComponent.class).getRenderer().getShape();
            final Shape shape = edge.getComponent(ViewComponent.class).getRenderer().getShape();
            shape.layoutXProperty().bind(destCircle.layoutXProperty());
            shape.layoutYProperty().bind(destCircle.layoutYProperty());
            canvas.getChildren().add(shape);
            shape.setPickOnBounds(false);
            shape.setMouseTransparent(true);
            return;

        }

        // source and destination shape
        final Shape srcShape = pair.getFirst().getComponent(ViewComponent.class).getRenderer().getShape();
        final Shape destShape = pair.getSecond().getComponent(ViewComponent.class).getRenderer().getShape();

        // the positions of the shapes
        final DoubleProperty srcX = srcShape.layoutXProperty();
        final DoubleProperty srcY = srcShape.layoutYProperty();
        final DoubleProperty destX = destShape.layoutXProperty();
        final DoubleProperty destY = destShape.layoutYProperty();


        // the direction vector from source to destination (deltaX, deltaY)
        final DoubleBinding deltaX = destX.subtract(srcX);
        final DoubleBinding deltaY = destY.subtract(srcY);

        // the length of the direction vector
        final DoubleBinding length = MyBindings.sqrt(Bindings.add(MyBindings.pow2(deltaX), MyBindings.pow2(deltaY)));

        // the normalized direction vector
        final DoubleBinding normalX = deltaX.divide(length);
        final DoubleBinding normalY = deltaY.divide(length);

        // cast the shapes to circles (because right now i know they are circles) //TODO make this for arbitrary shapes
        final Circle destCircle = (Circle) destShape;
        final Circle srcCircle = (Circle) srcShape;

        // get the real source by multiplying the normal with the radius and the scale of the shape
        final DoubleBinding realSoureX = srcX.add(normalX.multiply(srcCircle.radiusProperty().multiply(srcShape.scaleXProperty())));
        final DoubleBinding realSoureY = srcY.add(normalY.multiply(srcCircle.radiusProperty().multiply(srcShape.scaleYProperty())));

        // get the real destination by multipling the normal vector with the length minus the radius and scale of the destination shape
        final DoubleBinding realDestX = srcX.add(normalX.multiply(length.subtract(destCircle.radiusProperty().multiply(destShape.scaleXProperty()))));
        final DoubleBinding realDestY = srcY.add(normalY.multiply(length.subtract(destCircle.radiusProperty().multiply(destShape.scaleYProperty()))));

        final IEdgeRenderer edgeRenderer = (IEdgeRenderer) edge.getComponent(ViewComponent.class).getRenderer();


        edgeRenderer.getArrowShape().layoutXProperty().bind(realDestX);
        edgeRenderer.getArrowShape().layoutYProperty().bind(realDestY);

        final QuadCurve curve = edgeRenderer.getLine();
        curve.setCacheHint(CacheHint.SPEED);


        // make the edge clickable
        curve.setOnMouseClicked(e -> {

            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            logger.debug(edge.getSrc().getComponent(NodeInfoComponent.class).toString() + " --[" + edge.getComponent(EdgeStatisticsComponent.class).getTraffic() +
                    "]-->" + edge.getDest().getComponent(NodeInfoComponent.class).toString());

            edge.getComponent(ViewComponent.class).getRenderer().togglePicked();
        });

        edgeRenderer.getArrowShape().setOnMouseClicked(e -> {

            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            logger.debug(edge.getSrc().getComponent(NodeInfoComponent.class).toString() + " --[" + edge.getComponent(EdgeStatisticsComponent.class).getTraffic() +
            "]-->" + edge.getDest().getComponent(NodeInfoComponent.class).toString());

            edge.getComponent(ViewComponent.class).getRenderer().togglePicked();


        });

        //get the edge size binding by dividing the total traffic with the local traffic
        DoubleBinding edgeSize = MyBindings.divideIntToDouble(edge.getComponent(EdgeStatisticsComponent.class).getTrafficProperty(), port.getMaxConnectionSizeProperty()).multiply(8).add(2);
        curve.strokeWidthProperty().bind(edgeSize.multiply(edgeRenderer.edgeWidthMultiplierProperty()));


        // bind the ending to the real destionaion point
        curve.endXProperty().bind(realDestX);
        curve.endYProperty().bind(realDestY);

        // bind the source to the real source point
        curve.startXProperty().bind(realSoureX);
        curve.startYProperty().bind(realSoureY);

        NumberBinding normalVectorX = Bindings.subtract(realDestY, realSoureY).negate();
        NumberBinding normalVectorY = Bindings.subtract(realDestX, realSoureX);

        NumberBinding centerPointX = Bindings.divide(Bindings.add(curve.endXProperty(), curve.startXProperty()), 2);
        NumberBinding centerPointY = Bindings.divide(Bindings.add(curve.endYProperty(), curve.startYProperty()), 2);

        NumberBinding normalLength = MyBindings.sqrt(Bindings.add(normalVectorX.multiply(normalVectorX), normalVectorY.multiply(normalVectorY)));

        NumberBinding normalizedNVX = normalVectorX.divide(normalLength);
        NumberBinding normalizedNVY = normalVectorY.divide(normalLength);

        NumberBinding bezierPointOffset = length.multiply(.1);

        curve.controlXProperty().bind(Bindings.add(centerPointX, normalizedNVX.multiply(bezierPointOffset)));
        curve.controlYProperty().bind(Bindings.add(centerPointY, normalizedNVY.multiply(bezierPointOffset)));

        // TODO do this in component
        curve.setFill(null);


        canvas.getChildren().add(edgeRenderer.getArrowShape());
        // add the edge to the canvas
        canvas.getChildren().add(curve);
    }

    synchronized
    private void initVertex(V vertex) {

        if (vertex.getAddress().isMulticast()) {
            return;
        }

        final Shape nodeShape = vertex.getComponent(ViewComponent.class).getRenderer().getShape();
        nodeShape.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        nodeShape.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        nodeShape.setOnMouseClicked(e -> {

            if (e.getButton() == MouseButton.PRIMARY) {
                final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();

                if (!renderer.picked()) {
                    selectedNodes.add(vertex);
                    renderer.isPicked(true);
                    onSelectionChanged();
                }

            } else if (e.getButton() == MouseButton.SECONDARY) {
                final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();

                if (!renderer.picked()) {
                    selectedNodes.forEach(v -> v.getComponent(ViewComponent.class).getRenderer().isPicked(false));
                    selectedEdges.forEach(v -> v.getComponent(ViewComponent.class).getRenderer().isPicked(false));
                    selectedNodes.clear();
                    selectedEdges.clear();

                    selectedNodes.add(vertex);
                    renderer.isPicked(true);
                    onSelectionChanged();
                    onSelectionContextMenu(e.getScreenX(), e.getScreenY());
                }


            }
            e.consume();
        });
        //nodeShape.setCache(false);
        //nodeShape.setCacheHint(CacheHint.);

        final NodeStatisticsComponent nsc = vertex.getComponent(NodeStatisticsComponent.class);

        final DoubleBinding nodeSize = MyBindings.divideIntToDouble(nsc.getCommunicationCountProperty(), port.getMaxThroughputProperty()).add(1);

        nodeShape.scaleXProperty().bind(nodeSize);
        nodeShape.scaleYProperty().bind(nodeSize);

        nodeShape.setLayoutX(layout.transform(vertex).getX());
        nodeShape.setLayoutY(layout.transform(vertex).getY());

        ///////////
        // LABEL //
        ///////////

        Label nodeLabel = new Label();

        // cast the shapes to circles (because right now i know they are circles) //TODO make this for arbitrary shapes
        final Circle nodeCircle = (Circle) nodeShape;

/*        final DoubleProperty labelX = new SimpleDoubleProperty();
        final DoubleProperty labelY = new SimpleDoubleProperty();*/



        //labelX.bind(nodeShape.layoutXProperty().add(nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty())));
        //labelY.bind(nodeShape.layoutYProperty().add(nodeCircle.radiusProperty().multiply(nodeShape.scaleYProperty())));

        //nodeLabel.layoutXProperty().bindBidirectional(labelX);
        //nodeLabel.layoutYProperty().bindBidirectional(labelY);

        //nodeLabel.layoutXProperty().bind(nodeShape.layoutXProperty().add(nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty())));

        nodeLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.WHITE));

        MyBindings.bindBidirectional(nodeLabel.layoutXProperty(), nodeShape.layoutXProperty(), nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty()));
        MyBindings.bindBidirectional(nodeLabel.layoutYProperty(), nodeShape.layoutYProperty(), nodeCircle.radiusProperty().multiply(nodeShape.scaleYProperty()));


        NodeInfoComponent nic = vertex.getComponent(NodeInfoComponent.class);
        if (nic != null) {
            nodeLabel.textProperty().bind(nic.toStringBinding());
        }

        nodeLabel.scaleXProperty().bind(Bindings.divide(1, canvas.scaleXProperty()));
        nodeLabel.scaleYProperty().bind(Bindings.divide(1, canvas.scaleYProperty()));

        nodeLabel.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        nodeLabel.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        canvas.getChildren().addAll(nodeLabel, nodeShape);
    }

    synchronized
    public void refreshLayout() {

      //  logger.debug("refresh");
        final FRLayout2<V, E> l = new FRLayout2<>(this.layout.getObservableGraph());
            l.setMaxIterations(layout.getGraph().getEdgeCount() * (int) (this.getWidth() / canvas.getScale()));
           // l.setMaxIterations(700);
        this.layout = new ObservableLayout<>(l);
               //TODO make the dimension changeable from settings menu?

           // logger.debug(canvas.getScale() + " " + this.getWidth() + " " + this.getHeight());
        layout.setSize(new Dimension((int) (this.getWidth() / (2 * canvas.getScale())), (int) (this.getHeight() / (2 * canvas.getScale()))));

            //layout.set

        final Executor layouter = Executors.newSingleThreadExecutor();

        layouter.execute(() -> {

            while (!layout.done()) {
                layout.step();
                Platform.runLater(this::repaint);
            }

        });



    }



    @Override
    public void setDoubleBuffered(boolean b) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean isDoubleBuffered() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public VisualizationModel<V, E> getModel() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setModel(VisualizationModel<V, E> visualizationModel) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setRenderer(Renderer<V, E> renderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Renderer<V, E> getRenderer() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setGraphLayout(Layout<V, E> layout) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Layout<V, E> getGraphLayout() {
        return layout;
    }


    @Override
    public Map<RenderingHints.Key, Object> getRenderingHints() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setRenderingHints(Map<RenderingHints.Key, Object> map) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void addPreRenderPaintable(Paintable paintable) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void removePreRenderPaintable(Paintable paintable) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void addPostRenderPaintable(Paintable paintable) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void removePostRenderPaintable(Paintable paintable) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void fireStateChanged() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public PickedState<V> getPickedVertexState() {
       return pickedNodes;
    }

    @Override
    public PickedState<E> getPickedEdgeState() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickedVertexState(PickedState<V> pickedState) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickedEdgeState(PickedState<E> pickedState) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public GraphElementAccessor<V, E> getPickSupport() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickSupport(GraphElementAccessor<V, E> graphElementAccessor) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Point2D getCenter() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public RenderContext<V, E> getRenderContext() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setRenderContext(RenderContext<V, E> renderContext) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    synchronized
    public void repaint() {


       this.layout.getGraph().getVertices().forEach(v -> {

            //this.layout = new FRLayout<>(observableGraph);

            final ViewComponent vc = v.getComponent(ViewComponent.class);

            //System.out.println(layout.transform(v));

           vc.getRenderer().getShape().setLayoutX(layout.transform(v).getX());
           vc.getRenderer().getShape().setLayoutY(layout.transform(v).getY());

        });


    }

    public PannableCanvas getCanvas() {
        return canvas;
    }

    private void onSelectionChanged() {

        final IUserCommand selectionCommand = interactionMap.get(GraphInteraction.SELECTION);

        if (selectionCommand == null) {
            logger.warn("There is no command registered to: " + GraphInteraction.SELECTION);
            return;
        }

        selectionCommand.setSelection(new ImmutablePair<>(new HashSet<>(selectedNodes), new HashSet<>(selectedEdges)));

        notifyListeners(selectionCommand);

    }

    private void onSelectionContextMenu(double posX, double posY) {
        //FIXME: make this adequate
        final SelectionContextMenuCommand cmCommand = (SelectionContextMenuCommand)interactionMap.get(GraphInteraction.SELECTION_CONTEXTMENU);

        if (cmCommand == null) {
            logger.warn("There is no command registered to: " + GraphInteraction.SELECTION_CONTEXTMENU);
            return;
        }
        cmCommand.setSelection(new ImmutablePair<>(new HashSet<>(selectedNodes), new HashSet<>(selectedEdges)));
        cmCommand.setPosX(posX);
        cmCommand.setPosY(posY);

        notifyListeners(cmCommand);
    }



    @Override
    public final boolean addListener(final IListener<IUserCommand> listener) {

        return viewControllerNotifier.addListener(listener);
    }

    @Override
    public final boolean removeListener(final IListener<IUserCommand> listener) {
        return viewControllerNotifier.removeListener(listener);
    }

    @Override
    public final void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }

    @Override
    public void addCommand(GraphInteraction interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }

    public void selectAllNodes() {

        getGraphLayout().getGraph().getVertices().stream().forEach(vertex -> {

            final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();

            if (!renderer.picked()) {
                selectedNodes.add(vertex);
                Platform.runLater(() -> {
                    renderer.isPicked(true);
                });
            }
        });
        onSelectionChanged();
    }

    private static class MyPickedState<T> implements PickedState<T> {

        private final Set<T> pickedNodes;

        public MyPickedState(Set<T> selectedNodes) {

            this.pickedNodes = selectedNodes;
        }

        @Override
        public boolean pick(T v, boolean b) {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public void clear() {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public Set<T> getPicked() {
            return pickedNodes;
        }

        @Override
        public boolean isPicked(T v) {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public Object[] getSelectedObjects() {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public void addItemListener(ItemListener l) {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }

        @Override
        public void removeItemListener(ItemListener l) {
            //TODO implement this method
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
