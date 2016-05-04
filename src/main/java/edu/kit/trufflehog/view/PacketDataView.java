package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.PacketDataInteraction;
import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Hoehler on 28.04.2016.
 */
public class PacketDataView extends AnchorPaneController<PacketDataInteraction> {
    private final Map<PacketDataInteraction, IUserCommand> interactionMap = new EnumMap<>(PacketDataInteraction.class);
    private final PacketDataView packetDataView;
    private ObservableList<Packet> data;
    private TableView<Packet> tableView;
    private TableColumn<Packet, String> nameColumn;

    private final ArrayList<PacketDataLoggingComponent> pdlComponentArray = new ArrayList<>();
    private final ArrayList<ListChangeListener> pdlListenerArray = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

    public PacketDataView(final ObservableList<Packet> data) {

        super("filter_menu_overlay.fxml", new EnumMap<>(PacketDataInteraction.class));

        this.data = data;
        // Build the table view and the filter menu
        BorderPane borderPane = setUpMenu(setUpTableView());

        // Add menu to overlay
        this.getChildren().add(borderPane);
        this.packetDataView = this;
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

        setUpNameColumn(tableView);

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

    private void setUpNameColumn(TableView<Packet> tableView) {
        // Set up name column
        nameColumn = new TableColumn<>("Packets");
        tableView.setEditable(true);
        nameColumn.setMinWidth(500);
        nameColumn.setPrefWidth(500);
        tableView.getColumns().add(nameColumn);
        nameColumn.setCellValueFactory(param -> param.getValue().getDataProperty());
    }

    private void addEntry(IPacketData entry) {
        data.add(new Packet(packetDataToString(entry)));
    }

    private void addEntries(List<IPacketData> entries) {
        for (IPacketData packetData:entries) {
            data.add(new Packet(packetDataToString(packetData)));
        }
    }

    private String packetDataToString(IPacketData packetData) {
        if (packetData == null) return "";
        StringBuilder sb = new StringBuilder();

        Long timeOfArrival = packetData.getAttribute(Long.class, "timeOfArrival");
        MacAddress srcMacAddress = packetData.getAttribute(MacAddress.class, "sourceMacAddress");
        MacAddress destMacAddress = packetData.getAttribute(MacAddress.class, "destMacAddress");
        IPAddress srcIPAddress = packetData.getAttribute(IPAddress.class, "sourceIPAddress");
        IPAddress destIPAddress = packetData.getAttribute(IPAddress.class, "destIPAddress");
        String nameOfStation = packetData.getAttribute(String.class, "nameOfStation");
        Short etherType = packetData.getAttribute(Short.class, "etherType");
        Integer serviceID = packetData.getAttribute(Integer.class, "serviceID");
        String serviceIDName = packetData.getAttribute(String.class, "serviceIDName");
        Integer serviceType = packetData.getAttribute(Integer.class, "serviceType");
        String serviceTypeName = packetData.getAttribute(String.class, "serviceTypeName");
        Long xid = packetData.getAttribute(Long.class, "xid");
        Integer responseDelay = packetData.getAttribute(Integer.class, "responseDelay");
        Integer isResponse = packetData.getAttribute(Integer.class, "isResponse");

        if (timeOfArrival != null) {
            Date date = new Date(timeOfArrival);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            sb.append("Time: ");
            sb.append(sdf.format(date));
        }

        sb.append("\nSource MAC address: ");
        if (srcMacAddress != null) sb.append(srcMacAddress.toString());

        sb.append("\nSource IP address: ");
        if (srcIPAddress != null) sb.append(srcIPAddress.toString());

        sb.append("\nDestination MAC address: ");
        if (destMacAddress != null) sb.append(destMacAddress.toString());

        sb.append("\nDestination IP address: ");
        if (destIPAddress != null) sb.append(destIPAddress.toString());

        if (nameOfStation != null) {
            sb.append("\nDevice name: ");
            sb.append(nameOfStation);
        }

        if (etherType != null) {
            sb.append("\nEther type: ");
            sb.append(etherType.toString());
        }

        if (serviceID != null) {
            sb.append("\nService ID: ");
            sb.append(serviceID.toString());
        }

        if (serviceIDName != null) {
            sb.append("\nService ID name: ");
            sb.append(serviceIDName);
        }

        if (serviceType != null) {
            sb.append("\nService type: ");
            sb.append(serviceType.toString());
        }

        if (serviceTypeName != null) {
            sb.append("\nService type name: ");
            sb.append(serviceTypeName);
        }

        if (xid != null) {
            sb.append("\nXID: ");
            sb.append(xid.toString());
        }

        if (responseDelay != null) {
            sb.append("\nResponse delay: ");
            sb.append(responseDelay.toString());
        }

        if (isResponse != null) {
            sb.append("\nIs response: ");
            sb.append(isResponse.toString());
        }

        sb.append("\n\n");

        return sb.toString();
    }

    public void setName(String name) {
        nameColumn.setText(name);
    }

    public void register(Set<INode> nodeSet) {
        if (nodeSet == null) throw new NullPointerException("node must not be null!");
        clear();

        int i = 0;
        PacketDataLoggingComponent loggingComponent = null;
        NodeInfoComponent infoComponent = null;

        StringBuilder sb = new StringBuilder();
        sb.append("Nodes: ");

        for (INode node:nodeSet) {
            loggingComponent = node.getComponent(PacketDataLoggingComponent.class);
            infoComponent = node.getComponent(NodeInfoComponent.class);

            if (loggingComponent != null && infoComponent != null) {
                //TODO add comparator to IPacketData to sort list using time of arrival
                addEntries(loggingComponent.getObservablePackets());

                ListChangeListener listener = c -> {
                    c.next();
                    addEntries(c.getAddedSubList());
                };

                loggingComponent.getObservablePacketsProperty().addListener(listener);

                pdlComponentArray.add(i, loggingComponent);
                pdlListenerArray.add(i, listener);

                sb.append(infoComponent.getMacAddress());
                sb.append(" ");
            }

            loggingComponent = null;
            infoComponent = null;

            i++;
        }

        setName(sb.toString());
    }

    private void clear() {
        PacketDataLoggingComponent component = null;

        for (int i = 0; i < pdlComponentArray.size(); i++) {
            component = pdlComponentArray.get(i);
            component.getObservablePacketsProperty().removeListener(pdlListenerArray.get(i));
        }

        pdlComponentArray.clear();
        pdlListenerArray.clear();
        tableView.getItems().clear();
        data.clear();
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

        closeButton.setOnMouseClicked(event -> packetDataView.setVisible(false));

        clear();

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
