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
    private final INetwork replayLayout;
    private final BooleanProperty movableNodesProperty = new SimpleBooleanProperty(true);

    public FrameHandler(INetworkTape playTape, INetwork replayLayout) {

        this.playTape = playTape;
        this.replayLayout = replayLayout;
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

        replayLayout.getViewPort().setMaxConnectionSize(playFrame.getMaxConnectionSize());
        replayLayout.getViewPort().setMaxThroughput(playFrame.getMaxThroughput());
        replayLayout.getViewPort().setViewTime(playFrame.getViewTime());

        if (playFrame.getGraph().getVertexCount() < replayLayout.getViewPort().getGraph().getVertexCount() ||
                playFrame.getGraph().getEdgeCount() < replayLayout.getViewPort().getGraph().getVertexCount()) {

            replayLayout.getViewPort().graphIntersection(playFrame.getGraph());
        }


        playFrame.getGraph().getVertices().stream().forEach(node -> {
            replayLayout.getWritingPort().writeNode(node);
            if (getMovableNodes()) {
                replayLayout.getViewPort().setLocation(node, playFrame.transform(node));
            }
        });

        playFrame.getGraph().getEdges().stream().forEach(edge -> {
            replayLayout.getWritingPort().writeConnection(edge);
        });

        //logger.debug(replayLayout.toString());
    }
}
