package edu.kit.trufflehog.model.network.graph.components.node;

import de.saxsys.javafx.test.JfxRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Hoehler on 25.03.2016.
 */

@RunWith(JfxRunner.class)
public class NodeRendererTest {

    @Test(expected = NullPointerException.class)
    public void constructorNullPointer() throws Exception {
        NodeRenderer renderer = new NodeRenderer(null, null, null);
    }

    @Test
    public void togglePicked() throws Exception {
        NodeRenderer renderer = new NodeRenderer();

    }
}