package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.trufflecommand.ReceiverErrorCommand;

/**
 * <p>
 *     This implementation of the {@link TruffleReceiver} uses a message queue
 *     to communicate with the spp_profinet snort plugin.
 * </p>
 */
public class MessageQueueReceiver extends TruffleReceiver {

    /**
     * <p>
     *     Creates the MessageQueueReceiver
     * </p>
     */
    public MessageQueueReceiver() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * <p>
     *     The main method of the MessageQueueReceiver service.
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
        throw new UnsupportedOperationException("Not implemented yet!");

        //initIPC();
    }

    private native void initIPC();

    private native void getTruffle();

    private void sendStartPluginCommand() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public void connect() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
