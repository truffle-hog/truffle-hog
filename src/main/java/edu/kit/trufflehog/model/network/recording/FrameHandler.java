package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class FrameHandler implements EventHandler<ActionEvent> {

    private final INetworkTape playTape;
    private final INetworkDevice graphDevice;
    private final INetwork replayLayout;

    public FrameHandler(INetworkTape playTape, INetworkDevice currentFrame, INetwork replayLayout) {

        this.playTape = playTape;
        this.graphDevice = currentFrame;
        this.replayLayout = replayLayout;
    }

    @Override
    public void handle(ActionEvent event) {

        playTape.setCurrentReadingFrame(graphDevice.getPlaybackFrame());
        final INetworkTape.INetworkFrame playFrame = playTape.getCurrentReadingFrame();

        replayLayout.getViewPort().setMaxConnectionSize(playFrame.getViewPort().getMaxConnectionSize());
        replayLayout.getViewPort().setMaxThroughput(playFrame.getViewPort().getMaxThroughput());
        replayLayout.getViewPort().setViewTime(playFrame.getViewPort().getViewTime());

        if (playFrame.getViewPort().getGraph().getVertexCount() < replayLayout.getViewPort().getGraph().getVertexCount() ||
                playFrame.getViewPort().getGraph().getEdgeCount() < replayLayout.getViewPort().getGraph().getVertexCount()) {

            replayLayout.getViewPort().graphIntersection(playFrame.getViewPort().getGraph());
        }


        playFrame.getViewPort().getGraph().getVertices().stream().forEach(node -> {
            replayLayout.getWritingPort().writeNode(node);
            if (graphDevice.isMovableDuringPlayback()) {
                replayLayout.getViewPort().setLocation(node, playFrame.getViewPort().transform(node));
            }
        });

        playFrame.getViewPort().getGraph().getEdges().stream().forEach(edge -> {
            replayLayout.getWritingPort().writeConnection(edge);
        });

    }
}
