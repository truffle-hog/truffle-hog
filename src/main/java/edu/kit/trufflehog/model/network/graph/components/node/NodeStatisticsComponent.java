package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent implements IComponent {

    private static final Logger logger = LogManager.getLogger(NodeStatisticsComponent.class);
    
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
    public String toString() {

        return name() + ": " + "Throughput=" + getThroughput();

    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {

        // TODO externalise this logic in the copy creator
        final IComponent copy = new NodeStatisticsComponent(getThroughput());

        //logger.debug("Deep copy created: " + copy.toString());
        return copy;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {



        return updater.update(this, instance);
    }
}
