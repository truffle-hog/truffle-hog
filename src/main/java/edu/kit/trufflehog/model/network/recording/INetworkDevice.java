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
    void play(INetworkTape tape, INetworkViewPortSwitch viewPort);

    /**
     * Stops the recording of the network on the inserted tape.
     */
    void stop();

    /**
     * Records the current live network with the given framerate. Does noething and returns false if
     * it is already recording.
     *
     * @param network the network to be recorded
     * @param tape the tape the given network will be recorded on
     * @param framerate the maximum framerate the live network will be recorded with.
     *
     * @return {@code true} if the is no record currently active on the given recording port,
     *          {@code false} otherwise
     */
    boolean record(final INetworkViewPort network, final INetworkTape tape, final int framerate);


    /**
     * Tell the device to show the given network on the given viewportswitch
     * @param network the network to be shown
     * @param viewPortSwitch the view port switch for the network to be displayed on
     */
    void goLive(INetwork network, INetworkViewPortSwitch viewPortSwitch);


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
     * Returns the property that inhabits the total framecount that are currently on the playback tape.
     * @return
     */
    IntegerProperty getPlaybackFrameCountProperty();

    /**
     * The number of frames on the playback tape.
     * @return
     */
    int getPlaybackFrameCount();

    /**
     * @return Whether or not the device is recording
     */
    BooleanProperty isRecordingProperty();

    /**
     * @return Whether or not the device is recording
     */
    boolean isRecording();

    /**
     * @return Whether or not the device is showing live
     */
    BooleanProperty isLiveProperty();

    /**
     * @return Whether or not the device is showing live
     */
    boolean isLive();

    /**
     * @return Whether or not the user is able to move the Nodes during playback // TODO Maybe a better method name
     */
    boolean isMovableDuringPlayback();

}
