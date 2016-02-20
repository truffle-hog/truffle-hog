package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import edu.kit.trufflehog.model.network.*;
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

    private INetwork replayNetwork;
    private INetwork liveNetwork;

    private INetworkTape networkTape;

    private boolean userLocation = false;
    private IntegerProperty playbackFrameProperty = new SimpleIntegerProperty(0);
    private IntegerProperty playbackFrameCountProperty = new SimpleIntegerProperty(0);

    private final INetworkWritingPortSwitch writingPortSwitch;
    private final INetworkReadingPortSwitch readingPortSwitch;
    private final INetworkViewPortSwitch viewPortSwitch;

    public NetworkDevice(INetwork liveNetwork) {

        writingPortSwitch = new NetworkWritingPortSwitch(liveNetwork.getWritingPort());
        readingPortSwitch = new NetworkReadingPortSwitch(liveNetwork.getReadingPort());
        viewPortSwitch = new NetworkViewPortSwitch(liveNetwork.getViewPort());

        this.liveNetwork = liveNetwork;
    }

    @Override
    public boolean record(int framerate) {

        if (isRecording) {
            return false;
        }

        isRecording = true;
        long frameLength = 1000 / framerate;

        playTape.write(this.getLiveNetwork());

        frameWriter = new Timeline(new KeyFrame(Duration.millis(frameLength), event -> {
            playTape.write(this.getLiveNetwork());
            playbackFrameCountProperty.set(playTape.frameCount() - 1);
        }));
        frameWriter.setCycleCount(Timeline.INDEFINITE);
        frameWriter.play();

        return true;
    }

    @Override
    public void goLive() {

        viewPortSwitch.setActiveViewPort(getLiveNetwork().getViewPort());
    }

    @Override
    public void play(INetworkTape graphTape, INetworkViewPort viewPort) {

        playbackFrameCountProperty.set(graphTape.frameCount() - 1);

        final Graph<INode, IConnection> replayViewGraph = new ConcurrentDirectedSparseGraph<>();
        replayNetwork = new ReplayNetwork(replayViewGraph);

        viewPortSwitch.setActiveViewPort(viewPort);

        isLive = false;
        this.playTape = graphTape;
        playTape.setCurrentReadingFrame(0);

        // TODO Take the real framel ength!!! instead of 100 - --- the   -1    PROBLEM
        final long frameLength = 1000 / graphTape.getFrameRate();


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
    public INetwork getLiveNetwork() {
        return liveNetwork;
    }

    @Override
    public INetwork getReplayNetwork() {
        return replayNetwork;
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
