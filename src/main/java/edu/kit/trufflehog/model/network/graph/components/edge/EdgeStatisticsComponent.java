package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

import java.time.Instant;

/**
 * Created by jan on 23.02.16.
 */
public class EdgeStatisticsComponent implements IComponent {

    private final IntegerProperty trafficProperty = new SimpleIntegerProperty(1);
    private final LongProperty lastUpdateTime = new SimpleLongProperty(Instant.now().toEpochMilli());

    public EdgeStatisticsComponent(int initial) {


        trafficProperty.set(initial);
    }

    public IntegerProperty getTrafficProperty() {
        return trafficProperty;
    }

    public int getTraffic() {
        return trafficProperty.get();
    }

    public void setTrafficProperty(int value) {
        trafficProperty.set(value);
    }

    public void incrementTraffic(int step) {
        setTrafficProperty(getTraffic() + step);
    }

    @Override
    public String name() {
        return "traffic info";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime.get();
    }

    public LongProperty lastUpdateTimeProperty() {
        return lastUpdateTime;
    }

    public void setLastUpdateTimeProperty(long value) {
        lastUpdateTimeProperty().setValue(value);
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        return updater.update(this, instance);
    }
}
