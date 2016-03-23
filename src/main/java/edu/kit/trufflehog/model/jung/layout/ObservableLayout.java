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
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.model.jung.layout;

import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.LayoutDecorator;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import edu.uci.ics.jung.graph.event.GraphEventListener;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * \details
 * \date 21.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class ObservableLayout<V extends IComposition, E extends IComposition> extends LayoutDecorator<V, E> {

    private final List<GraphEventListener<V,E>> listenerList =
            Collections.synchronizedList(new LinkedList<GraphEventListener<V,E>>());

    private final ObservableUpdatableGraph<V, E> observableGraph;
    /**
     * Creates a new instance based on the provided {@code delegate}.
     */
    public ObservableLayout(Layout<V, E> delegate) {
        super(delegate);

        if (!(delegate.getGraph() instanceof ObservableUpdatableGraph)) {
            throw new IllegalArgumentException("layouts graph has to be an obersable implementation");
        }

        this.observableGraph = (ObservableUpdatableGraph<V, E>) delegate.getGraph();

    }

    public ObservableUpdatableGraph<V, E> getObservableGraph() {

        return observableGraph;
    }

    /**
     * Adds {@code l} as a listener to this layout.
     */
    public void addGraphEventListener(GraphEventListener<V,E> l) {
        listenerList.add(l);
    }

    /**
     * Removes {@code l} as a listener to this layout.
     */
    public void removeGraphEventListener(GraphEventListener<V,E> l) {
        listenerList.remove(l);
    }

    protected void fireLayoutEvent(LayoutEvent<V,E> evt) {

        //     for(GraphEventListener<V,E> listener : listenerList) {
        //        listener.handleGraphEvent(evt);
        //    }
    }

    @Override
    public void setLocation(V vertex, Point2D position) {

        // GraphEvent<V,E> evt = new GraphEvent.Vertex<V,E>(delegate, GraphEvent.Type.VERTEX_REMOVED, vertex);
        //  fireLayoutEvent(evt);

    }

    @Override
    public Point2D transform(V input) {
        return super.transform(input);
    }
}

