package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

import java.time.Instant;

/**
 * \brief
 * \details
 * \date 23.02.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class EdgeStatisticsComponent extends AbstractComponent implements IComponent {

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
        Platform.runLater(() -> trafficProperty.set(value));
    }

    public void incrementTraffic(int step) {
        setTrafficProperty(getTraffic() + step);
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
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EdgeStatisticsComponent);
    }

}
