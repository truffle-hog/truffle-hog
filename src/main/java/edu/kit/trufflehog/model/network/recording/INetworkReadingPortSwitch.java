package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkReadingPort;

/**
 * Created by jan on 20.02.16.
 */
public interface INetworkReadingPortSwitch extends INetworkReadingPort {

    INetworkReadingPort getActiveReadingPort();

    void setActiveReadingPort(INetworkReadingPort port);

}
