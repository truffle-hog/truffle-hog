package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;

/**
 * Created by jan on 19.02.16.
 */
public interface INetworkTape {

    INetworkFrame getCurrentReadingFrame();

    INetworkFrame getCurrentWritingFrame();

    void setCurrentReadingFrame(int frame);

    void setCurrentWritingFrame(int frame);

    void write(INetwork liveNetwork);

    int frameCount();

    int getFrameRate();

    interface INetworkFrame extends INetwork {

    }
}
