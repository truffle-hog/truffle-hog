package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import org.apache.commons.collections15.Transformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * TODO Implement
 */
public class NetworkViewPortSwitch implements INetworkViewPortSwitch {

    private static final Logger logger = LogManager.getLogger(NetworkViewPortSwitch.class);
    
    //private final INetworkDevice attachedDevice;
    private INetworkViewPort activeViewport;



    public NetworkViewPortSwitch(INetworkViewPort viewPort) {

        activeViewport = viewPort;
    }


    @Override
    public void setActiveViewPort(INetworkViewPort viewPort) {
        activeViewport = viewPort;
    }

    @Override
    public INetworkViewPort getActiveViewPort() {
        return activeViewport;
    }

    @Override
    public int getMaxConnectionSize() {
        return getActiveViewPort().getMaxConnectionSize();
    }

    @Override
    public void setMaxConnectionSize(int size) {
        getActiveViewPort().setMaxConnectionSize(size);
    }

    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return getActiveViewPort().getMaxConnectionSizeProperty();
    }

    @Override
    public int getMaxThroughput() {
        return getActiveViewPort().getMaxThroughput();
    }

    @Override
    public void setMaxThroughput(int size) {
        getActiveViewPort().setMaxThroughput(size);
    }

    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return getActiveViewPort().getMaxThroughputProperty();
    }

    @Override
    public long getViewTime() {
        return getActiveViewPort().getViewTime();
    }

    @Override
    public void setViewTime(long time) {
        getActiveViewPort().setViewTime(time);
    }

    @Override
    public LongProperty getViewTimeProperty() {
        return getActiveViewPort().getViewTimeProperty();
    }

    @Override
    public void refreshLayout() {
        getActiveViewPort().refreshLayout();
    }

    @Override
    public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory) {
        getActiveViewPort().setLayoutFactory(layoutFactory);
    }

    @Override
    public void graphIntersection(Graph<INode, IConnection> graph) {
        getActiveViewPort().graphIntersection(graph);
    }

    @Override
    public void graphIntersection(Collection<INode> vertices, Collection<IConnection> edges) {
        getActiveViewPort().graphIntersection(vertices, edges);
    }

    @Override
    public void initialize() {
        getActiveViewPort().initialize();
    }

    @Override
    public void setInitializer(Transformer<INode, Point2D> initializer) {
        getActiveViewPort().setInitializer(initializer);
    }

    @Override
    public void setGraph(Graph<INode, IConnection> graph) {
        getActiveViewPort().setGraph(graph);
    }

    @Override
    public Graph<INode, IConnection> getGraph() {
        
        //logger.debug(getActiveViewPort().getGraph());
        return getActiveViewPort().getGraph();
    }

    @Override
    public void reset() {
        getActiveViewPort().reset();
    }

    @Override
    public void setSize(Dimension d) {
        getActiveViewPort().setSize(d);
    }

    @Override
    public Dimension getSize() {
        return getActiveViewPort().getSize();
    }

    @Override
    public void lock(INode iNode, boolean state) {
        getActiveViewPort().lock(iNode, state);
    }

    @Override
    public boolean isLocked(INode iNode) {
        return getActiveViewPort().isLocked(iNode);
    }

    @Override
    public void setLocation(INode iNode, Point2D location) {
        getActiveViewPort().setLocation(iNode, location);
    }

    @Override
    public Point2D transform(INode iNode) {
        return getActiveViewPort().transform(iNode);
    }

    @Override
    public NetworkViewCopy createDeepCopy(ICopyCreator copyCreator) {
        return getActiveViewPort().createDeepCopy(copyCreator);
    }

    @Override
    public boolean isMutable() {
        return true;
    }
}
