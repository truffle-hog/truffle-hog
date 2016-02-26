/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 */
package edu.kit.trufflehog.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;

import edu.kit.trufflehog.view.graph.decorators.FXEdgeShape;
import edu.uci.ics.jung.visualization.BasicTransformer;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.TruePredicate;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.DefaultParallelEdgeIndexFunction;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;
import edu.uci.ics.jung.graph.util.IncidentEdgeIndexFunction;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.DirectionalEdgeArrowTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;


/**
 */
@SuppressWarnings("unchecked")
public class FXPluggableRenderContext<V, E> implements RenderContext<V, E> {
    
	protected float arrowPlacementTolerance = 1;
    protected Predicate<Context<Graph<V,E>,V>> vertexIncludePredicate = TruePredicate.getInstance();
    protected Transformer<V,Stroke> vertexStrokeTransformer = 
    	new ConstantTransformer(new BasicStroke(1.0f));
    
    protected Transformer<V,Shape> vertexShapeTransformer = 
        		new ConstantTransformer(
        		new Ellipse2D.Float(-10,-10,20,20));

    protected Transformer<V,String> vertexLabelTransformer = new ConstantTransformer(null);
    protected Transformer<V,Icon> vertexIconTransformer;
    protected Transformer<V,Font> vertexFontTransformer = 
        new ConstantTransformer(new Font("Helvetica", Font.PLAIN, 12));
    
    protected Transformer<V,Paint> vertexDrawPaintTransformer = new ConstantTransformer(Color.BLACK);
    protected Transformer<V,Paint> vertexFillPaintTransformer = new ConstantTransformer(Color.RED);
    
    protected Transformer<E,String> edgeLabelTransformer = new ConstantTransformer(null);
    protected Transformer<E,Stroke> edgeStrokeTransformer = new ConstantTransformer(new BasicStroke(1.0f));
    protected Transformer<E,Stroke> edgeArrowStrokeTransformer = new ConstantTransformer(new BasicStroke(1.0f));
    
    protected Transformer<Context<Graph<V,E>,E>,Shape> edgeArrowTransformer = 
        new DirectionalEdgeArrowTransformer<V,E>(10, 8, 4);
    
    protected Predicate<Context<Graph<V,E>,E>> edgeArrowPredicate = new DirectedEdgeArrowPredicate<V,E>();
    protected Predicate<Context<Graph<V,E>,E>> edgeIncludePredicate = TruePredicate.getInstance();
    protected Transformer<E,Font> edgeFontTransformer =
        new ConstantTransformer(new Font("Helvetica", Font.PLAIN, 12));
    protected Transformer<Context<Graph<V,E>,E>,Number> edgeLabelClosenessTransformer = 
        new ConstantDirectionalEdgeValueTransformer<V,E>(0.5, 0.65);
    protected Transformer<Context<Graph<V,E>,E>,Shape> edgeShapeTransformer =
            new EdgeShape.QuadCurve<>();
    protected Transformer<E,Paint> edgeFillPaintTransformer =
        new ConstantTransformer(null);
    protected Transformer<E,Paint> edgeDrawPaintTransformer =
        new ConstantTransformer(Color.black);
    protected Transformer<E,Paint> arrowFillPaintTransformer =
        new ConstantTransformer(Color.black);
    protected Transformer<E,Paint> arrowDrawPaintTransformer =
        new ConstantTransformer(Color.black);
    
    protected EdgeIndexFunction<V,E> parallelEdgeIndexFunction = 
        DefaultParallelEdgeIndexFunction.<V,E>getInstance();
    
    protected EdgeIndexFunction<V,E> incidentEdgeIndexFunction = 
        IncidentEdgeIndexFunction.<V,E>getInstance();
    
    protected MultiLayerTransformer multiLayerTransformer = new BasicTransformer();
    
	/**
	 * pluggable support for picking graph elements by
	 * finding them based on their coordinates.
	 */
	protected GraphElementAccessor<V, E> pickSupport;

    
    protected int labelOffset = LABEL_OFFSET;
    
    /**
     * the JComponent that this Renderer will display the graph on
     */
    protected JComponent screenDevice;
    
    protected PickedState<V> pickedVertexState;
    protected PickedState<E> pickedEdgeState;
    
    /**
     * The CellRendererPane is used here just as it is in JTree
     * and JTable, to allow a pluggable JLabel-based renderer for
     * Vertex and Edge label strings and icons.
     */
    protected CellRendererPane rendererPane = new CellRendererPane();
    
    /**
     * A default GraphLabelRenderer - picked Vertex labels are
     * blue, picked edge labels are cyan
     */
    protected VertexLabelRenderer vertexLabelRenderer = 
        new DefaultVertexLabelRenderer(new Color(0xf0caa3));
    
    protected EdgeLabelRenderer edgeLabelRenderer = new DefaultEdgeLabelRenderer(new Color(0xf0caa3));
    
    protected GraphicsDecorator graphicsContext;
    
    FXPluggableRenderContext() {
        this.setEdgeShapeTransformer(new EdgeShape.QuadCurve<V,E>());
    }

	/**
	 * @return the vertexShapeTransformer
	 */
	public Transformer<V, Shape> getVertexShapeTransformer() {
		return vertexShapeTransformer;
	}

	/**
	 * @param vertexShapeTransformer the vertexShapeTransformer to set
	 */
	public void setVertexShapeTransformer(
			Transformer<V, Shape> vertexShapeTransformer) {
		this.vertexShapeTransformer = vertexShapeTransformer;
	}

	/**
	 * @return the vertexStrokeTransformer
	 */
	public Transformer<V, Stroke> getVertexStrokeTransformer() {
		return vertexStrokeTransformer;
	}

	/**
	 * @param vertexStrokeTransformer the vertexStrokeTransformer to set
	 */
	public void setVertexStrokeTransformer(
			Transformer<V, Stroke> vertexStrokeTransformer) {
		this.vertexStrokeTransformer = vertexStrokeTransformer;
	}

	public static float[] getDashing() {
        return dashing;
    }

    public static float[] getDotting() {
        return dotting;
    }

    /**
     * @see RenderContext#getArrowPlacementTolerance() ()
     */
    public float getArrowPlacementTolerance() {
        return arrowPlacementTolerance;
    }

    /**
     * @see RenderContext#setArrowPlacementTolerance(float)
     */
    public void setArrowPlacementTolerance(float arrow_placement_tolerance) {
        this.arrowPlacementTolerance = arrow_placement_tolerance;
    }

    /**
     * @see RenderContext#getEdgeArrowTransformer()
     */
    public Transformer<Context<Graph<V,E>,E>,Shape> getEdgeArrowTransformer() {
        return edgeArrowTransformer;
    }

    /**
     * @see RenderContext#setEdgeArrowTransformer(Transformer)
     */
    public void setEdgeArrowTransformer(Transformer<Context<Graph<V,E>,E>,Shape> edgeArrowTransformer) {
        this.edgeArrowTransformer = edgeArrowTransformer;
    }

    /**
     * @see RenderContext#getEdgeArrowPredicate()
     */
    public Predicate<Context<Graph<V,E>,E>> getEdgeArrowPredicate() {
        return edgeArrowPredicate;
    }

    /**
     * @see RenderContext#setEdgeArrowPredicate(Predicate)
     */
    public void setEdgeArrowPredicate(Predicate<Context<Graph<V,E>,E>> edgeArrowPredicate) {
        this.edgeArrowPredicate = edgeArrowPredicate;
    }

    /**
     * @see RenderContext#getEdgeFontTransformer()
     */
    public Transformer<E,Font> getEdgeFontTransformer() {
        return edgeFontTransformer;
    }

    /**
     * @see RenderContext#setEdgeFontTransformer(Transformer)
     */
    public void setEdgeFontTransformer(Transformer<E,Font> edgeFontTransformer) {
        this.edgeFontTransformer = edgeFontTransformer;
    }

    /**
     * @see RenderContext#getEdgeIncludePredicate()
     */
    public Predicate<Context<Graph<V,E>,E>> getEdgeIncludePredicate() {
        return edgeIncludePredicate;
    }

    /**
     * @see RenderContext#setEdgeIncludePredicate(Predicate)
     */
    public void setEdgeIncludePredicate(Predicate<Context<Graph<V,E>,E>> edgeIncludePredicate) {
        this.edgeIncludePredicate = edgeIncludePredicate;
    }

    /**
     * @see RenderContext#getEdgeLabelClosenessTransformer()
     */
    public Transformer<Context<Graph<V,E>,E>,Number> getEdgeLabelClosenessTransformer() {
        return edgeLabelClosenessTransformer;
    }

    /**
     * @see RenderContext#setEdgeLabelClosenessTransformer(Transformer)
     *
     */
    public void setEdgeLabelClosenessTransformer(
    		Transformer<Context<Graph<V,E>,E>,Number> edgeLabelClosenessTransformer) {
        this.edgeLabelClosenessTransformer = edgeLabelClosenessTransformer;
    }

    /**
     * @see RenderContext#getEdgeLabelRenderer()
     */
    public EdgeLabelRenderer getEdgeLabelRenderer() {
        return edgeLabelRenderer;
    }

    /**
     * @see RenderContext#setEdgeLabelRenderer(EdgeLabelRenderer)
     */
    public void setEdgeLabelRenderer(EdgeLabelRenderer edgeLabelRenderer) {
        this.edgeLabelRenderer = edgeLabelRenderer;
    }

    /**
     * @see RenderContext#getEdgeFillPaintTransformer() ()
     */
    public Transformer<E,Paint> getEdgeFillPaintTransformer() {
        return edgeFillPaintTransformer;
    }

    /**
     * @see RenderContext#setEdgeDrawPaintTransformer(Transformer)
     */
    public void setEdgeDrawPaintTransformer(Transformer<E,Paint> edgeDrawPaintTransformer) {
        this.edgeDrawPaintTransformer = edgeDrawPaintTransformer;
    }

    /**
     * @see RenderContext#getEdgeDrawPaintTransformer()
     */
    public Transformer<E,Paint> getEdgeDrawPaintTransformer() {
        return edgeDrawPaintTransformer;
    }

    /**
     * @see RenderContext#setEdgeFillPaintTransformer(Transformer)
     */
    public void setEdgeFillPaintTransformer(Transformer<E,Paint> edgeFillPaintTransformer) {
        this.edgeFillPaintTransformer = edgeFillPaintTransformer;
    }

    /**
     * @see RenderContext#getEdgeShapeTransformer()
     */
    public Transformer<Context<Graph<V,E>,E>,Shape> getEdgeShapeTransformer() {
        return edgeShapeTransformer;
    }

    /**
     * @see RenderContext#setEdgeShapeTransformer(Transformer)
     */
    public void setEdgeShapeTransformer(Transformer<Context<Graph<V,E>,E>,Shape> edgeShapeTransformer) {
        this.edgeShapeTransformer = edgeShapeTransformer;
        if(edgeShapeTransformer instanceof EdgeShape.Orthogonal) {
        	((EdgeShape.IndexedRendering<V, E>)edgeShapeTransformer).setEdgeIndexFunction(this.incidentEdgeIndexFunction);
        } else 
        if(edgeShapeTransformer instanceof EdgeShape.IndexedRendering) {
            ((EdgeShape.IndexedRendering<V,E>)edgeShapeTransformer).setEdgeIndexFunction(this.parallelEdgeIndexFunction);
        }
    }

    /**
     * @see RenderContext#getEdgeLabelTransformer()
     */
    public Transformer<E,String> getEdgeLabelTransformer() {
        return edgeLabelTransformer;
    }

    /**
     * @see RenderContext#setEdgeLabelTransformer(Transformer)
     */
    public void setEdgeLabelTransformer(Transformer<E,String> edgeLabelTransformer) {
        this.edgeLabelTransformer = edgeLabelTransformer;
    }

    /**
     * @see RenderContext#getEdgeStrokeTransformer()
     */
    public Transformer<E,Stroke> getEdgeStrokeTransformer() {
        return edgeStrokeTransformer;
    }

    /**
     * @see RenderContext#setEdgeStrokeTransformer(Transformer)
     */
    public void setEdgeStrokeTransformer(Transformer<E,Stroke> edgeStrokeTransformer) {
        this.edgeStrokeTransformer = edgeStrokeTransformer;
    }

    /**
     * @see RenderContext#getEdgeStrokeTransformer()
     */
    public Transformer<E,Stroke> getEdgeArrowStrokeTransformer() {
        return edgeArrowStrokeTransformer;
    }

    /**
     * @see RenderContext#setEdgeStrokeTransformer(Transformer)
     */
    public void setEdgeArrowStrokeTransformer(Transformer<E,Stroke> edgeArrowStrokeTransformer) {
        this.edgeArrowStrokeTransformer = edgeArrowStrokeTransformer;
    }

    /**
     * @see RenderContext#getGraphicsContext()
     */
    public GraphicsDecorator getGraphicsContext() {
        return graphicsContext;
    }

    /**
     * @see RenderContext#setGraphicsContext(GraphicsDecorator)
     */
    public void setGraphicsContext(GraphicsDecorator graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * @see RenderContext#getLabelOffset()
     */
    public int getLabelOffset() {
        return labelOffset;
    }

    /**
     * @see RenderContext#setLabelOffset(int)
     */
    public void setLabelOffset(int labelOffset) {
        this.labelOffset = labelOffset;
    }

    /**
     * @see RenderContext#getParallelEdgeIndexFunction()
     */
    public EdgeIndexFunction<V, E> getParallelEdgeIndexFunction() {
        return parallelEdgeIndexFunction;
    }

    /**
     * @see RenderContext#setParallelEdgeIndexFunction(EdgeIndexFunction)
     */
    public void setParallelEdgeIndexFunction(
            EdgeIndexFunction<V, E> parallelEdgeIndexFunction) {
        this.parallelEdgeIndexFunction = parallelEdgeIndexFunction;
        // reset the edge shape transformer, as the parallel edge index function
        // is used by it
        this.setEdgeShapeTransformer(getEdgeShapeTransformer());
    }

    /**
     * @see RenderContext#getPickedEdgeState()
     */
    public PickedState<E> getPickedEdgeState() {
        return pickedEdgeState;
    }

    /**
     * @see RenderContext#setPickedEdgeState(PickedState)
     */
    public void setPickedEdgeState(PickedState<E> pickedEdgeState) {
        this.pickedEdgeState = pickedEdgeState;
    }

    /**
     * @see RenderContext#getPickedVertexState()
     */
    public PickedState<V> getPickedVertexState() {
        return pickedVertexState;
    }

    /**
     * @see RenderContext#setPickedVertexState(PickedState)
     */
    public void setPickedVertexState(PickedState<V> pickedVertexState) {
        this.pickedVertexState = pickedVertexState;
    }

    /**
     * @see RenderContext#getRendererPane()
     */
    public CellRendererPane getRendererPane() {
        return rendererPane;
    }

    /**
     * @see RenderContext#setRendererPane(CellRendererPane)
     */
    public void setRendererPane(CellRendererPane rendererPane) {
        this.rendererPane = rendererPane;
    }

    /**
     * @see RenderContext#getScreenDevice()
     */
    public JComponent getScreenDevice() {
        return screenDevice;
    }

    /**
     * @see RenderContext#setScreenDevice(JComponent)
     */
    public void setScreenDevice(JComponent screenDevice) {
        this.screenDevice = screenDevice;
        screenDevice.add(rendererPane);
    }

    /**
     * @see RenderContext#getVertexFontTransformer()
     */
    public Transformer<V,Font> getVertexFontTransformer() {
        return vertexFontTransformer;
    }

    /**
     * @see RenderContext#setVertexFontTransformer(Transformer)
     */
    public void setVertexFontTransformer(Transformer<V,Font> vertexFontTransformer) {
        this.vertexFontTransformer = vertexFontTransformer;
    }

    /**
     * @see RenderContext#getVertexIconTransformer()
     */
    public Transformer<V,Icon> getVertexIconTransformer() {
        return vertexIconTransformer;
    }

    /**
     * @see RenderContext#setVertexIconTransformer(Transformer)
     */
    public void setVertexIconTransformer(Transformer<V,Icon> vertexIconTransformer) {
        this.vertexIconTransformer = vertexIconTransformer;
    }

    /**
     * @see RenderContext#getVertexIncludePredicate()
     */
    public Predicate<Context<Graph<V,E>,V>> getVertexIncludePredicate() {
        return vertexIncludePredicate;
    }

    /**
     * @see RenderContext#setVertexIncludePredicate(Predicate)
     */
    public void setVertexIncludePredicate(Predicate<Context<Graph<V,E>,V>> vertexIncludePredicate) {
        this.vertexIncludePredicate = vertexIncludePredicate;
    }

    /**
     * @see RenderContext#getVertexLabelRenderer()
     */
    public VertexLabelRenderer getVertexLabelRenderer() {
        return vertexLabelRenderer;
    }

    /**
     * @see RenderContext#setVertexLabelRenderer(VertexLabelRenderer)
     */
    public void setVertexLabelRenderer(VertexLabelRenderer vertexLabelRenderer) {
        this.vertexLabelRenderer = vertexLabelRenderer;
    }

    /**
     * @see RenderContext#getVertexFillPaintTransformer()
     */
    public Transformer<V,Paint> getVertexFillPaintTransformer() {
        return vertexFillPaintTransformer;
    }

    /**
     * @see RenderContext#setVertexFillPaintTransformer(Transformer)
     */
    public void setVertexFillPaintTransformer(Transformer<V,Paint> vertexFillPaintTransformer) {
        this.vertexFillPaintTransformer = vertexFillPaintTransformer;
    }

    /**
     * @see RenderContext#getVertexDrawPaintTransformer()
     */
    public Transformer<V,Paint> getVertexDrawPaintTransformer() {
        return vertexDrawPaintTransformer;
    }

    /**
     * @see RenderContext#setVertexDrawPaintTransformer(Transformer)
     */
    public void setVertexDrawPaintTransformer(Transformer<V,Paint> vertexDrawPaintTransformer) {
        this.vertexDrawPaintTransformer = vertexDrawPaintTransformer;
    }

    /**
     * @see RenderContext#getVertexLabelTransformer()
     */
    public Transformer<V,String> getVertexLabelTransformer() {
        return vertexLabelTransformer;
    }

    /**
     * @see RenderContext#setVertexLabelTransformer(Transformer)
     */
    public void setVertexLabelTransformer(Transformer<V,String> vertexLabelTransformer) {
        this.vertexLabelTransformer = vertexLabelTransformer;
    }

	/**
	 * @return the pickSupport
	 */
	public GraphElementAccessor<V, E> getPickSupport() {
		return pickSupport;
	}

	/**
	 * @param pickSupport the pickSupport to set
	 */
	public void setPickSupport(GraphElementAccessor<V, E> pickSupport) {
		this.pickSupport = pickSupport;
	}
	
	/**
	 * @return the basicTransformer
	 */
	public MultiLayerTransformer getMultiLayerTransformer() {
		return multiLayerTransformer;
	}

	/**
	 * @param basicTransformer the basicTransformer to set
	 */
	public void setMultiLayerTransformer(MultiLayerTransformer basicTransformer) {
		this.multiLayerTransformer = basicTransformer;
	}

	/**
	 * @see RenderContext#getArrowDrawPaintTransformer()
	 */
	public Transformer<E, Paint> getArrowDrawPaintTransformer() {
		return arrowDrawPaintTransformer;
	}

	/**
	 * @see RenderContext#getArrowFillPaintTransformer()
	 */
	public Transformer<E, Paint> getArrowFillPaintTransformer() {
		return arrowFillPaintTransformer;
	}

	/**
	 * @see RenderContext#setArrowDrawPaintTransformer(Transformer)
	 */
	public void setArrowDrawPaintTransformer(Transformer<E, Paint> arrowDrawPaintTransformer) {
		this.arrowDrawPaintTransformer = arrowDrawPaintTransformer;
		
	}

	/**
	 * @see RenderContext#setArrowFillPaintTransformer(Transformer)
	 */
	public void setArrowFillPaintTransformer(Transformer<E, Paint> arrowFillPaintTransformer) {
		this.arrowFillPaintTransformer = arrowFillPaintTransformer;
		
	}
}


