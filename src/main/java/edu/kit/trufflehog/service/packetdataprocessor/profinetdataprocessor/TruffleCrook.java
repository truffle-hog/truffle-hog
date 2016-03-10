package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

/**
 * Created by Hoehler on 04.03.2016.
 * FOR TESTING PURPOSES ONLY!
 */
public class TruffleCrook extends TruffleReceiver {
    private final INetworkWritingPort networkWritingPort;
    private long lastCreation = 0;

    private long[] addresses;
    private int maxAddresses = 10;

    public TruffleCrook(INetworkWritingPort writingPort, IFilter filter) {
        networkWritingPort = writingPort;
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
                    Thread.sleep(1000);

                    final Truffle truffle = getTruffle();

                    if (truffle != null) {
                        notifyListeners(new AddPacketDataCommand(networkWritingPort, truffle, node -> System.out.println("Dummy filter")));
                    }

                    Thread.sleep(10);
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
            return Truffle.buildTruffle(addresses[0], addresses[a2], 0, 0, null, (short) 0);
        } catch (InvalidProfinetPacket invalidProfinetPacket) {
            invalidProfinetPacket.printStackTrace();
        }

        return null;
    }

    private void init() {
        addresses = new long[maxAddresses];
        for (int i = 0; i < maxAddresses; i++) {
            addresses[i] = (long)(Math.random()*10000000);
        }
    }
}