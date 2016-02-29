package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.trufflecommand.ReceiverErrorCommand;
import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.*;

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

    private final INetworkWritingPort networkWritingPort;
    private final List<Filter> filters;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Logger logger = LogManager.getLogger();


    private boolean connected = false;

    /**
     * <p>
     *     Creates the UnixSocketReceiver.
     * </p>
     */
    public UnixSocketReceiver(INetworkWritingPort networkWritingPort, List<Filter> filters) {
        this.networkWritingPort = networkWritingPort;
        this.filters = filters;
    }

    /**
     * <p>
     *     The main method of the UnixSocketReceiver service.
     * </p>
     *
     * <p>
     *     Tries to connect to the spp_profinet process.
     *     If the connection failed a {@link ReceiverErrorCommand} is sent to all listeners.
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

                    Truffle truffle = getTruffle();

                    if (truffle != null) {
                        notifyListeners(new AddPacketDataCommand(networkWritingPort, truffle, filters));
                    }
                } catch (InterruptedException e) {
                    logger.debug("UnixSocketReceiver interrupted. Exiting...");
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

        if (!connected) {

            try {
                openIPC();

                connected = true;

                synchronized (this) {
                    this.notifyAll();
                }
            } catch (SnortPNPluginNotRunningException e) {
                notifyListeners(new ReceiverErrorCommand("Snort plugin doesn't seem to be running."));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {

        if (connected) {
            connected = false;

            synchronized (this) {
                try {
                    closeIPC();
                } catch (SnortPNPluginDisconnectFailedException e) {
                    logger.error(e);
                    notifyListeners(new ReceiverErrorCommand("Couldn't disconnect from plugin correctly."));
                }
            }
        }
    }

    private native void openIPC() throws SnortPNPluginNotRunningException;

    private native void closeIPC() throws SnortPNPluginDisconnectFailedException;

    private native Truffle getTruffle();
}
