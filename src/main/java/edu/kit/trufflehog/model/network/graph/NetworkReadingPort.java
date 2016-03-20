package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

import java.util.Collection;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkReadingPort implements INetworkReadingPort {


    @Override
    public Collection<INode> getNetworkNodes() {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public Collection<IConnection> getNetworkConnections() {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public INode getNetworkNodeByAddress(IAddress address) {
        return null;
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        return null;
    }

    @Override
    public int getMaxConnectionSize() {
        return 0;
    }


    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return null;
    }

    @Override
    public int getMaxThroughput() {
        return 0;
    }


    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return null;
    }

    @Override
    public void setPopulation(int value) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public int getPopulation() {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public IntegerProperty getPopulationProperty() {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public void setThroughput(double value) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public double getThroughput() {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public DoubleProperty getThroughputProperty() {
        throw new UnsupportedOperationException("not implemented yet!");
    }
}
