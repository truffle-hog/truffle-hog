package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkWritingPort;

/**
 *
 */
public interface INetworkWritingPortSwitch extends INetworkWritingPort {

    INetworkWritingPort getActiveWritingPort();

    void setActiveWritingPort(INetworkWritingPort writingPort);

}
