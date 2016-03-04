/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
/*
 * Created on Feb 17, 2004
 */
package edu.kit.trufflehog.view.graph.control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.kit.trufflehog.view.graph.FXVisualizationViewer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

/**
 * This class translates mouse clicks into vertex clicks
 * 
 * @author danyelf
 */
public class FXMouseListenerTranslator<V, E> extends MouseAdapter {

	private FXVisualizationViewer<V,E> vv;
	private GraphMouseListener<V> gel;

	/**
	 * @param gel
	 * @param vv
	 */
	public FXMouseListenerTranslator(GraphMouseListener<V> gel, FXVisualizationViewer<V,E> vv) {
		this.gel = gel;
		this.vv = vv;
	}
	
	/**
	 * Transform the point to the coordinate system in the
	 * VisualizationViewer, then use either PickSuuport
	 * (if available) or Layout to find a Vertex
	 * @param point
	 * @return
	 */
	private V getVertex(Point2D point) {
	    // adjust for scale and offset in the VisualizationViewer
	    Point2D p = point;
	    	//vv.getRenderContext().getBasicTransformer().inverseViewTransform(point);
	    GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        Layout<V,E> layout = vv.getGraphLayout();
	    V v = null;
	    if(pickSupport != null) {
	        v = pickSupport.getVertex(layout, p.getX(), p.getY());
	    } 
	    return v;
	}
	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	    V v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphClicked(v, e );
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		V v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphPressed(v, e );
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		V v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphReleased(v, e );
		}
	}
}
