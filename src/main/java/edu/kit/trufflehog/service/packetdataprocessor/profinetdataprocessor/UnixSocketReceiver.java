package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.trufflecommand.PluginNotRunningCommand;
import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.view.NetworkGraphView;

import java.util.List;

/**
 * <p>
 *     This implementation of the {@link TruffleReceiver} uses a unix socket
 *     to communicate with the spp_profinet snort plugin.
 * </p>
 *
 * @author Mark Giraud
 * @version 0.1
 */
public class UnixSocketReceiver extends TruffleReceiver {

    private final INetworkGraph graph;
    private final List<Filter> filters;

    private boolean connected = false;

    /**
     * <p>
     *     Creates the UnixSocketReceiver.
     * </p>
     */
    public UnixSocketReceiver(INetworkGraph graph, List<Filter> filters) {
        this.graph = graph;
        this.filters = filters;
    }

    /**
     * <p>
     *     The main method of the UnixSocketReceiver service.
     * </p>
     *
     * <p>
     *     Tries to connect to the spp_profinet process.
     *     If the connection failed a {@link PluginNotRunningCommand} is sent to all listeners.
     *     Otherwise the service starts receiving packet data from the spp_profinet snort plugin,
     *     packs the data into {@link Truffle} objects and then generates {@link ITruffleCommand} objects and sends
     *     them to all listeners.
     * </p>
     */
    @Override
    public void run() {

        while(!Thread.interrupted()) {
            synchronized (this) {
                try {
                    while (!connected) {
                        this.wait();
                    }

                    notifyListeners(new AddPacketDataCommand(graph, getTruffle(), filters));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {

        try {
            if (!connected) {
                openIPC();
            }

            connected = true;
            synchronized (this) {
                this.notifyAll();
            }
        } catch (SnortPNPluginNotRunningException e) {
            notifyListeners(new PluginNotRunningCommand());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {

        connected = false;

        synchronized (this) {
            if (connected) {
                closeIPC();
            }
        }

    }

    private native void openIPC() throws SnortPNPluginNotRunningException;

    private native void closeIPC();

    private native Truffle getTruffle();
}
