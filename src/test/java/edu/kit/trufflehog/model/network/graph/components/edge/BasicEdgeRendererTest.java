package edu.kit.trufflehog.model.network.graph.components.edge;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 25.03.2016.
 *
 * TODO: write tests for getter?
 */
public class BasicEdgeRendererTest {

    @Test
    public void equals() {
        BasicEdgeRenderer renderer = new BasicEdgeRenderer();
        assertTrue(renderer.equals(new BasicEdgeRenderer()));
    }
}