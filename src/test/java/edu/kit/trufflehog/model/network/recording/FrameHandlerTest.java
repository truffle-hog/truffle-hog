package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetwork;
//import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import org.junit.Test;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class FrameHandlerTest {

    @Test
    public void testHandle() throws Exception {

        //final INetwork replayNetwork = new ReplayNetwork(new ConcurrentDirectedSparseGraph<>());
        final INetworkTape playTape = new NetworkTape(5);



        //final FrameHandler handler = new FrameHandler(playTape, replayNetwork);


    }
}