package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.trufflecommand.StartPluginCommand;

/**
 * <p>
 *     This implementation of the {@link TruffleReceiver} uses a unix socket
 *     to communicate with the spp_profinet snort plugin.
 * </p>
 */
public class UnixSocketReceiver extends TruffleReceiver {

    /**
     * <p>
     *     Creates the UnixSocketReceiver.
     * </p>
     */
    public UnixSocketReceiver() {
        throw new UnsupportedOperationException("Note implemented yet!");
    }

    /**
     * <p>
     *     The main method of the UnixSocketReceiver service.
     * </p>
     *
     * <p>
     *     Tries to connect to the spp_profinet process.
     *     If the connection failed a {@link StartPluginCommand} is sent to all listeners.
     *     Otherwise the service starts receiving packet data from the spp_profinet snort plugin,
     *     packs the data into {@link Truffle} objects and then generates {@link ITruffleCommand} objects and sends
     *     them to all listeners.
     * </p>
     */
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private native void initIPC() throws SnortPNPluginNotRunningException;

    private native void getTruffle();

    private void sendStartPluginCommand() {
        notifyListeners(new StartPluginCommand());
    }
}
