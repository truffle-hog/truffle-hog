package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

/**
 * This Class inhabits several NetworkFrames that a Network session can be recorded on.
 * TODO implement
 */
public class NetworkTape implements INetworkTape {

    public NetworkFrame getCurrentReadingFrame() {
        // TODO implement
        return null;
    }

    public NetworkFrame getCurrentWritingFrame() {
        return null;
    }

    @Override
    public void setCurrentReadingFrame(int frame) {

    }

    @Override
    public void setCurrentWritingFrame(int frame) {

    }

    @Override
    public void write(INetwork liveNetwork) {

    }

    @Override
    public int frameCount() {
        return 0;
    }

    @Override
    public int getFrameRate() {
        return 0;
    }

    public static class NetworkFrame implements INetworkFrame {

        @Override
        public INetworkReadingPort getReadingPort() {
            return null;
        }

        @Override
        public INetworkWritingPort getWritingPort() {
            return null;
        }

        @Override
        public INetworkViewPort getViewPort() {
            return null;
        }
    }
}
