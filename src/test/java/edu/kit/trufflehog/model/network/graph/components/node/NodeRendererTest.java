package edu.kit.trufflehog.model.network.graph.components.node;

import org.junit.Test;

/**
 * Created by Hoehler on 25.03.2016.
 */
public class NodeRendererTest {

    @Test(expected = NullPointerException.class)
    public void constructorNullPointer() throws Exception {
        NodeRenderer renderer = new NodeRenderer(null, null, null);
    }
}