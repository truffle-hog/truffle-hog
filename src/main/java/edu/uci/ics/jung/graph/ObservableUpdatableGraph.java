package edu.uci.ics.jung.graph;

import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A decorator class for graphs which generates events 
 * 
 * @author Joshua O'Madadhain
 */
@SuppressWarnings("serial")
public class ObservableUpdatableGraph<V,E> extends GraphDecorator<V,E> {

	private final List<GraphEventListener<V,E>> listenerList =
		Collections.synchronizedList(new LinkedList<GraphEventListener<V,E>>());


    private final Map<V,V> existingVertices = new ConcurrentHashMap<>();
    private final Map<E,E> existingEdges = new ConcurrentHashMap<>();

	private final GraphUpdater<V, E> graphUpdater;

    /**
     * Creates a new instance based on the provided {@code delegate}.
     */
	public ObservableUpdatableGraph(Graph<V, E> delegate, GraphUpdater<V, E> updater) {
		super(delegate);

		this.graphUpdater = updater;
	}

	/**
	 * Adds {@code l} as a listener to this graph.
	 */
	public void addGraphEventListener(GraphEventListener<V,E> l) {
		listenerList.add(l);
	}

    /**
     * Removes {@code l} as a listener to this graph.
     */
	public void removeGraphEventListener(GraphEventListener<V,E> l) {
		listenerList.remove(l);
	}

	protected void fireGraphEvent(GraphEvent<V,E> evt) {

		for(GraphEventListener<V,E> listener : listenerList) {
			listener.handleGraphEvent(evt);
		 }
	 }


	private boolean updateEdge(E existingEdge, E newEdge) {

		if (graphUpdater.updateEdge(existingEdge, newEdge)) {

			fireGraphEvent(new GraphEvent.Edge<>(delegate, GraphEvent.Type.EDGE_CHANGED, existingEdge));
		}
        return false;
	}

	private boolean updateVertex(V existingVertex, V newVertex) {

		if (graphUpdater.updateVertex(existingVertex, newVertex)) {

			fireGraphEvent(new GraphEvent.Vertex<>(delegate, GraphEvent.Type.VERTEX_CHANGED, existingVertex));
		}
        return false;
	}

	/**
	 * @see Hypergraph#addEdge(Object, Collection)
	 */
	@Override
	public boolean addEdge(E edge, Collection<? extends V> vertices) {
		boolean state = super.addEdge(edge, vertices);

		if (state) {

            existingEdges.put(edge, edge);

			final GraphEvent<V,E> evt = new GraphEvent.Edge<>(delegate, GraphEvent.Type.EDGE_ADDED, edge);
			fireGraphEvent(evt);
            return true;

		} else {

			return updateEdge(existingEdges.get(edge), edge);
		}
	}

	/**
	 * @see Graph#addEdge(Object, Object, Object, EdgeType)
	 */
	@Override
  public boolean addEdge(E e, V v1, V v2, EdgeType edgeType) {

		boolean state = super.addEdge(e, v1, v2, edgeType);
		if (state) {

            existingEdges.put(e, e);

			GraphEvent<V,E> evt = new GraphEvent.Edge<V,E>(delegate, GraphEvent.Type.EDGE_ADDED, e);
			fireGraphEvent(evt);

		} else {

			return updateEdge(existingEdges.get(e), e);
		}
		return state;
	}

	/**
	 * @see Graph#addEdge(Object, Object, Object)
	 */
	@Override
  public boolean addEdge(E e, V v1, V v2) {
		boolean state = super.addEdge(e, v1, v2);

		if(state) {

            existingEdges.put(e, e);

			GraphEvent<V,E> evt = new GraphEvent.Edge<V,E>(delegate, GraphEvent.Type.EDGE_ADDED, e);
			fireGraphEvent(evt);
			return true;

		}  else {

			return updateEdge(existingEdges.get(e), e);
		}
	}

	/**
	 * @see Hypergraph#addVertex(Object)
	 */
	@Override
  public boolean addVertex(V vertex) {
		final boolean state = super.addVertex(vertex);

		if(state) {

            existingVertices.put(vertex, vertex);

			GraphEvent<V,E> evt = new GraphEvent.Vertex<V,E>(delegate, GraphEvent.Type.VERTEX_ADDED, vertex);
			fireGraphEvent(evt);
			return true;

		}  else {

			return updateVertex(existingVertices.get(vertex), vertex);
		}
	}

	/**
	 * @see Hypergraph#removeEdge(Object)
	 */
	@Override
  public boolean removeEdge(E edge) {
		boolean state = delegate.removeEdge(edge);
		if(state) {

            existingEdges.remove(edge);

			GraphEvent<V,E> evt = new GraphEvent.Edge<V,E>(delegate, GraphEvent.Type.EDGE_REMOVED, edge);
			fireGraphEvent(evt);
			return true;
		}
		return state;
	}

	/**
	 * @see Hypergraph#removeVertex(Object)
	 */
	@Override
	public boolean removeVertex(V vertex) {

		// remove all incident edges first, so that the appropriate events will
		// be fired (otherwise they'll be removed inside {@code delegate.removeVertex}
		// and the events will not be fired)
		Collection<E> incident_edges = new ArrayList<E>(delegate.getIncidentEdges(vertex));
		for (E e : incident_edges) 
			this.removeEdge(e);
		
		boolean state = delegate.removeVertex(vertex);

		if(state) {
            existingVertices.remove(vertex);
			GraphEvent<V,E> evt = new GraphEvent.Vertex<V,E>(delegate, GraphEvent.Type.VERTEX_REMOVED, vertex);
			fireGraphEvent(evt);
		}
		return state;
	}

}
