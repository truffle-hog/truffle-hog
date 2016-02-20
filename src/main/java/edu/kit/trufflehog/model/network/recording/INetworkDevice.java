package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

/**
 * This class handles all the recording functionality that can be done on a live network.
 *
 * This includes recording the current live network, stopping the recording,
 * removing and showing the current network tape, playing an existing NetworkTape.
 */
public interface INetworkDevice {

    /**
     * Plays the given given network tape for playback on the given viewport.
     *
     * @param tape the tape to be played
     */
    void play(INetworkTape tape, INetworkViewPort viewPort);

    /**
     * Stops the recording of the network on the inserted tape.
     */
    void stop();

    /**
     * Records the current live network with the given framerate. Does noething and returns false if
     * it is already recording.
     *
     * @param frameRate the maximum framerate the live network will be recorded with.
     * @return {@code true} if the is no record currently active, {@code false} otherwise
     */
    boolean record(int frameRate);

    /**
     * Tell the device to track the live network again.
     */
    void goLive();

    /**
     * Returns the live network that this device is currently working on
     * @return the live network this device is connected to
     */
    INetwork getLiveNetwork();

    /**
     * Returns the replay network this device is currently recording on
     * @return the replay network this device is recording on
     */
    INetwork getReplayNetwork();

    /**
     * Retrieves and removes the network tape currently in the recorder
     * @return The network Tape that was in the recorder
     */
    INetworkTape removeNetworkTape();

    /**
     * Returns a copy of the network tape currently in the recorder.
     * @return The network Tape that is in the recorder
     */
    INetworkTape getNetworkTape();



    /**
     * The current frame number the playback is showing.
     * @return
     */
    IntegerProperty getPlaybackFrameProperty();

    /**
     * The current frame number the playback is showing.
     * @return
     */
    int getPlaybackFrame();

    /**
     * Returns the property that inhabits the total frames that are currently on the playback tape.
     * @return
     */
    IntegerProperty getPlaybackFrameCountProperty();

    /**
     * The number of frames on the playback tape.
     * @return
     */
    int getPlaybackFrameCount();

    BooleanProperty isRecordingProperty();

    boolean isRecording();

    BooleanProperty isLiveProperty();

    boolean isLive();

    BooleanProperty isPlayBackProperty();

    boolean isPlayBack();

    boolean isPositionPlayback();
}
