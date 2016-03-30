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
    private final IFilter filter;
    private boolean running = false;

    private long[] addresses;
    private int maxAddresses = 100;

    public TruffleCrook(INetworkWritingPort writingPort, IFilter filter) {
        networkWritingPort = writingPort;
        this.filter = filter;
        init();
    }

    @Override
    public void connect() {
        running = true;
    }

    @Override
    public void disconnect() {
        running = false;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            synchronized (this) {
                try {
                    Thread.sleep(10);
                    while (!running) {
                        wait();
                    }

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
        int a2 =  (int)(Math.random()*maxAddresses / 2);
        a2 = a2 == 0 ? 1 : a2;

        try {
            return Truffle.buildTruffle(addresses[a1],
                    addresses[a2],
                    addresses[a1],
                    addresses[a2],
                    null,
                    0,
                    0,
                    null,
                    0,
                    null,
                    0,
                    0,
                    0);
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