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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.Instant;
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

            ScrollPane packetPane = new ScrollPane();
            packetPane.setPrefSize(200,400);
            VBox vb = new VBox();

            /*
            anchorPane.getChildren().set(3, nodeStatisticsOverlay);
            anchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
            anchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
            */
            anchorPane.getChildren().set(3, packetPane);
            anchorPane.setTopAnchor(nodeStatisticsOverlay, 200d);
            anchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
            /*
            for (int i = 0; i <= 100; i++) {
                Label l = new Label(i + ". entry");
                vb.getChildren().add(0,l);
            }
            packetPane.setContent(vb);
            */
            PacketDataLoggingComponent pdlc = node.getComposition().getComponent(PacketDataLoggingComponent.class);
            if (pdlc != null) {
                for (IPacketData i: pdlc.getObservablePackets()) {
                    vb.getChildren().add(0, new Label(i.toString()));
                }
            }


            Text throughput = new Text("Throughput: " + node.getComposition().getComponent(NodeStatisticsComponent.class).getThroughput());
            node.getComposition().getComponent(NodeStatisticsComponent.class).getThroughputProperty().addListener((observable, oldValue, newValue) -> {
                thr = newValue.intValue();
                throughput.setText("Throughput: " + thr);
            });
            node.getComposition().getComponent(NodeStatisticsComponent.class).getObjectProperty().addListener((observable, oldValue, newValue) -> {
                throughput.setText("Throughput: " + newValue.getThroughput());
            });
            if (node.getComposition().getComponent(PacketDataLoggingComponent.class) == null) return;
            node.getComposition().getComponent(PacketDataLoggingComponent.class).getObservablePacketsProperty().addListener((ListChangeListener) c -> {
                c.next();
                List<IPacketData> newPackets = c.getAddedSubList();

                Platform.runLater(() -> {
                    for (IPacketData i : newPackets) {
                        Label data = new Label("At: " + Instant.now()+"\n"+i.toString());
                        vb.getChildren().add(0,data);
                    }
                });
            });
            node.getComposition().getComponent(PacketDataLoggingComponent.class).getObservablePacketsProperty().addListener((observable, oldValue, newValue) -> {

            });

            packetPane.setContent(vb);
            nodeStatisticsOverlay.add(throughput, 0, 0);
        });
    }
}
