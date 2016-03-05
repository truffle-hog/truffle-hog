package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FrameHandler implements EventHandler<ActionEvent> {

    private static final Logger logger = LogManager.getLogger(FrameHandler.class);

    private final INetworkTape playTape;
    private final INetwork replayNetwork;
    private final BooleanProperty movableNodesProperty = new SimpleBooleanProperty(true);

    public FrameHandler(INetworkTape playTape, INetwork replayNetwork) {

        this.playTape = playTape;
        this.replayNetwork = replayNetwork;
    }

    public BooleanProperty getMovableNodesProperty() {

        return movableNodesProperty;
    }

    public boolean getMovableNodes() {

        return movableNodesProperty.get();
    }

    @Override
    public void handle(ActionEvent event) {

        final INetworkTape.INetworkFrame playFrame = playTape.getFrame(playTape.getCurrentReadingFrame());

        logger.debug(playFrame.toString());

        replayNetwork.getViewPort().setMaxConnectionSize(playFrame.getMaxConnectionSize());
        replayNetwork.getViewPort().setMaxThroughput(playFrame.getMaxThroughput());
        replayNetwork.getViewPort().setViewTime(playFrame.getViewTime());

        if (playFrame.getVertexCount() < replayNetwork.getViewPort().getGraph().getVertexCount() ||
                playFrame.getEdgeCount() < replayNetwork.getViewPort().getGraph().getVertexCount()) {

            replayNetwork.getViewPort().graphIntersection(playFrame.getVertices(), playFrame.getEdges());
        }


        playFrame.getVertices().stream().forEach(node -> {

            replayNetwork.getWritingPort().writeNode(node);
            if (getMovableNodes()) {
                replayNetwork.getViewPort().setLocation(node, playFrame.transform(node));
            }
        });

        playFrame.getEdges().stream().forEach(edge -> {
            replayNetwork.getWritingPort().writeConnection(edge);
        });

        //logger.debug(replayNetwork.toString());
    }
}
