package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.PacketDataInteraction;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by Hoehler on 28.04.2016.
 */
public class PacketDataViewController extends AnchorPaneController<PacketDataInteraction> {
    private final Map<PacketDataInteraction, IUserCommand> interactionMap = new EnumMap<>(PacketDataInteraction.class);
    private final PacketDataViewController packetDataViewController;
    private ObservableList<Packet> data;
    private TableView<Packet> tableView;
    private static final Logger logger = LogManager.getLogger();

    public PacketDataViewController(final ObservableList<Packet> data) {

        super("filter_menu_overlay.fxml", new EnumMap<>(PacketDataInteraction.class));

        this.data = data;
        // Build the table view and the filter menu
        BorderPane borderPane = setUpMenu(setUpTableView());

        // Add menu to overlay
        this.getChildren().add(borderPane);
        this.packetDataViewController = this;
    }

    @Override
    public void addCommand(PacketDataInteraction interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }

    /**
     * <p>
     * Sets up the entire TableView with all its functionalities.
     * </p>
     *
     * @return The created TableView.
     */
    private TableView<Packet> setUpTableView() {
        // Set up table view
        tableView = new TableView<>();
        tableView.setEditable(true);
        tableView.setMinWidth(500);
        tableView.setMinHeight(280);

        setUpTypeColumn(tableView);

        tableView.setItems(data);

        return tableView;
    }

    public void setItems(final ObservableList<Packet> data) {
        this.data = data;
        tableView.setItems(data);
    }

    /**
     * <p>
     * Sets up the OverlayMenu with all buttons from the existing table view.
     * </p>
     *
     * @param tableView The table view to put on the overlay menu.
     * @return A {@link BorderPane} containing the full menu.
     */
    private BorderPane setUpMenu(TableView<Packet> tableView) {

        final Button closeButton = setCloseButton();

        // Set up components on overlay
        final BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        tableView.setMinHeight(300);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(closeButton);
        borderPane.setBottom(anchorPane);

        AnchorPane.setBottomAnchor(closeButton, 0d);
        AnchorPane.setRightAnchor(closeButton, 0d);


        return borderPane;
    }

    private void setUpTypeColumn(TableView<Packet> tableView) {
        // Set up type column
        final TableColumn<Packet, String> typeColumn = new TableColumn<>("Packets");
        tableView.setEditable(true);
        typeColumn.setMinWidth(500);
        typeColumn.setPrefWidth(500);
        tableView.getColumns().add(typeColumn);
        typeColumn.setCellValueFactory(param -> param.getValue().getDataProperty());
    }

    public void update(PacketDataLoggingComponent component){
        if (component == null) throw new NullPointerException("component must not be null!");
        tableView.getItems().clear();
        int i = 0;
        for (IPacketData packetData:component.getObservablePackets()) {
            data.add(0, new Packet(packetData.toString()));
        }
    }

    public void addEntry(IPacketData entry) {
        data.add(new Packet(entry.toString()));
    }

    public void addEntries(List<IPacketData> entries) {
        for (IPacketData packetData:entries) {
            data.add(0, new Packet(packetData.toString()));
        }
    }

    /**
     * <p>
     * Sets up the button to close this window
     * </p>
     *
     * @return The fully configured close button.
     */
    private Button setCloseButton() {
        final Button closeButton = new Button("Close");
        closeButton.setScaleX(0.9);
        closeButton.setScaleY(0.9);

        closeButton.setOnMouseClicked(event -> packetDataViewController.setVisible(false));

        return closeButton;
    }

    public static class Packet {
        private final SimpleStringProperty data;

        private Packet(String data) {
            this.data = new SimpleStringProperty(data);
        }

        public StringProperty getDataProperty() {
            return data;
        }
    }

}
