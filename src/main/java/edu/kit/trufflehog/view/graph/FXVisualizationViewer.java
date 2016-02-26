/*
* Copyright (c) 2003, the JUNG Project and the Regents of the University 
* of California
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
*/
package edu.kit.trufflehog.view.graph;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.ToolTipManager;

import edu.kit.trufflehog.view.graph.control.FXMouseListenerTranslator;
import edu.uci.ics.jung.visualization.*;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.MouseListenerTranslator;

/**
 * Adds mouse behaviors and tooltips to the graph visualization base class
 * 
 * @author Joshua O'Madadhain
 * @author Tom Nelson 
 * @author Danyel Fisher
 */
@SuppressWarnings("serial")
public class FXVisualizationViewer<V,E> extends FXVisualizationServer<V,E> {

	protected Transformer<V,String> vertexToolTipTransformer;
	protected Transformer<E,String> edgeToolTipTransformer;
	protected Transformer<MouseEvent,String> mouseEventToolTipTransformer;
	
    /**
     * provides MouseListener, MouseMotionListener, and MouseWheelListener
     * events to the graph
     */
    protected FXGraphMouse graphMouse;
    
    protected MouseListener requestFocusListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			requestFocusInWindow();
		}
    };


    /**
     * Create an instance with passed parameters.
     * 
     * @param layout		The Layout to apply, with its associated Graph
     */
	public FXVisualizationViewer(Layout<V,E> layout) {
	    this(new DefaultVisualizationModel<V,E>(layout));
	}
	
    /**
     * Create an instance with passed parameters.
     * 
     * @param layout		The Layout to apply, with its associated Graph
     * @param preferredSize the preferred size of this View
     */
	public FXVisualizationViewer(Layout<V,E> layout, Dimension preferredSize) {
	    this(new DefaultVisualizationModel<V,E>(layout, preferredSize), preferredSize);
	}
	
	/**
	 * Create an instance with passed parameters.
	 * 
	 * @param model
	 */
	public FXVisualizationViewer(VisualizationModel<V,E> model) {
	    this(model, new Dimension(600,600));
	}
	/**
	 * Create an instance with passed parameters.
	 * 
	 * @param model
	 * @param preferredSize initial preferred size of the view
	 */
	@SuppressWarnings("unchecked")
    public FXVisualizationViewer(VisualizationModel<V,E> model,
	        Dimension preferredSize) {
        super(model, preferredSize);
		setFocusable(true);
        addMouseListener(requestFocusListener);
	}
	
	/**
	 * a setter for the GraphMouse. This will remove any
	 * previous GraphMouse (including the one that
	 * is added in the initMouseClicker method.
	 * @param graphMouse new value
	 */
	public void setGraphMouse(FXGraphMouse graphMouse) {
	    this.graphMouse = graphMouse;
	    MouseListener[] ml = getMouseListeners();
	    for(int i=0; i<ml.length; i++) {
	        if(ml[i] instanceof VisualizationViewer.GraphMouse) {
	            removeMouseListener(ml[i]);
	        }
	    }
	    MouseMotionListener[] mml = getMouseMotionListeners();
	    for(int i=0; i<mml.length; i++) {
	        if(mml[i] instanceof VisualizationViewer.GraphMouse) {
	            removeMouseMotionListener(mml[i]);
	        }
	    }
	    MouseWheelListener[] mwl = getMouseWheelListeners();
	    for(int i=0; i<mwl.length; i++) {
	        if(mwl[i] instanceof VisualizationViewer.GraphMouse) {
	            removeMouseWheelListener(mwl[i]);
	        }
	    }
	    addMouseListener(graphMouse);
	    addMouseMotionListener(graphMouse);
	    addMouseWheelListener(graphMouse);
	}
	
	/**
	 * @return the current <code>GraphMouse</code>
	 */
	public FXGraphMouse getGraphMouse() {
	    return graphMouse;
	}

	/**
	 * This is the interface for adding a mouse listener. The GEL
	 * will be called back with mouse clicks on vertices.
	 * @param gel
	 */
	public void addGraphMouseListener( GraphMouseListener<V> gel ) {
		addMouseListener( new FXMouseListenerTranslator<V,E>( gel, this ));
	}
	
	/** 
	 * Override to request focus on mouse enter, if a key listener is added
	 * @see java.awt.Component#addKeyListener(KeyListener)
	 */
	@Override
	public synchronized void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
//		setFocusable(true);
//		addMouseListener(requestFocusListener);
	}
	
	/**
	 * @param edgeToolTipTransformer the edgeToolTipTransformer to set
	 */
	public void setEdgeToolTipTransformer(
			Transformer<E, String> edgeToolTipTransformer) {
		this.edgeToolTipTransformer = edgeToolTipTransformer;
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * @param mouseEventToolTipTransformer the mouseEventToolTipTransformer to set
	 */
	public void setMouseEventToolTipTransformer(
			Transformer<MouseEvent, String> mouseEventToolTipTransformer) {
		this.mouseEventToolTipTransformer = mouseEventToolTipTransformer;
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * @param vertexToolTipTransformer the vertexToolTipTransformer to set
	 */
	public void setVertexToolTipTransformer(
			Transformer<V, String> vertexToolTipTransformer) {
		this.vertexToolTipTransformer = vertexToolTipTransformer;
		ToolTipManager.sharedInstance().registerComponent(this);
	}

    /**
     * called by the superclass to display tooltips
     */
    public String getToolTipText(MouseEvent event) {
        Layout<V,E> layout = getGraphLayout();
        Point2D p = null;
        if(vertexToolTipTransformer != null) {
            p = event.getPoint();
            	//renderContext.getBasicTransformer().inverseViewTransform(event.getPoint());
            V vertex = getPickSupport().getVertex(layout, p.getX(), p.getY());
            if(vertex != null) {
            	return vertexToolTipTransformer.transform(vertex);
            }
        }
        if(edgeToolTipTransformer != null) {
        	if(p == null) p = renderContext.getMultiLayerTransformer().inverseTransform(Layer.VIEW, event.getPoint());
            E edge = getPickSupport().getEdge(layout, p.getX(), p.getY());
            if(edge != null) {
            	return edgeToolTipTransformer.transform(edge);
            }
        }
        if(mouseEventToolTipTransformer != null) {
        	return mouseEventToolTipTransformer.transform(event);
        }
        return super.getToolTipText(event);
    }

     /**
     * a convenience type to represent a class that
     * processes all types of mouse events for the graph
     */
    public interface FXGraphMouse extends MouseListener, MouseMotionListener, MouseWheelListener {}
    
}
