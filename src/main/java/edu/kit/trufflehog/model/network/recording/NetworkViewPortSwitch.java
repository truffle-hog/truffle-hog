package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * TODO Implement
 */
public class NetworkViewPortSwitch implements INetworkViewPortSwitch {

    public NetworkViewPortSwitch(INetworkViewPort viewPort) {
    }

    @Override
    public void setActiveViewPort(INetworkViewPort viewPort) {

    }

    @Override
    public INetworkViewPort getActiveViewPort() {
        return null;
    }

    @Override
    public int getMaxConnectionSize() {
        return 0;
    }

    @Override
    public void setMaxConnectionSize(int size) {

    }

    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return null;
    }

    @Override
    public int getMaxThroughput() {
        return 0;
    }

    @Override
    public void setMaxThroughput(int size) {

    }

    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return null;
    }

    @Override
    public long getViewTime() {
        return 0;
    }

    @Override
    public void setViewTime(long time) {

    }

    @Override
    public LongProperty getViewTimeProperty() {
        return null;
    }

    @Override
    public void refreshLayout() {

    }

    @Override
    public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory) {

    }

    @Override
    public void graphIntersection(Graph<INode, IConnection> graph) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void setInitializer(Transformer<INode, Point2D> initializer) {

    }

    @Override
    public void setGraph(Graph<INode, IConnection> graph) {

    }

    @Override
    public Graph<INode, IConnection> getGraph() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setSize(Dimension d) {

    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public void lock(INode iNode, boolean state) {

    }

    @Override
    public boolean isLocked(INode iNode) {
        return false;
    }

    @Override
    public void setLocation(INode iNode, Point2D location) {

    }

    @Override
    public Point2D transform(INode iNode) {
        return null;
    }
}
