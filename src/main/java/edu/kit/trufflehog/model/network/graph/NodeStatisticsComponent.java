package edu.kit.trufflehog.model.network.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent implements IComponent {

    private final IntegerProperty throughputProperty = new SimpleIntegerProperty(1);

    public NodeStatisticsComponent(int initial) {

        throughputProperty.set(initial);
    }

    public IntegerProperty getThroughputProperty() {
        return throughputProperty;
    }

    public int getThroughput() {
        return throughputProperty.get();
    }

    public void setThroughputProperty(int value) {
        throughputProperty.set(value);
    }

    public void incrementThroughput(int step) {
        setThroughputProperty(getThroughput() + step);
    }

    @Override
    public String name() {
        return "traffic info";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public IComponent createDeepCopy() {

        final IComponent copy = new NodeStatisticsComponent(throughputProperty.get());

        return copy;
    }
}
