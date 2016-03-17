package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.graph.INode;

/**
 * Created by Hoehler on 04.03.2016.
 * FOR TESTING PURPOSES ONLY!
 */
public class TruffleCrook extends TruffleReceiver {
    private final INetworkWritingPort networkWritingPort;
    private final IFilter filter;

    private long[] addresses;
    private int maxAddresses = 20000;

    public TruffleCrook(INetworkWritingPort writingPort, IFilter filter) {
        networkWritingPort = writingPort;
        this.filter = filter;
        init();
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            synchronized (this) {
                try {
                    Thread.sleep(0, 1);

                    final Truffle truffle = getTruffle();

                    if (truffle != null) {
                        notifyListeners(new AddPacketDataCommand(networkWritingPort, truffle, filter));
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private Truffle getTruffle() {
        int a1 = (int)(Math.random()*maxAddresses);
        int a2 = (int)(Math.random()*maxAddresses);

        try {
            return Truffle.buildTruffle(addresses[0], addresses[a2], addresses[0], addresses[a2], null, (short) 0);
        } catch (InvalidProfinetPacket invalidProfinetPacket) {
            invalidProfinetPacket.printStackTrace();
        }

        return null;
    }

    private void init() {
        addresses = new long[maxAddresses];
        for (int i = 0; i < maxAddresses; i++) {
            addresses[i] = i + 1;
        }
    }
}