package edu.kit.trufflehog.view.graph.renderers;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformerDecorator;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Created by root on 25.02.16.
 */
public class FXVertexRenderer<V,E> implements Renderer.Vertex<V,E> {

    public void paintVertex(RenderContext<V,E> rc, Layout<V,E> layout, V v) {
        Graph<V,E> graph = layout.getGraph();
        if (rc.getVertexIncludePredicate().evaluate(Context.getInstance(graph,v))) {
            paintIconForVertex(rc, v, layout);
        }
    }

    /**
     * Paint <code>v</code>'s icon on <code>g</code> at <code>(x,y)</code>.
     */
    protected void paintIconForVertex(RenderContext<V,E> rc, V v, Layout<V,E> layout) {

        GraphicsDecorator g = rc.getGraphicsContext();
        boolean vertexHit = true;
        // get the shape to be rendered
        Shape shape = rc.getVertexShapeTransformer().transform(v);

        Point2D p = layout.transform(v);
        p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);
        float x = (float)p.getX();
        float y = (float)p.getY();
        // create a transform that translates to the location of
        // the vertex to be rendered
        AffineTransform xform = AffineTransform.getTranslateInstance(x,y);
        // transform the vertex shape with xtransform
        shape = xform.createTransformedShape(shape);

        vertexHit = vertexHit(rc, shape);
        //rc.getViewTransformer().transform(shape).intersects(deviceRectangle);

        if (vertexHit) {
            if(rc.getVertexIconTransformer() != null) {
                Icon icon = rc.getVertexIconTransformer().transform(v);
                if(icon != null) {

                    g.draw(icon, rc.getScreenDevice(), shape, (int)x, (int)y);

                } else {
                    paintShapeForVertex(rc, v, shape);
                }
            } else {
                paintShapeForVertex(rc, v, shape);
            }
        }
    }

    protected boolean vertexHit(RenderContext<V,E> rc, Shape s) {
        JComponent vv = rc.getScreenDevice();
        Rectangle deviceRectangle = null;
        if(vv != null) {
            Dimension d = vv.getSize();
            deviceRectangle = new Rectangle(
                    0,0,
                    d.width,d.height);
        }
        MutableTransformer vt = rc.getMultiLayerTransformer().getTransformer(Layer.VIEW);
        if(vt instanceof MutableTransformerDecorator) {
            vt = ((MutableTransformerDecorator)vt).getDelegate();
        }
        return vt.transform(s).intersects(deviceRectangle);
    }

    protected void paintShapeForVertex(RenderContext<V,E> rc, V v, Shape shape) {

        GraphicsDecorator g = rc.getGraphicsContext();
        Paint oldPaint = g.getPaint();
        Paint fillPaint = rc.getVertexFillPaintTransformer().transform(v);
        if(fillPaint != null) {
            g.setPaint(fillPaint);
            g.fill(shape);
            g.setPaint(oldPaint);
        }
        //Paint drawPaint = rc.getVertexDrawPaintTransformer().transform(v);
/*        if(drawPaint != null) {
            g.setPaint(drawPaint);
            Stroke oldStroke = g.getStroke();
            Stroke stroke = rc.getVertexStrokeTransformer().transform(v);
            if(stroke != null) {
                g.setStroke(stroke);
            }
            g.draw(shape);
            g.setPaint(oldPaint);
            g.setStroke(oldStroke);
        }*/
    }
}