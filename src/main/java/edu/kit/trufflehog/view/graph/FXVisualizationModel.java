package edu.kit.trufflehog.view.graph;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.visualization.VisualizationModel;

import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by root on 24.01.16.
 */
public class FXVisualizationModel implements VisualizationModel<INode, IConnection> {
    @Override
    public Relaxer getRelaxer() {
        return null;
    }

    @Override
    public void setGraphLayout(Layout<INode, IConnection> layout) {

    }

    @Override
    public void setGraphLayout(Layout<INode, IConnection> layout, Dimension dimension) {

    }

    @Override
    public Layout<INode, IConnection> getGraphLayout() {
        return null;
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
}
