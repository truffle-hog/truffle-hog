package edu.kit.trufflehog.model.network.graph.components.node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 25.03.2016.
 *
 * TODO: find reasons to test this as there are only getter and setter methods
 */
public class NodeStatisticsComponentTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void equals() {
        NodeStatisticsComponent component = new NodeStatisticsComponent(0, 0);
        assertTrue(component.equals(new NodeStatisticsComponent(1, 1)));
    }

    @After
    public void tearDown() throws Exception {

    }
}