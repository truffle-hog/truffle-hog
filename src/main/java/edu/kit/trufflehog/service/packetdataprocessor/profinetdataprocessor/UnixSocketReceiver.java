package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.trufflecommand.ReceiverErrorCommand;
import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.INetworkGraph;
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

    private final INetworkGraph graph;
    private final List<Filter> filters;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Logger logger = LogManager.getLogger();


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



                    Future<Truffle> future = executor.submit(this::getTruffle);

                    notifyListeners(new AddPacketDataCommand(graph, future.get(1, TimeUnit.SECONDS), filters));
                } catch (InterruptedException e) {
                    logger.debug("UnixSocketReceiver interrupted. Exiting...");
                    Thread.currentThread().interrupt();
                } catch (ExecutionException | TimeoutException e) {
                    logger.error(e);
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
            Future<Void> future = executor.submit(() -> {
                openIPC();
                return null;
            });

            try {
                future.get(1, TimeUnit.SECONDS);

                connected = true;

                synchronized (this) {
                    this.notifyAll();
                }
            } catch (InterruptedException e) {
                logger.error(e);
            } catch (ExecutionException e) {
                logger.error(e);
                notifyListeners(new ReceiverErrorCommand("Snort does not seem to be running!"));
            } catch (TimeoutException e) {
                logger.error(e);
                notifyListeners(new ReceiverErrorCommand("Connect took too long!"));
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

                Future<Void> future = executor.submit(() -> {
                    closeIPC();
                    return null;
                });

                try {
                    future.get(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error(e);
                } catch (ExecutionException e) {
                    logger.error(e);
                    notifyListeners(new ReceiverErrorCommand("Disconnect failed for some reason. See logs for more info."));
                } catch (TimeoutException e) {
                    logger.error(e);
                    notifyListeners(new ReceiverErrorCommand("Disconnect took too long!"));
                }
            }
        }
    }

    private native void openIPC() throws SnortPNPluginNotRunningException;

    private native void closeIPC();

    private native Truffle getTruffle();
}
