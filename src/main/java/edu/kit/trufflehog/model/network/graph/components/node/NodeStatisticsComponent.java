package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent implements IComponent {

    private final IntegerProperty throughputProperty = new SimpleIntegerProperty(1);

    private ObjectProperty<NodeStatisticsComponent> objectProperty;

    public NodeStatisticsComponent(int initial) {

        throughputProperty.set(initial);
        objectProperty = new SimpleObjectProperty<NodeStatisticsComponent>(this) {
        };
    }

    public ObjectProperty<NodeStatisticsComponent> getObjectProperty() {return objectProperty; }

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

    @Override
    public boolean update(INode update) {

        // TODO maybe change to another value
        incrementThroughput(1);
        return true;
    }
}
