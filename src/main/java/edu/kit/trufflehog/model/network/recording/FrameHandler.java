package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;


public class FrameHandler implements EventHandler<ActionEvent> {

    private static final Logger logger = LogManager.getLogger(FrameHandler.class);

    private final INetworkTape playTape;
    private final INetwork replayNetwork;
    private final BooleanProperty movableNodesProperty = new SimpleBooleanProperty(false);

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

//        logger.debug(playFrame.toString());

/*        final Collection<INode> nodes = replayNetwork.getViewPort().getGraph().getVertices());

        nodes.stream().forEach(vertex -> {
            replayNetwork.getViewPort().getGraph().removeVertex(vertex);
        });*/

        replayNetwork.getViewPort().setMaxConnectionSize(playFrame.getMaxConnectionSize());
        replayNetwork.getViewPort().setMaxThroughput(playFrame.getMaxThroughput());
        replayNetwork.getViewPort().setViewTime(playFrame.getViewTime());

        if (playFrame.getEdgeCount() < replayNetwork.getViewPort().getGraph().getEdgeCount()) {

            final List<INode> toBeDeleted = replayNetwork.getViewPort().getGraph().getVertices().stream().
                    filter(v -> playFrame.transform(v) == null).collect(Collectors.toList());

            toBeDeleted.stream().forEach(v -> replayNetwork.getViewPort().getGraph().removeVertex(v));
        }

        playFrame.getEdges().stream().forEach(edge -> {
            replayNetwork.getWritingPort().writeConnection(edge);

            if (!getMovableNodes()) {
                replayNetwork.getViewPort().setLocation(edge.getDest(), playFrame.transform(edge.getDest()));
                replayNetwork.getViewPort().setLocation(edge.getSrc(), playFrame.transform(edge.getSrc()));
            }
        });
    }
}
