package edu.kit.trufflehog.command.trufflecommand;

/**
 * <p>
 *     Command used to add Truffle data to the graph. It updates the INodes and IConnections and creates new ones if
 *     necessary (i.e. when new devices enter the network). After the creation, the new nodes get checked with the
 *     Filter objects and marked accordingly.
 * </p>
 */
public class AddPacketDataCommand implements ITruffleCommand{

    public void execute() {

    }
}
