package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public void writeFrame(NetworkCopy networkCopy) {

//        logger.debug(networkCopy.toString());

        frames.add(new NetworkFrame(networkCopy));
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

        /*private final Map<INode, Point2D> nodeMap = new ConcurrentHashMap<>();

        private final Map<INode, INode> nodes = new ConcurrentHashMap<>();

        private final Collection<IConnection> connections = new HashSet<>();

        private final int maxThroughput;

        private final int maxConnectionSize;

        private final long viewTime;
        private final Dimension dimension;*/

        private final NetworkCopy copy;

        private NetworkFrame(NetworkCopy copy) {

            this.copy = copy;
/*            maxThroughput = liveNetworkViewPort.getMaxThroughput();
            maxConnectionSize = liveNetworkViewPort.getMaxConnectionSize();
            dimension = liveNetworkViewPort.getSize();
            viewTime = liveNetworkViewPort.getViewTime();*/
        }

        @Override
        public int getMaxConnectionSize() {
            return copy.getMaxConnectionSize();
        }

        @Override
        public int getMaxThroughput() {
            return copy.getMaxThroughput();
        }

        @Override
        public long getViewTime() {
            return copy.getViewTime();
        }

        @Override
        public int getEdgeCount() {
            return copy.getConnections().size();
        }

        @Override
        public Collection<IConnection> getEdges() {
            return copy.getConnections();
        }

        @Override
        public Point2D transform(INode iNode) {
            return copy.transform(iNode.getAddress());
        }

        @Override
        public String toString() {
            return copy.toString();
        }

    }
}
