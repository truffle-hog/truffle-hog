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

import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentFRLayout;
import edu.kit.trufflehog.model.network.recording.NetworkViewCopy;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
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


    private Layout<INode, IConnection> delegate;
    private Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> layoutFactory;

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);

    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

    private final LongProperty viewTimeProperty = new SimpleLongProperty(Instant.now().toEpochMilli());

    public NetworkViewPort(final Graph<INode, IConnection> delegate) {

        this.delegate = new ConcurrentFRLayout<>(delegate);
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
    public void refreshLayout() {

        delegate = layoutFactory.transform(getGraph());
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
    public NetworkViewCopy createDeepCopy(ICopyCreator copyCreator) {
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return false;
    }
}
