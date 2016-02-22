package edu.kit.trufflehog.view.graph;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by root on 24.01.16.
 */
public class FXRenderContext<V, E> implements RenderContext<V, E> {
    @Override
    public int getLabelOffset() {
        return 0;
    }

    @Override
    public void setLabelOffset(int i) {

    }

    @Override
    public float getArrowPlacementTolerance() {
        return 0;
    }

    @Override
    public void setArrowPlacementTolerance(float v) {

    }

    @Override
    public Transformer<Context<Graph<V, E>, E>, Shape> getEdgeArrowTransformer() {
        return null;
    }

    @Override
    public void setEdgeArrowTransformer(Transformer<Context<Graph<V, E>, E>, Shape> transformer) {

    }

    @Override
    public Predicate<Context<Graph<V, E>, E>> getEdgeArrowPredicate() {
        return null;
    }

    @Override
    public void setEdgeArrowPredicate(Predicate<Context<Graph<V, E>, E>> predicate) {

    }

    @Override
    public Transformer<E, Font> getEdgeFontTransformer() {
        return null;
    }

    @Override
    public void setEdgeFontTransformer(Transformer<E, Font> transformer) {

    }

    @Override
    public Predicate<Context<Graph<V, E>, E>> getEdgeIncludePredicate() {
        return null;
    }

    @Override
    public void setEdgeIncludePredicate(Predicate<Context<Graph<V, E>, E>> predicate) {

    }

    @Override
    public Transformer<Context<Graph<V, E>, E>, Number> getEdgeLabelClosenessTransformer() {
        return null;
    }

    @Override
    public void setEdgeLabelClosenessTransformer(Transformer<Context<Graph<V, E>, E>, Number> transformer) {

    }

    @Override
    public EdgeLabelRenderer getEdgeLabelRenderer() {
        return null;
    }

    @Override
    public void setEdgeLabelRenderer(EdgeLabelRenderer edgeLabelRenderer) {

    }

    @Override
    public Transformer<E, Paint> getEdgeFillPaintTransformer() {
        return null;
    }

    @Override
    public void setEdgeFillPaintTransformer(Transformer<E, Paint> transformer) {

    }

    @Override
    public Transformer<E, Paint> getEdgeDrawPaintTransformer() {
        return null;
    }

    @Override
    public void setEdgeDrawPaintTransformer(Transformer<E, Paint> transformer) {

    }

    @Override
    public Transformer<E, Paint> getArrowDrawPaintTransformer() {
        return null;
    }

    @Override
    public void setArrowDrawPaintTransformer(Transformer<E, Paint> transformer) {

    }

    @Override
    public Transformer<E, Paint> getArrowFillPaintTransformer() {
        return null;
    }

    @Override
    public void setArrowFillPaintTransformer(Transformer<E, Paint> transformer) {

    }

    @Override
    public Transformer<Context<Graph<V, E>, E>, Shape> getEdgeShapeTransformer() {
        return null;
    }

    @Override
    public void setEdgeShapeTransformer(Transformer<Context<Graph<V, E>, E>, Shape> transformer) {

    }

    @Override
    public Transformer<E, String> getEdgeLabelTransformer() {
        return null;
    }

    @Override
    public void setEdgeLabelTransformer(Transformer<E, String> transformer) {

    }

    @Override
    public Transformer<E, Stroke> getEdgeStrokeTransformer() {
        return null;
    }

    @Override
    public void setEdgeStrokeTransformer(Transformer<E, Stroke> transformer) {

    }

    @Override
    public Transformer<E, Stroke> getEdgeArrowStrokeTransformer() {
        return null;
    }

    @Override
    public void setEdgeArrowStrokeTransformer(Transformer<E, Stroke> transformer) {

    }

    @Override
    public GraphicsDecorator getGraphicsContext() {
        return null;
    }

    @Override
    public void setGraphicsContext(GraphicsDecorator graphicsDecorator) {

    }

    @Override
    public EdgeIndexFunction<V, E> getParallelEdgeIndexFunction() {
        return null;
    }

    @Override
    public void setParallelEdgeIndexFunction(EdgeIndexFunction<V, E> edgeIndexFunction) {

    }

    @Override
    public PickedState<E> getPickedEdgeState() {
        return null;
    }

    @Override
    public void setPickedEdgeState(PickedState<E> pickedState) {

    }

    @Override
    public PickedState<V> getPickedVertexState() {
        return null;
    }

    @Override
    public void setPickedVertexState(PickedState<V> pickedState) {

    }

    @Override
    public CellRendererPane getRendererPane() {
        return null;
    }

    @Override
    public void setRendererPane(CellRendererPane cellRendererPane) {

    }

    @Override
    public JComponent getScreenDevice() {
        return null;
    }

    @Override
    public void setScreenDevice(JComponent jComponent) {

    }

    @Override
    public Transformer<V, Font> getVertexFontTransformer() {
        return null;
    }

    @Override
    public void setVertexFontTransformer(Transformer<V, Font> transformer) {

    }

    @Override
    public Transformer<V, Icon> getVertexIconTransformer() {
        return null;
    }

    @Override
    public void setVertexIconTransformer(Transformer<V, Icon> transformer) {

    }

    @Override
    public Predicate<Context<Graph<V, E>, V>> getVertexIncludePredicate() {
        return null;
    }

    @Override
    public void setVertexIncludePredicate(Predicate<Context<Graph<V, E>, V>> predicate) {

    }

    @Override
    public VertexLabelRenderer getVertexLabelRenderer() {
        return null;
    }

    @Override
    public void setVertexLabelRenderer(VertexLabelRenderer vertexLabelRenderer) {

    }

    @Override
    public Transformer<V, Paint> getVertexFillPaintTransformer() {
        return null;
    }

    @Override
    public void setVertexFillPaintTransformer(Transformer<V, Paint> transformer) {

    }

    @Override
    public Transformer<V, Paint> getVertexDrawPaintTransformer() {
        return null;
    }

    @Override
    public void setVertexDrawPaintTransformer(Transformer<V, Paint> transformer) {

    }

    @Override
    public Transformer<V, Shape> getVertexShapeTransformer() {
        return null;
    }

    @Override
    public void setVertexShapeTransformer(Transformer<V, Shape> transformer) {

    }

    @Override
    public Transformer<V, String> getVertexLabelTransformer() {
        return null;
    }

    @Override
    public void setVertexLabelTransformer(Transformer<V, String> transformer) {

    }

    @Override
    public Transformer<V, Stroke> getVertexStrokeTransformer() {
        return null;
    }

    @Override
    public void setVertexStrokeTransformer(Transformer<V, Stroke> transformer) {

    }

    @Override
    public MultiLayerTransformer getMultiLayerTransformer() {
        return null;
    }

    @Override
    public void setMultiLayerTransformer(MultiLayerTransformer multiLayerTransformer) {

    }

    @Override
    public GraphElementAccessor<V, E> getPickSupport() {
        return null;
    }

    @Override
    public void setPickSupport(GraphElementAccessor<V, E> graphElementAccessor) {

    }
}
