package edu.kit.trufflehog.model.network.graph.components.edge;

import de.saxsys.javafx.test.JfxRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 25.03.2016.
 *
 */

@RunWith(JfxRunner.class)
public class BasicEdgeRendererTest {

    @Test
    public void equals() throws Exception {
        BasicEdgeRenderer renderer = new BasicEdgeRenderer();
        assertTrue(renderer.equals(new BasicEdgeRenderer()));
    }

    @Test
    public void togglePicked() throws Exception {
        BasicEdgeRenderer renderer = new BasicEdgeRenderer();
    }
}