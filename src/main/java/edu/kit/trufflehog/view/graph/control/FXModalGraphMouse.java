/*
 * Copyright (c) 2005, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Created on Aug 26, 2005
 */

package edu.kit.trufflehog.view.graph.control;

import edu.kit.trufflehog.view.graph.FXVisualizationViewer;

import java.awt.event.ItemListener;


/**
 * Interface for a GraphMouse that supports modality.
 * 
 * @author Tom Nelson 
 *
 */
public interface FXModalGraphMouse extends FXVisualizationViewer.FXGraphMouse {
    
    void setMode(Mode mode);
    
    /**
     * @return Returns the modeListener.
     */
    ItemListener getModeListener();
    
    /**
     */
    enum Mode { TRANSFORMING, PICKING, ANNOTATING, EDITING }
    
}