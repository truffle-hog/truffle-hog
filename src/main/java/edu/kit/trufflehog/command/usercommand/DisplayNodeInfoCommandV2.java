package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Hoehler on 04.03.2016.
 * FOR TESTING PURPOSES ONLY!
 */
public class DisplayNodeInfoCommandV2 implements IUserCommand {
    private AnchorPane anchorPane;
    private INode node;
    private int thr;

    public DisplayNodeInfoCommandV2(INode node, AnchorPane pane) {
        anchorPane = pane;
        this.node = node;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> {
            //OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
            GridPane nodeStatisticsOverlay = new GridPane();
            nodeStatisticsOverlay.setHgap(100);
            nodeStatisticsOverlay.setVgap(100);
            anchorPane.getChildren().set(3, nodeStatisticsOverlay);
            anchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
            anchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);

            Text throughput = new Text("Throughput: " + node.getComposition().getComponent(NodeStatisticsComponent.class).getThroughput());
            node.getComposition().getComponent(NodeStatisticsComponent.class).getThroughputProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    thr = newValue.intValue();
                    throughput.setText("Throughput: " + thr);
                }
            });
            node.getComposition().getComponent(NodeStatisticsComponent.class).getObjectProperty().addListener(new ChangeListener<NodeStatisticsComponent>() {
                @Override
                public void changed(ObservableValue<? extends NodeStatisticsComponent> observable, NodeStatisticsComponent oldValue, NodeStatisticsComponent newValue) {
                    throughput.setText("Throughput: " + newValue.getThroughput());
                }
            });
            if (node.getComposition().getComponent(PacketDataLoggingComponent.class) == null) return;
            node.getComposition().getComponent(PacketDataLoggingComponent.class).getObservablePacketsProperty().addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change c) {
                    c.next();
                    List<IPacketData> newPackets = c.getAddedSubList();

                    Platform.runLater(() -> {
                        int counter = 1;
                        for (IPacketData i : newPackets) {
                            Label data = new Label(i.toString() + counter);
                            nodeStatisticsOverlay.add(data, 0, counter);

                            counter++;
                        }
                    });
                }
            });
            node.getComposition().getComponent(PacketDataLoggingComponent.class).getObservablePacketsProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                }
            });

            nodeStatisticsOverlay.add(throughput, 0, 0);
        });
    }
}
