package edu.kit.trufflehog.model.network.recording;

import org.junit.Test;

/**
 * Created by jan on 23.02.16.
 */
public class NetworkTapeTest {

    @Test
    public void testGetCurrentReadingFrame() throws Exception {

    }

    @Test
    public void testGetCurrentWritingFrame() throws Exception {

    }

    @Test
    public void testSetCurrentReadingFrame() throws Exception {

    }

    @Test
    public void testSetCurrentWritingFrame() throws Exception {

    }

    @Test
    public void testWrite() throws Exception {

        //FIXME ... change the test to new interface

  /*      final NodeCopier nodeCopier = new NodeCopier(new ComponentCopier());
        copier = new NetworkCopier(new TapeGraphCopier(nodeCopier, new ConnectionCopier(nodeCopier)));

        final Graph<INode, IConnection> graph = Graphs.synchronizedDirectedGraph(new DirectedSparseGraph<>());

        final ObservableUpdatableGraph<INode, IConnection> og = new ObservableUpdatableGraph<>(graph, new LiveUpdater());


        final LiveNetwork liveNetwork = new LiveNetwork(og);

        for (int i = 0; i < 4; i++) {

            final IConnection c = new NetworkConnection(new NetworkNode(new MacAddress(i)), new NetworkNode(new MacAddress(i+1)));

            final INode src = new NetworkNode(new MacAddress(i));
            final INode dest = new NetworkNode(new MacAddress(i + 1));

            liveNetwork.getWritingPort().writeNode(src);
            liveNetwork.getWritingPort().writeNode(dest);
            liveNetwork.getWritingPort().writeConnection(c);
        }

        final INetworkTape tape = new NetworkTape(25);
        tape.writeFrame(copyCreator.createDeepCopy(liveNetwork));

        liveNetwork.getWritingPort().writeNode(new NetworkNode(new MacAddress(0x00FFL)));

        tape.writeFrame(copyCreator.createDeepCopy(liveNetwork));


        System.out.println(tape.toString());*/
    }

    @Test
    public void testFrameCount() throws Exception {

    }

    @Test
    public void testGetFrameRate() throws Exception {

    }
}