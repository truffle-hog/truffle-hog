package edu.kit.trufflehog.service;
import org.junit.Test;

/**
 * Test class for NodeStatisticsUpdater.
 */
public class NodeStatisticsUpdaterTest {

    @Test(expected = NullPointerException.class)
    public void firstNullTest() {
        NodeStatisticsUpdater myUpdater = new NodeStatisticsUpdater(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void secondNullTest() {
        NodeStatisticsUpdater myUpdater = new NodeStatisticsUpdater(null, null, 0);
    }
}
