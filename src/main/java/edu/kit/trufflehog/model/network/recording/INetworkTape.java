package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkViewPort;

/**
 * This interface provides the functionality that a network tape where a recording of a network is captured on has.
 */
public interface INetworkTape {

    /**
     * @return The Frame the reading end is currently pointing to
     */
    INetworkFrame getCurrentReadingFrame();

    /**
     * @return The Frame the writing end is currently pointing to
     */
    INetworkFrame getCurrentWritingFrame();

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
     * @param networkViewPort the live network to be written on the current writing frame
     */
  //  void write(INetwork liveNetwork);

    void write(INetworkViewPort networkViewPort);

    /**
     * @return The number of frames on this network tape
     */
    int frameCount();

    /**
     * @return The Framerate this network tape was recorded with
     */
    int getFrameRate();

    /**
     * This interface provides the functionality a single frame in a networktape possesses.
     */
    interface INetworkFrame extends INetworkViewPort {

    }
}
