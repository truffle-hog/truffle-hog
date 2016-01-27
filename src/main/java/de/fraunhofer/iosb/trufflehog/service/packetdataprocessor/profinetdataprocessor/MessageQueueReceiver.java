package de.fraunhofer.iosb.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import de.fraunhofer.iosb.trufflehog.command.trufflecommand.ITruffleCommand;
import de.fraunhofer.iosb.trufflehog.command.trufflecommand.StartPluginCommand;

/**
 * This implementation of the {@link TruffleReceiver} uses a message queue
 * to communicate with the spp_profinet snort plugin.
 *
 * @author Mr. X
 * @version 0.0
 */
public class MessageQueueReceiver extends TruffleReceiver {

    /**
     * Creates the MessageQueueReceiver
     */
    public MessageQueueReceiver() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Tries to connect to the spp_profinet process.
     * If the connection failed a {@link StartPluginCommand} is sent to all listeners.
     * Otherwise the service starts receiving packet data from the spp_profinet snort plugin,
     * packs the data into {@link Truffle} objects and then generates {@link ITruffleCommand} objects and sends
     * them to all listeners.
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
}
