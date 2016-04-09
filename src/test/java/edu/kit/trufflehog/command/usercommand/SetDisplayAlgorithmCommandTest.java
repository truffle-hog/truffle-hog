package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Valentin Kiechle on 09.04.2016.
 */
public class SetDisplayAlgorithmCommandTest {

    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;
    private INetworkViewPort viewPort;
    private SetDisplayAlgorithmCommand sdac;

    @Before
    public void setup() {
        layoutFactory = mock(Transformer.class);
        viewPort = mock(INetworkViewPort.class);
        sdac = new SetDisplayAlgorithmCommand(layoutFactory, viewPort);
    }

    @After
    public void tearDown() {
        layoutFactory = null;
        viewPort = null;
        sdac = null;
    }

    @Test (expected = NullPointerException.class)
    public void setDisplayAlgorithmTest_NullParam() {
        sdac = new SetDisplayAlgorithmCommand(null, null);
    }

    @Test
    public void setDisplayAlgorithmTest_MethodGetsCalled () {
        sdac.execute();
        verify(viewPort, times(1)).setLayoutFactory(layoutFactory);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void setDisplayAlgorithmTest_DoNotSelectAnything() {
        sdac.setSelection(null);
    }
}