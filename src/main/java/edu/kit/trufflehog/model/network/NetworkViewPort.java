/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.CircleLayoutFactory;
import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.recording.NetworkViewCopy;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import javafx.beans.property.*;
import org.apache.commons.collections15.Transformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.Collection;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 * @author Jan Hermes
 * @version 0.0.1
 */
public class NetworkViewPort implements INetworkViewPort {

    private static final Logger logger = LogManager.getLogger(NetworkViewPort.class);

    private Layout<INode, IConnection> delegate;
    private final ObservableUpdatableGraph<INode, IConnection> graphDelegate;
    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);
    private final LongProperty viewTimeProperty = new SimpleLongProperty(Instant.now().toEpochMilli());
    private final IntegerProperty populationProperty = new SimpleIntegerProperty(0);
    private final DoubleProperty throughput = new SimpleDoubleProperty(0);

    public NetworkViewPort(final ObservableUpdatableGraph<INode, IConnection> delegate) {

        this.graphDelegate = delegate;

        this.delegate = new FRLayout<>(this.graphDelegate);
        this.layoutFactory = new FRLayoutFactory();

    }

    @Override
    public void initialize() {
        delegate.initialize();
    }

    @Override
    public void setInitializer(Transformer<INode, Point2D> initializer) {
        delegate.setInitializer(initializer);
    }

    @Override
    public void setGraph(Graph<INode, IConnection> graph) {
        delegate.setGraph(graph);
    }

    @Override
    public Graph<INode, IConnection> getGraph() {
        return delegate.getGraph();
    }

    @Override
    public void reset() {
        delegate.reset();
    }

    @Override
    public void setSize(Dimension d) {
        delegate.setSize(d);
    }

    @Override
    public Dimension getSize() {
        return delegate.getSize();
    }

    @Override
    public void lock(INode iNode, boolean state) {
        delegate.lock(iNode, state);
    }

    @Override
    public boolean isLocked(INode iNode) {
        return delegate.isLocked(iNode);
    }

    @Override
    public void setLocation(INode iNode, Point2D location) {
        delegate.setLocation(iNode, location);
    }

    @Override
    public Point2D transform(INode iNode) {
        return delegate.transform(iNode);
    }


    @Override
    public int getMaxConnectionSize() {
        return maxConnectionSizeProperty.get();
    }

    @Override
    public void setMaxConnectionSize(int size) {

        maxConnectionSizeProperty.set(size);

    }

    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return maxConnectionSizeProperty;
    }

    @Override
    public int getMaxThroughput() {
        return maxThroughputProperty.get();
    }

    @Override
    public void setMaxThroughput(int size) {

        maxThroughputProperty.set(size);

    }

    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return maxThroughputProperty;
    }

    @Override
    public long getViewTime() {
        return viewTimeProperty.get();
    }

    @Override
    public void setViewTime(long time) {
        viewTimeProperty.set(time);
    }

    @Override
    public LongProperty getViewTimeProperty() {
        return viewTimeProperty;
    }

    @Override
    public void setPopulation(int value) {
        populationProperty.setValue(value);
    }

    @Override
    public int getPopulation() {
        return populationProperty.get();
    }

    @Override
    public IntegerProperty getPopulationProperty() {
        return populationProperty;
    }

    @Override
    public void setThroughput(double value) {
        throughput.setValue(value);
    }

    @Override
    public double getThroughput() {
        return throughput.getValue();
    }

    @Override
    public DoubleProperty getThroughputProperty() {
        return throughput;
    }

    @Override
    public void refreshLayout() {

        delegate = layoutFactory.transform(delegate.getGraph());
    }

    @Override
    public void setLayoutFactory(Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory) {

        this.layoutFactory = layoutFactory;
    }

    @Override
    public void graphIntersection(Graph<INode, IConnection> graph) {

        throw new UnsupportedOperationException("A live graph does not support this operation");
    }

    @Override
    public void graphIntersection(Collection<INode> vertices, Collection<IConnection> edges) {
        throw new UnsupportedOperationException("A live graph does not support this operation");
    }

    @Override
    public void addGraphEventListener(GraphEventListener<INode, IConnection> l) {
        this.graphDelegate.addGraphEventListener(l);
    }

    @Override
    public void removeGraphEventListener(GraphEventListener<INode, IConnection> l) {
        this.graphDelegate.removeGraphEventListener(l);
    }

    @Override
    public NetworkViewCopy createDeepCopy(ICopyCreator copyCreator) {
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return false;
    }


}
