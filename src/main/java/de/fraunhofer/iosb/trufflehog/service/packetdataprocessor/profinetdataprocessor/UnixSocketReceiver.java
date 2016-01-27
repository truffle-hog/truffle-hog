package de.fraunhofer.iosb.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * This implementation of the {@link TruffleReceiver} uses a unix socket
 * to communicate with the spp_profinet snort plugin
 *
 * @author Mr. X
 * @version 0.0
 */
public class UnixSocketReceiver extends TruffleReceiver {

    public UnixSocketReceiver() {

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private native void initIPC();

    private native void getTruffle();

    private void sendStartPluginCommand() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
