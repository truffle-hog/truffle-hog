package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 * TODO implmeent
 */
public class NetworkDevice implements INetworkDevice {

    private INetworkTape playTape;
    private Timeline frameWriter;

    private boolean isRecording = false;
    private boolean isLive = true;

    private INetworkTape networkTape;

    private boolean userLocation = false;
    private IntegerProperty playbackFrameProperty = new SimpleIntegerProperty(0);
    private IntegerProperty playbackFrameCountProperty = new SimpleIntegerProperty(0);

    @Override
    public boolean record(final INetwork network, final INetworkTape tape, final int framerate) {

        // TODO check if there is an ongoin recording on the given tape
/*        if (tape.isRecording) {
            return false;
        }*/

        // TODO set the isRecording value on the tape to true
        isRecording = true;
        long frameLength = 1000 / framerate;

        tape.write(network);

        frameWriter = new Timeline(new KeyFrame(Duration.millis(frameLength), event -> {
            tape.write(network);
            playbackFrameCountProperty.set(tape.frameCount() - 1);
        }));
        frameWriter.setCycleCount(Timeline.INDEFINITE);
        frameWriter.play();

        return true;
    }

    @Override
    public void goLive(INetwork network, INetworkViewPortSwitch port) {

        port.setActiveViewPort(network.getViewPort());
    }

    @Override
    public void play(INetworkTape tape, INetworkViewPortSwitch viewPortSwitch) {

        playbackFrameCountProperty.set(tape.frameCount() - 1);

        final Graph<INode, IConnection> replayViewGraph = new ConcurrentDirectedSparseGraph<>();
        final INetwork replayNetwork = new ReplayNetwork(replayViewGraph);

        viewPortSwitch.setActiveViewPort(replayNetwork.getViewPort());

        isLive = false;
        this.playTape = tape;
        playTape.setCurrentReadingFrame(0);

        // TODO Take the real framel ength!!! instead of 100 - --- the   -1    PROBLEM
        final long frameLength = 1000 / tape.getFrameRate();


        final Timeline frameReader = new Timeline(new KeyFrame(Duration.millis(42), new FrameHandler(playTape, this, replayNetwork)));
        // frameReader.setCycleCount(playTape.frameCount() - 1);Timeline.INDEFINITE
        frameReader.setCycleCount(Timeline.INDEFINITE);
        frameReader.play();
    }

    @Override
    public void stop() {

        frameWriter.stop();
    }

    @Override
    public IntegerProperty getPlaybackFrameProperty() {
        return playbackFrameProperty;
    }

    @Override
    public int getPlaybackFrame() {
        return playbackFrameProperty.get();
    }

    @Override
    public IntegerProperty getPlaybackFrameCountProperty() {
        return playbackFrameCountProperty;
    }

    @Override
    public int getPlaybackFrameCount() {
        return playbackFrameCountProperty.get();
    }

    @Override
    public BooleanProperty isRecordingProperty() {
        return null;
    }


    @Override
    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public BooleanProperty isLiveProperty() {
        return null;
    }

    @Override
    public boolean isLive() {
        return false;
    }

    @Override
    public boolean isMovableDuringPlayback() {
        return false;
    }

    @Override
    public INetworkTape removeNetworkTape() {

        final INetworkTape temp = playTape;
        playTape = new NetworkTape();

        return temp;
    }

    @Override
    public INetworkTape getNetworkTape() {
        return playTape;
    }
}
