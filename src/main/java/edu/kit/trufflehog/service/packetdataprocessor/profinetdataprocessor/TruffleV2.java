package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * Created by Hoehler on 04.03.2016.
 */
public class TruffleV2 extends Truffle {
    public TruffleV2(long srcAddress, long destAddress) {
        setAttribute(Long.class, "sourceMacAddress", srcAddress);
        setAttribute(Long.class, "destMacAddress", destAddress);
    }
}
