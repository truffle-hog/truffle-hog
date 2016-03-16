package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import edu.uci.ics.jung.graph.util.Graphs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * TODO implmeent
 */
public class NetworkDevice implements INetworkDevice {

    private static final Logger logger = LogManager.getLogger(NetworkDevice.class);
    
    private Timeline frameWriter;

    private boolean isRecording = false;
    private boolean isLive = true;

    private final ICopyCreator copyCreator = new TapeCopyCreator();

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

        //tape.writeFrame(network);

        frameWriter = new Timeline(new KeyFrame(Duration.millis(frameLength), event -> {

            final NetworkCopy copiedNetwork = network.createDeepCopy(copyCreator);

            tape.writeFrame(copiedNetwork);

//            logger.debug(tape.getFrame(tape.getCurrentWritingFrame() - 1));

            playbackFrameCountProperty.set(tape.getFrameCount() - 1);
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
    public void goReplay(final INetworkTape tape, final INetworkViewPortSwitch viewPortSwitch) {

        if (tape.getFrameCount() == 0) {

            logger.warn("Framecount of the given tape to be played is zero");
            return;
        }

        playbackFrameCountProperty.set(tape.getFrameCount() - 1);

        final Graph<INode, IConnection> replayViewGraph = Graphs.synchronizedDirectedGraph(new DirectedSparseGraph<>());
        final INetwork replayNetwork = new ReplayNetwork(new ObservableUpdatableGraph<>(replayViewGraph, new ReplayUpdater()));

        viewPortSwitch.setActiveViewPort(replayNetwork.getViewPort());

        isLive = false;
       // this.playTape = tape;
        tape.setCurrentReadingFrame(0);

        // TODO Take the real framel ength!!! instead of 100 - --- the   -1    PROBLEM
        final long frameLength = 1000 / tape.getFrameRate();

        final Timeline frameReader = new Timeline(new KeyFrame(Duration.millis(frameLength), new FrameHandler(tape, replayNetwork)));
        // frameReader.setCycleCount(playTape.frameCount() - 1);Timeline.INDEFINITE
        frameReader.setCycleCount(Timeline.INDEFINITE);
        frameReader.play();
    }

    @Override
    public void stop() {

        if (frameWriter == null) {
            logger.warn("Currently there is no ongoing recording");
            return;
        }
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

}
