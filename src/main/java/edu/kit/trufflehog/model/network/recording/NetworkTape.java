package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.apache.commons.collections15.Transformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This Class inhabits several NetworkFrames that a Network session can be recorded on.
 * TODO implement
 */
public class NetworkTape implements INetworkTape {

    private static final Logger logger = LogManager.getLogger(NetworkTape.class);
    
    private final int frameRate;


    private final List<INetworkFrame> frames = new ArrayList<>();

    private final IntegerProperty currentReadingFrameProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty currentWritingFrameProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty frameCountProperty = new SimpleIntegerProperty(0);



    public NetworkTape(int frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    public void setCurrentReadingFrame(int frame) {

        currentReadingFrameProperty.set(frame);
    }

    @Override
    public IntegerProperty getCurrentReadingFrameProperty() {
        return currentReadingFrameProperty;
    }

    @Override
    public int getCurrentReadingFrame() {
        return currentReadingFrameProperty.get();
    }

    @Override
    public IntegerProperty getCurrentWritingFrameProperty() {
        return currentWritingFrameProperty;
    }

    @Override
    public int getCurrentWritingFrame() {
        return currentWritingFrameProperty.get();
    }

    @Override
    public void setCurrentWritingFrame(int frame) {

        currentWritingFrameProperty.set(frame);
    }

    @Override
    public void write(INetworkViewPort networkViewPort) {

        frames.add(new NetworkFrame(networkViewPort));
        currentWritingFrameProperty.set(currentWritingFrameProperty.get() + 1);
        frameCountProperty.set(frames.size() - 1);
    }

    @Override
    public int getFrameCount() {
        return frameCountProperty.get();
    }

    @Override
    public IntegerProperty getFrameCountProperty() {
        return frameCountProperty;
    }

    @Override
    public int getFrameRate() {
        return frameRate;
    }

    @Override
    public INetworkFrame getFrame(int index) {
        return frames.get(index);
    }

    @Override
    public String toString() {

        return "---\n" + frames.stream().map(Object::toString).collect(Collectors.joining("\n---\n")) + "\n---";
    }

    public static class NetworkFrame implements INetworkFrame {

        private final long time;
        private final Graph<INode, IConnection> frame;


        private final Map<INode, Point2D> nodeMap = new ConcurrentHashMap<>();

        private final Map<INode, INode> nodes = new ConcurrentHashMap<>();

        private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);

        private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

        private final LongProperty viewTimeProperty = new SimpleLongProperty(0);
        private Dimension dimension;

        private NetworkFrame(INetworkViewPort liveNetworkViewPort) {

            time = Instant.now().toEpochMilli();
            frame = new ConcurrentDirectedSparseGraph<>();

            logger.debug(liveNetworkViewPort.getGraph().toString());

            liveNetworkViewPort.getGraph().getVertices().stream().forEach(node -> {

                final INode copyNode = node.createDeepCopy();
                final Point2D transform = liveNetworkViewPort.transform(node);
                nodeMap.put(copyNode, new Point2D.Double(transform.getX(), transform.getY()));
                nodes.put(copyNode, copyNode);
                //connectionMap.put(copyNode, new HashSet<>());
                frame.addVertex(copyNode);
            });

            liveNetworkViewPort.getGraph().getEdges().stream().forEach(edge -> {

                //final IConnection connectionCopy = new Connection(edge);
             //  final Pair<INode> endpoints = liveNetworkViewPort.getGraph().getEndpoints(edge);

               // final INode source = node.get(endpoints.getFirst());
              //  final INode dest = node.get(endpoints.getSecond());

/*                long result = 17;
                result = 31 * result + source.getID();
                result = 31 * result + dest.getID();*/
                final IConnection connectionCopy = edge.createDeepCopy();
                frame.addEdge(connectionCopy, connectionCopy.getSrc(), connectionCopy.getDest());
            });

            logger.debug(frame.toString());

/*        staticReplayLayout = new StaticLayout<>(this, TransformerUtils.mapTransformer(nodeMap));*/
            setMaxThroughput(liveNetworkViewPort.getMaxThroughput());
            setMaxConnectionSize(liveNetworkViewPort.getMaxConnectionSize());
            setSize(liveNetworkViewPort.getSize());

           // staticReplayLayout = new ConcurrentStaticLayout<>(frame);
        }

        @Override
        public int getMaxConnectionSize() {
            return maxConnectionSizeProperty.get();
        }

        @Override
        public void setMaxConnectionSize(int size) {
            maxConnectionSizeProperty.set(size);
        }

        @Override
        public IntegerProperty getMaxConnectionSizeProperty() {
            return maxConnectionSizeProperty;
        }

        @Override
        public int getMaxThroughput() {
            return maxThroughputProperty.get();
        }

        @Override
        public void setMaxThroughput(int size) {
            maxThroughputProperty.set(size);
        }

        @Override
        public IntegerProperty getMaxThroughputProperty() {
            return maxThroughputProperty;
        }

        @Override
        public long getViewTime() {
            return viewTimeProperty.get();
        }

        @Override
        public void setViewTime(long time) {
            viewTimeProperty.set(time);
        }

        @Override
        public LongProperty getViewTimeProperty() {
            return viewTimeProperty;
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
            return frame;
        }

        @Override
        public void reset() {

        }

        @Override
        public void setSize(Dimension d) {

            dimension = d;
        }

        @Override
        public Dimension getSize() {
            return dimension;
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

            nodeMap.put(iNode, location);
        }

        @Override
        public Point2D transform(INode iNode) {
            return nodeMap.get(iNode);
        }

        @Override
        public String toString() {
            return frame.toString() + "\n"
                    + "max connection: " + maxConnectionSizeProperty.get() + "\n"
                    + "max throughput: " + maxThroughputProperty.get();
        }
    }
}
