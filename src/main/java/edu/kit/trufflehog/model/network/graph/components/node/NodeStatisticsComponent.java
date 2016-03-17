package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent extends AbstractComponent implements IComponent {

    private static final Logger logger = LogManager.getLogger(NodeStatisticsComponent.class);
    
    private final IntegerProperty communicationCount = new SimpleIntegerProperty(1);
    private final DoubleProperty throughput = new SimpleDoubleProperty(1);

    public NodeStatisticsComponent(int initial) {

        communicationCount.set(initial);
    }

    public IntegerProperty getCommunicationCountProperty() {
        return communicationCount;
    }

    public int getCommunicationCount() {
        return communicationCount.get();
    }

    //FIXME fix concurrency problem?
    public void setCommunicationCountProperty(int value) {
        communicationCount.set(value);
    }

    public void incrementCommunicationCount(int step) {
        setCommunicationCountProperty(getCommunicationCount() + step);
    }

    public DoubleProperty getThroughputProperty() {
        return throughput;
    }

    public double getThroughput() {
        return throughput.getValue();
    }

    public void setThroughput(double value) {
        throughput.setValue(value);
    }

    @Override
    public String name() {
        return "Traffic info";
    }

    @Override
    public <T> T accept(IComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String toString() {

        return name() + ": " + "Throughput=" + getCommunicationCount();

    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NodeStatisticsComponent);
    }
}
