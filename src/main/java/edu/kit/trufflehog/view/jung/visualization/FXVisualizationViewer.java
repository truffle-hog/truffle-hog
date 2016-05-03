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
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
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
public class FXVisualizationViewer extends Pane implements VisualizationServer<INode, IConnection>, IViewController<GraphInteraction> {

    private static final Logger logger = LogManager.getLogger();

    private final PannableCanvas canvas;
    private final NodeGestures nodeGestures;
    private final EdgeGestures edgeGestures = new EdgeGestures();

    private boolean wasRightClicked = false;

    private final SelectionModel selectionModel = new SelectionModel();

    private final PickedState<INode> pickedNodes = new MyPickedState<>(selectionModel.selectedVertices);

    private final INotifier<IUserCommand> viewControllerNotifier = new Notifier<IUserCommand>() {
        @Override
        public boolean addListener(IListener<IUserCommand> listener) {
            return super.addListener(listener);
        }
    };

    /** The commands that are mapped to their interactions. **/
    private final Map<GraphInteraction, IUserCommand> interactionMap =
            new EnumMap<>(GraphInteraction.class);

    private ObservableLayout<INode, IConnection> layout;

    private INetworkViewPort port;

    public FXVisualizationViewer(final ObservableLayout<INode, IConnection> layout, INetworkViewPort port) {

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

        //StackPane spane = new StackPane();
        //spane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));

        Pane ghost = new Pane();
        canvas = new PannableCanvas(ghost);

        Pane parent = new Pane();
        parent.getChildren().add(ghost);
        parent.getChildren().add(canvas);


        SceneGestures sceneGestures = new SceneGestures(parent, canvas);

        addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        new RubberBandSelection(this);



        // TODO make canvas transparent
        //canvas.setStyle("-fx-background-color: #1d1d1d");

        // we don't want the canvas on the top/left in this example => just
        // translate it a bit
        //canvas.setTranslateX(100);
        //canvas.setTranslateY(100);

        // create sample nodes which can be dragged
        nodeGestures = new NodeGestures();
        //this.getChildren().add(spane);
        this.getChildren().add(parent);

        this.layout = layout;

        //this.layout.getGraph().getVertices().forEach(v -> Platform.runLater(() -> this.initVertex(v)));
        //this.layout.getGraph().getEdges().forEach(e -> Platform.runLater(() -> this.initEdge(e)));

        this.layout.getObservableGraph().addGraphEventListener(e -> {

            //Platform.runLater(() -> {

                switch (e.getType()) {
                    case VERTEX_ADDED:
                        final INode node = ((GraphEvent.Vertex<INode, IConnection>) e).getVertex();
                        Platform.runLater(() -> initVertex(node));
                        break;

                    case EDGE_ADDED:
                        final IConnection edge = ((GraphEvent.Edge<INode, IConnection>) e).getEdge();
                        Platform.runLater(() -> initEdge(edge));
                        break;

                    case VERTEX_CHANGED:
                        break;

                    case EDGE_CHANGED:
                        final IConnection changedEdge = ((GraphEvent.Edge<INode, IConnection>) e).getEdge();
                        Platform.runLater(() -> changedEdge.getComponent(ViewComponent.class).getRenderer().animate());
                        break;
                }
            //});
        });


    }

    // TODO check if synch is needed
    synchronized
    private void initEdge(IConnection edge) {

        final Pair<INode> pair = this.layout.getGraph().getEndpoints(edge);

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
        final DoubleBinding srcX = srcShape.layoutXProperty().add(srcShape.translateXProperty());
        final DoubleBinding srcY = srcShape.layoutYProperty().add(srcShape.translateYProperty());
        final DoubleBinding destX = destShape.layoutXProperty().add(destShape.translateXProperty());
        final DoubleBinding destY = destShape.layoutYProperty().add(destShape.translateYProperty());


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

        curve.addEventFilter(MouseEvent.MOUSE_PRESSED, edgeGestures.getOnMousePressedEventHandler(edge));
        curve.addEventFilter(MouseEvent.MOUSE_RELEASED, edgeGestures.getOnMouseReleasedEventHandler(edge));
        edgeRenderer.getArrowShape().addEventFilter(MouseEvent.MOUSE_PRESSED, edgeGestures.getOnMousePressedEventHandler(edge));
        edgeRenderer.getArrowShape().addEventFilter(MouseEvent.MOUSE_RELEASED, edgeGestures.getOnMouseReleasedEventHandler(edge));

/*        // make the edge clickable
        curve.setOnMouseClicked(e -> {

            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            logger.debug(edge.getSrc().getComponent(NodeInfoComponent.class).toString() + " --[" + edge.getComponent(EdgeStatisticsComponent.class).getTraffic() +
                    "]-->" + edge.getDest().getComponent(NodeInfoComponent.class).toString());

//            edge.getComponent(ViewComponent.class).getRenderer().togglePicked();
        });

        edgeRenderer.getArrowShape().setOnMouseClicked(e -> {

            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            logger.debug(edge.getSrc().getComponent(NodeInfoComponent.class).toString() + " --[" + edge.getComponent(EdgeStatisticsComponent.class).getTraffic() +
            "]-->" + edge.getDest().getComponent(NodeInfoComponent.class).toString());

//            edge.getComponent(ViewComponent.class).getRenderer().togglePicked();


        });*/



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
    private void initVertex(INode vertex) {

        if (vertex.getAddress().isMulticast()) {
            return;
        }

        final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();
        final Shape nodeShape = renderer.getShape();
        //nodeShape.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        //nodeShape.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

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

        //nodeLabel.layoutXProperty().bindBidirectionalWithOffset(labelX);
        //nodeLabel.layoutYProperty().bindBidirectionalWithOffset(labelY);

        //nodeLabel.layoutXProperty().bind(nodeShape.layoutXProperty().add(nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty())));

        nodeLabel.textFillProperty().bind(new SimpleObjectProperty<>(Color.WHITE));

        //MyBindings.bindBidirectionalWithOffset(nodeLabel.layoutXProperty(), nodeShape.layoutXProperty(), nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty()));
        //MyBindings.bindBidirectionalWithOffset(nodeLabel.layoutYProperty(), nodeShape.layoutYProperty(), nodeCircle.radiusProperty().multiply(nodeShape.scaleYProperty()));

        nodeLabel.layoutXProperty().bind(nodeShape.layoutXProperty().add(nodeShape.translateXProperty()).add(nodeCircle.radiusProperty().multiply(nodeShape.scaleXProperty())));
        nodeLabel.layoutYProperty().bind(nodeShape.layoutYProperty().add(nodeShape.translateYProperty()).add(nodeCircle.radiusProperty().multiply(nodeShape.scaleYProperty())));

        NodeInfoComponent nic = vertex.getComponent(NodeInfoComponent.class);
        if (nic != null) {
            nodeLabel.textProperty().bind(nic.toStringBinding());
        }

        nodeLabel.scaleXProperty().bind(Bindings.divide(1, canvas.scaleXProperty()));
        nodeLabel.scaleYProperty().bind(Bindings.divide(1, canvas.scaleYProperty()));

        nodeLabel.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler(vertex));
        nodeLabel.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler(vertex));
        nodeLabel.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler(vertex));

        nodeShape.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler(vertex));
        nodeShape.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler(vertex));
        nodeShape.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler(vertex));


        canvas.getChildren().addAll(nodeLabel, nodeShape);
    }

    synchronized
    public void refreshLayout() {

      //  logger.debug("refresh");
        final FRLayout2<INode, IConnection> l = new FRLayout2<>(this.layout.getObservableGraph());
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
    public VisualizationModel<INode, IConnection> getModel() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setModel(VisualizationModel<INode, IConnection> visualizationModel) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setRenderer(Renderer<INode, IConnection> renderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Renderer<INode, IConnection> getRenderer() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setGraphLayout(Layout<INode, IConnection> layout) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Layout<INode, IConnection> getGraphLayout() {
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
    public PickedState<INode> getPickedVertexState() {
       return pickedNodes;
    }

    @Override
    public PickedState<IConnection> getPickedEdgeState() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickedVertexState(PickedState<INode> pickedState) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickedEdgeState(PickedState<IConnection> pickedState) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public GraphElementAccessor<INode, IConnection> getPickSupport() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setPickSupport(GraphElementAccessor<INode, IConnection> graphElementAccessor) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Point2D getCenter() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public RenderContext<INode, IConnection> getRenderContext() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setRenderContext(RenderContext<INode, IConnection> renderContext) {
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

        selectionCommand.setSelection(new ImmutablePair<>(new HashSet<>(selectionModel.getSelectedVertices()), new HashSet<>(selectionModel.getSelectedEdges())));

        notifyListeners(selectionCommand);

    }

    private void onSelectionContextMenu(double posX, double posY) {
        //FIXME: make this adequate
        final SelectionContextMenuCommand cmCommand = (SelectionContextMenuCommand)interactionMap.get(GraphInteraction.SELECTION_CONTEXTMENU);

        if (cmCommand == null) {
            logger.warn("There is no command registered to: " + GraphInteraction.SELECTION_CONTEXTMENU);
            return;
        }
        cmCommand.setSelection(new ImmutablePair<>(new HashSet<>(selectionModel.getSelectedVertices()), new HashSet<>(selectionModel.getSelectedEdges())));
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


            /*final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();

            if (!renderer.picked()) {
                selectedNodes.add(vertex);
                Platform.runLater(() -> {
                    renderer.isPicked(true);
                });
            }*/
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

/*    private class Selectable extends Region {

        ImageView view;

        public Selectable( double width, double height) {

            view = new ImageView( image);
            view.setFitWidth(width);
            view.setFitHeight(height);

            getChildren().add( view);

            this.setPrefSize(width, height);
        }

    }*/

    private class SelectionModel {

        private final Set<Node> selection = new HashSet<>();

        private final Set<INode> selectedVertices = new HashSet<>();

        private final Set<IConnection> selectedEdges = new HashSet<>();

        //private final Set<Node> lineSelection = new HashSet<>();

        public Set<INode> getSelectedVertices() {

            return selectedVertices;
        }

        public Set<IConnection> getSelectedEdges() {
            return selectedEdges;
        }

        public Set<Node> getSelectedNodes() {

            return selection;
        }

        public void add(IConnection c) {

            IEdgeRenderer renderer = (IEdgeRenderer) c.getComponent(ViewComponent.class).getRenderer();

            renderer.setCurrentFill(renderer.getArrowFillPicked());
            //renderer.getArrowShape().setFill(renderer.getArrowFillPicked());
            //renderer.getLine().setFill(renderer.getArrowFillPicked());

            selectedEdges.add(c);

            onSelectionChanged();
        }

        public void add(INode v) {

            final IRenderer renderer = v.getComponent(ViewComponent.class).getRenderer();

            renderer.getShape().setStroke(Color.WHITE);
            //renderer.getShape().setStrokeWidth(3);

            //renderer.getShape().setStyle("fx-stroke: WHITE;");

            //if (!renderer.getShape().getStyleClass().contains("picked")) {
             //   renderer.getShape().getStyleClass().add("picked");
            //}

            selection.add(renderer.getShape());
            selectedVertices.add(v);

            onSelectionChanged();


        }

        public void remove(INode v) {

            final IRenderer renderer = v.getComponent(ViewComponent.class).getRenderer();

            //renderer.getShape().setStyle("fx-stroke: null;");
            renderer.getShape().setStroke(null);
            //renderer.getShape().getStyleClass().remove("picked");
            selection.remove(renderer.getShape());
            selectedVertices.remove(v);

            onSelectionChanged();


        }

        public void remove(IConnection c) {

            final IEdgeRenderer renderer = (IEdgeRenderer) c.getComponent(ViewComponent.class).getRenderer();

            renderer.setCurrentFill(renderer.getArrowFillUnpicked());
           // renderer.getLine().setFill(renderer.getArrowFillUnpicked());

            selectedEdges.remove(c);

            onSelectionChanged();
        }

        public void clear() {
            clearVertices();
            clearEdges();
        }

        public void clearVertices() {

            while (!selectedVertices.isEmpty()) {
                remove(selectedVertices.iterator().next());
            }
        }

        public void clearEdges() {
            while (!selectedEdges.isEmpty()) {
                remove(selectedEdges.iterator().next());
            }
        }

        public boolean contains(Node node) {
            return selection.contains(node);
        }

        public boolean contains(INode v) {
            return selectedVertices.contains(v);
        }

        public boolean contains(IConnection c) { return selectedEdges.contains(c); }

        public int size() {
            return selectedVertices.size();
        }

        public void log() {
            System.out.println( "Items in model: " + Arrays.asList( selectedVertices.toArray()));
        }

    }

    private class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect;

        Pane group;
        boolean enabled = false;

        public RubberBandSelection(Pane group) {

            this.group = group;

            rect = new Rectangle(0, 0, 0, 0);
            rect.setStroke(Color.web("0x4089BF"));
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // simple flag to prevent multiple handling of this event or we'd get an exception because rect is already on the scene
                // eg if you drag with left mouse button and while doing that click the right mouse button
                if(enabled) {
                    return;
                }
                dragContext.mouseAnchorX = event.getSceneX();
                dragContext.mouseAnchorY = event.getSceneY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add(rect);
                event.consume();
                enabled = true;
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( !event.isShiftDown() && !event.isControlDown()) {
                    selectionModel.clear();
                }
                getGraphLayout().getGraph().getVertices().stream().forEach(vertex -> {

                    final IRenderer renderer = vertex.getComponent(ViewComponent.class).getRenderer();

                    if (rect.localToScene(rect.getBoundsInLocal()).contains(renderer.getShape().localToScene(renderer.getShape().getBoundsInLocal()))) {

                        selectionModel.add(vertex);
                    }
/*
                    if (renderer.getShape().getBoundsInParent().intersects(rect.getBoundsInParent())) {

                        selectionModel.add(vertex);
                    }*/
                });
                /*getGraphLayout().getGraph().getVertices().stream().forEach(vertex -> {

                });
                onSelectionChanged();

                for( Node node: group.getChildren()) {

                        if( node.getBoundsInParent().intersects( rect.getBoundsInParent())) {

                            if( event.isShiftDown()) {

                                selectionModel.add( node);

                            } else if( event.isControlDown()) {

                                if( selectionModel.contains( node)) {
                                    selectionModel.remove( node);
                                } else {
                                    selectionModel.add( node);
                                }
                            } else {
                                selectionModel.add( node);
                            }

                        }


                }*/

                //selectionModel.log();

                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);

                event.consume();

                enabled = false;
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                final double parentScaleX = group.
                        localToSceneTransformProperty().getValue().getMxx();
                final double parentScaleY = group.
                        localToSceneTransformProperty().getValue().getMyy();

                dragContext.translateAnchorX = event.getSceneX();
                dragContext.translateAnchorY = event.getSceneY();

                dragContext.mouseAnchorX = Math.max(dragContext.mouseAnchorX, 0);
                dragContext.mouseAnchorY = Math.max(dragContext.mouseAnchorY, 0);

                dragContext.translateAnchorX = Math.max(dragContext.translateAnchorX, 0);
                dragContext.translateAnchorY = Math.max(dragContext.translateAnchorY, 0);

                double x = Math.min(dragContext.mouseAnchorX, dragContext.translateAnchorX);
                double y = Math.min(dragContext.mouseAnchorY, dragContext.translateAnchorY);

                double width = Math.abs(dragContext.translateAnchorX - dragContext.mouseAnchorX);
                double height = Math.abs(dragContext.translateAnchorY - dragContext.mouseAnchorY);

                rect.setX(x);
                rect.setY(y);
                rect.setWidth(width);
                rect.setHeight(height);

                event.consume();
            }
        };

//        private final class DragContext {
//
//            public double mouseAnchorX;
//            public double mouseAnchorY;
//
//
//        }
    }

    private class EdgeGestures {

        private boolean enabled = false;

        public EdgeGestures() {

        }

        public EventHandler<MouseEvent> getOnMousePressedEventHandler(IConnection c) {

            return event -> {

                // left mouse button => dragging
                if( !event.isPrimaryButtonDown())
                    return;

                // don't do anything if the user is in the process of adding to the selection model
                if (event.isControlDown() || event.isShiftDown()) {

                    if (selectionModel.contains(c)) {
                        selectionModel.remove(c);
                    } else {

                        selectionModel.add(c);
                    }
                    return;
                }
                final Node node = (Node) event.getSource();

                // clearVertices the model if the current node isn't in the selection => new selection
                if (!selectionModel.contains(c)) {
                    selectionModel.clearEdges();
                    selectionModel.add(c);
                }
                // flag that the mouse released handler should consume the event, so it won't bubble up to the pane which has a rubberband selection mouse released handler
                enabled = true;

                // prevent rubberband selection handler
                event.consume();
            };
        }

        public EventHandler<MouseEvent> getOnMouseReleasedEventHandler(IConnection n) {

            return event -> {

                // prevent rubberband selection handler
                if (enabled) {

                    enabled = false;
                    event.consume();
                }
            };
        }

    }

    private class NodeGestures {

        private DragContext dragContext = new DragContext();

        private boolean enabled = false;

        public NodeGestures() {

        }

        public EventHandler<MouseEvent> getOnMousePressedEventHandler(INode n) {

            return event -> {

                // left mouse button => dragging
                if( event.isPrimaryButtonDown()) {
                    // don't do anything if the user is in the process of adding to the selection model
                    if (event.isControlDown() || event.isShiftDown()) {

                        if (selectionModel.contains(n)) {
                            selectionModel.remove(n);
                        } else {

                            selectionModel.add(n);
                        }
                        return;
                    }
                    final Node node = (Node) event.getSource();

                    dragContext.startX = event.getSceneX();
                    dragContext.startY = event.getSceneY();

                    // clearVertices the model if the current node isn't in the selection => new selection
                    if (!selectionModel.contains(n)) {
                        selectionModel.clearVertices();
                        selectionModel.add(n);
                    }
                    // flag that the mouse released handler should consume the event, so it won't bubble up to the pane which has a rubberband selection mouse released handler
                    enabled = true;
                } else if(event.isSecondaryButtonDown()) {
                    wasRightClicked = true;
                }
                // prevent rubberband selection handler
                event.consume();
            };
        }

        public EventHandler<MouseEvent> getOnMouseDraggedEventHandler(INode n) {

            return event -> {

                // left mouse button => dragging
                if( !event.isPrimaryButtonDown())
                    return;

                if (!enabled) {
                    return;
                }
                // all in selection
                for (Node node: selectionModel.getSelectedNodes()) {

                    node.setTranslateX((event.getSceneX() - dragContext.startX) / canvas.getScale());
                    node.setTranslateY((event.getSceneY() - dragContext.startY) / canvas.getScale());
                }
            };
        }

        public EventHandler<MouseEvent> getOnMouseReleasedEventHandler(INode n) {

            return event -> {

                // prevent rubberband selection handler
                if (!wasRightClicked) {
                    if (enabled) {

                        // set node's layout position to current position,remove translate coordinates
                        //selectionModel.selection.forEach(this::fixPosition);

                        selectionModel.getSelectedNodes().stream().forEach(node -> {

                            node.setLayoutX(node.getLayoutX() + node.getTranslateX());
                            node.setLayoutY(node.getLayoutY() + node.getTranslateY());
                            node.setTranslateX(0);
                            node.setTranslateY(0);
                        });
                        enabled = false;

                    }
                } else {
                    if (enabled) enabled = false;
                    selectionModel.clear();
                    selectionModel.add(n);

                    onSelectionContextMenu(event.getScreenX(), event.getScreenY());

                    wasRightClicked = false;
                }
                event.consume();
            };
        }
    }
}
