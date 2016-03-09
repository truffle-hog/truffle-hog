package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.IntegerProperty;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * This interface provides the functionality that a network tape where a recording of a network is captured on has.
 */
public interface INetworkTape {

    IntegerProperty getCurrentReadingFrameProperty();

    /**
     * @return The Frame the reading end is currently pointing to
     */
    int getCurrentReadingFrame();

    IntegerProperty getCurrentWritingFrameProperty();

    /**
     * @return The Frame the writing end is currently pointing to
     */
    int getCurrentWritingFrame();

    /**
     * Sets the current reading frame pointer to the given frame number.
     * @param frame the frame number to be set for the reading pointer
     */
    void setCurrentReadingFrame(int frame);

    /**
     * Sets the current writing frame pointer to the given frame number.
     * @param frame the frame number to be set for the writing pointer
     */
    void setCurrentWritingFrame(int frame);

    /**
     * Writes the given live Network onto the current writing frame.
     * @param network the live network to be written on the current writing frame
     */
    void writeFrame(NetworkCopy network);

    /**
     * @return The number of frames on this network tape
     */
    int getFrameCount();

    IntegerProperty getFrameCountProperty();

    /**
     * @return The Framerate this network tape was recorded with
     */
    int getFrameRate();

    INetworkFrame getFrame(int index);

    /**
     * This interface provides the functionality a single frame in a networktape possesses.
     */
    interface INetworkFrame {

        int getMaxConnectionSize();

        int getMaxThroughput();

        long getViewTime();

        //int getVertexCount();

        int getEdgeCount();

        //Collection<INode> getVertices();

        Collection<IConnection> getEdges();

        Point2D transform(INode node);
    }
}
