/*
 * Copyright (c) 2005, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Created on Aug 23, 2005
 */
package edu.kit.trufflehog.view.graph.renderers;


import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class FXVertexLabelRenderer extends DefaultVertexLabelRenderer
{
	protected Color unpickedVertexLabelColor = Color.BLACK;

	public FXVertexLabelRenderer(Color unpickedVertexLabelColor, Color pickedVertexLabelColor)
	{
		super(pickedVertexLabelColor);
		this.unpickedVertexLabelColor = unpickedVertexLabelColor;
	}

	public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font, boolean isSelected, V vertex)
	{
		super.setForeground(unpickedVertexLabelColor);
		if (isSelected) setForeground(pickedVertexLabelColor);
		super.setBackground(vv.getBackground());
		if (font != null)
		{
			setFont(font);
		}
		else
		{
			setFont(vv.getFont());
		}
		setIcon(null);
		setBorder(noFocusBorder);
		setValue(value);
		return this;
	}
}
