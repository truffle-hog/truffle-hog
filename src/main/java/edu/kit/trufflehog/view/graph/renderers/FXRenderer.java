package edu.kit.trufflehog.view.graph.renderers;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import java.util.ConcurrentModificationException;

/**
 * Created by root on 16.02.16.
 */
public class FXRenderer<V, E> implements Renderer<V, E> {

    Vertex<V,E> vertexRenderer = new FXVertexRenderer<>();
    VertexLabel<V,E> vertexLabelRenderer = new BasicVertexLabelRenderer<>();
    Edge<V,E> edgeRenderer = new FXEdgeRenderer<>();
    EdgeLabel<V,E> edgeLabelRenderer = new FXEdgeLabelRenderer<>();

    public void render(RenderContext<V, E> renderContext, Layout<V, E> layout) {

        // paint all the edges
        try {
            for(E e : layout.getGraph().getEdges()) {

                renderEdge(
                        renderContext,
                        layout,
                        e);
                renderEdgeLabel(
                        renderContext,
                        layout,
                        e);
            }
        } catch(ConcurrentModificationException cme) {
            renderContext.getScreenDevice().repaint();
        }

        // paint all the vertices
        try {
            for(V v : layout.getGraph().getVertices()) {

                renderVertex(
                        renderContext,
                        layout,
                        v);
                renderVertexLabel(
                        renderContext,
                        layout,
                        v);
            }
        } catch(ConcurrentModificationException cme) {
            renderContext.getScreenDevice().repaint();
        }
    }

    public void renderVertex(RenderContext<V,E> rc, Layout<V,E> layout, V v) {
        vertexRenderer.paintVertex(rc, layout, v);
    }

    public void renderVertexLabel(RenderContext<V,E> rc, Layout<V,E> layout, V v) {
        vertexLabelRenderer.labelVertex(rc, layout, v, rc.getVertexLabelTransformer().transform(v));
    }

    public void renderEdge(RenderContext<V,E> rc, Layout<V,E> layout, E e) {
        edgeRenderer.paintEdge(rc, layout, e);
    }

    public void renderEdgeLabel(RenderContext<V,E> rc, Layout<V,E> layout, E e) {
        edgeLabelRenderer.labelEdge(rc, layout, e, rc.getEdgeLabelTransformer().transform(e));
    }

    public void setVertexRenderer(Vertex<V,E> r) {
        this.vertexRenderer = r;
    }

    public void setEdgeRenderer(Edge<V,E> r) {
        this.edgeRenderer = r;
    }

    /**
     * @return the edgeLabelRenderer
     */
    public EdgeLabel<V, E> getEdgeLabelRenderer() {
        return edgeLabelRenderer;
    }

    /**
     * @param edgeLabelRenderer the edgeLabelRenderer to set
     */
    public void setEdgeLabelRenderer(EdgeLabel<V, E> edgeLabelRenderer) {
        this.edgeLabelRenderer = edgeLabelRenderer;
    }

    /**
     * @return the vertexLabelRenderer
     */
    public VertexLabel<V, E> getVertexLabelRenderer() {
        return vertexLabelRenderer;
    }

    /**
     * @param vertexLabelRenderer the vertexLabelRenderer to set
     */
    public void setVertexLabelRenderer(
            VertexLabel<V, E> vertexLabelRenderer) {
        this.vertexLabelRenderer = vertexLabelRenderer;
    }

    /**
     * @return the edgeRenderer
     */
    public Edge<V, E> getEdgeRenderer() {
        return edgeRenderer;
    }

    /**
     * @return the vertexRenderer
     */
    public Vertex<V, E> getVertexRenderer() {
        return vertexRenderer;
    }


}