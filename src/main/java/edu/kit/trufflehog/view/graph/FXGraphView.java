package edu.kit.trufflehog.view.graph;


import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

/**
 * Created by root on 24.01.16.
 */
public class FXGraphView implements VisualizationServer<INode, IConnection> {
    @Override
    public void setDoubleBuffered(boolean b) {

    }

    @Override
    public boolean isDoubleBuffered() {
        return false;
    }

    @Override
    public VisualizationModel<INode, IConnection> getModel() {
        return null;
    }

    @Override
    public void setModel(VisualizationModel<INode, IConnection> visualizationModel) {

    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {

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
    public void setVisible(boolean b) {

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

    }

    @Override
    public PickedState<INode> getPickedVertexState() {
        return null;
    }

    @Override
    public PickedState<IConnection> getPickedEdgeState() {
        return null;
    }

    @Override
    public void setPickedVertexState(PickedState<INode> pickedState) {

    }

    @Override
    public void setPickedEdgeState(PickedState<IConnection> pickedState) {

    }

    @Override
    public GraphElementAccessor<INode, IConnection> getPickSupport() {
        return null;
    }

    @Override
    public void setPickSupport(GraphElementAccessor<INode, IConnection> graphElementAccessor) {

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

    }
}
