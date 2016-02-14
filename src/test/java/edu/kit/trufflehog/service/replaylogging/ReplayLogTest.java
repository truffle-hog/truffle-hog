package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.model.graph.NetworkGraph;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * <p>
 *     Tests for the ReplayLog equals and compareTo methods.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLogTest {

    /**
     * <p>
     *     Test for comparison. We can assume here that commands are ordered in the order they were added into the queue,
     *     that means by ascending time.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testCompareTo() throws Exception {
        // Check for less than
        LinkedMap<IReplayCommand, Long> replayCommands1 = new LinkedMap<>();
        IReplayCommand command1 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command1, 2L);
        IReplayCommand command2 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command2, 3L);
        INetworkGraph graph1 = mock(NetworkGraph.class);
        ReplayLog replayLog1 = new ReplayLog(graph1, replayCommands1);

        LinkedMap<IReplayCommand, Long> replayCommands2 = new LinkedMap<>();
        IReplayCommand command3 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command3, 1L);
        IReplayCommand command4 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command4, 4L);
        INetworkGraph graph2 = mock(NetworkGraph.class);
        ReplayLog replayLog2 = new ReplayLog(graph2, replayCommands2);

        assertEquals(true, replayLog1.compareTo(replayLog2) < 0);

        // Check for greater than
        IReplayCommand command5 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command5, 8L);
        IReplayCommand command6 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command6, 23L);
        replayLog1 = new ReplayLog(graph1, replayCommands1);

        IReplayCommand command7 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command7, 0L);
        IReplayCommand command8 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command8, 22L);
        replayLog2 = new ReplayLog(graph2, replayCommands2);

        assertEquals(true, replayLog1.compareTo(replayLog2) > 0);

        // Check for equals
        IReplayCommand command9 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command9, 1L);
        IReplayCommand command10 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command10, 23L);
        replayLog1 = new ReplayLog(graph1, replayCommands1);

        IReplayCommand command11 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command11, 0L);
        IReplayCommand command12 = mock(AddPacketDataCommand.class);
        replayCommands2.put(command12, 23L);
        replayLog2 = new ReplayLog(graph2, replayCommands2);

        assertEquals(true, replayLog1.compareTo(replayLog2) == 0);
    }

    /**
     * <p>
     *     Tests if the equals method works correctly
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testEquals() throws Exception {
        // Check for equality
        LinkedMap<IReplayCommand, Long> replayCommands1 = new LinkedMap<>();
        IReplayCommand command1 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command1, 2L);
        IReplayCommand command2 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command2, 3L);
        INetworkGraph graph1 = mock(NetworkGraph.class);
        ReplayLog replayLog1 = new ReplayLog(graph1, replayCommands1);

        LinkedMap<IReplayCommand, Long> replayCommands2 = new LinkedMap<>();
        IReplayCommand command3 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command3, 2L);
        IReplayCommand command4 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command4, 3L);
        ReplayLog replayLog2 = new ReplayLog(graph1, replayCommands1);

        assertEquals(true, replayLog1.equals(replayLog2));

        // Adding an already existing command, should still be true
        IReplayCommand command5 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command5, 3L);
        replayLog1 = new ReplayLog(graph1, replayCommands1);

        assertEquals(true, replayLog1.equals(replayLog2));

        // Check for inequality
        IReplayCommand command6 = mock(AddPacketDataCommand.class);
        replayCommands1.put(command6, 8L);
        replayLog1 = new ReplayLog(graph1, replayCommands1);

        assertEquals(false, replayLog1.equals(replayLog2));
    }
}