/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class StartRecordCommand implements IUserCommand {

    private final INetworkDevice networkDevice;
    private final INetwork network;
    private final INetworkTape networkTape;

    public StartRecordCommand(INetworkDevice networkDevice, INetwork network, INetworkTape tape) {

        this.networkDevice = networkDevice;
        this.network = network;
        this.networkTape = tape;

    }

    @Override
    public void execute() {

        networkDevice.record(network, networkTape, 20);
    }
}
