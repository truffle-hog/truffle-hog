package edu.kit.trufflehog.model.network.graph.jungconcurrent;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.collections15.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of <code>DirectedGraph</code> suitable for sparse graphs.
 */
@SuppressWarnings("serial")
public class ConcurrentDirectedSparseGraph<V,E> extends AbstractTypedGraph<V, E> implements
        DirectedGraph<V, E>
{
    private static final Logger logger = LogManager.getLogger(ConcurrentDirectedSparseGraph.class);
    
    /**
     * Returns a {@code Factory} that creates an instance of this graph type.
     * @param <V> the vertex type for the graph factory
     * @param <E> the edge type for the graph factory
     */
    public static final <V,E> Factory<DirectedGraph<V,E>> getFactory() {
        return new Factory<DirectedGraph<V,E>> () {
            public DirectedGraph<V,E> create() {
                return new DirectedSparseGraph<V,E>();
            }
        };
    }

    
    
    protected Map<V, Pair<Map<V,E>>> vertices;  // Map of vertices to Pair of adjacency maps {incoming, outgoing}
    // of neighboring vertices to incident edges
    protected Map<E, Pair<V>> edges;            // Map of edges to incident vertex pairs

    /**
     * Creates an instance.
     */
    public ConcurrentDirectedSparseGraph()
    {
        super(EdgeType.DIRECTED);
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    @Override
    public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType)
    {
        this.validateEdgeType(edgeType);
        Pair<V> new_endpoints = getValidatedEndpoints(edge, endpoints);
        if (new_endpoints == null)
            return false;

        V source = new_endpoints.getFirst();
        V dest = new_endpoints.getSecond();

        if (findEdge(source, dest) != null) {

            //vertices.get(source).getSecond().put(dest, edge);

            return false;

        }

        edges.put(edge, new_endpoints);

        if (!vertices.containsKey(source))
            this.addVertex(source);

        if (!vertices.containsKey(dest))
            this.addVertex(dest);

        // map source of this edge to <dest, edge> and vice versa
        vertices.get(source).getSecond().put(dest, edge);
        vertices.get(dest).getFirst().put(source, edge);

        return true;
    }

    @Override
    public E findEdge(V v1, V v2)
    {
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;
        return vertices.get(v1).getSecond().get(v2);
    }

    @Override
    public Collection<E> findEdgeSet(V v1, V v2)
    {
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;
        ArrayList<E> edge_collection = new ArrayList<E>(1);
        E e = findEdge(v1, v2);
        if (e == null)
            return edge_collection;
        edge_collection.add(e);
        return edge_collection;
    }

    protected Collection<E> getIncoming_internal(V vertex)
    {
        return vertices.get(vertex).getFirst().values();
    }

    protected Collection<E> getOutgoing_internal(V vertex)
    {
        return vertices.get(vertex).getSecond().values();
    }

    protected Collection<V> getPreds_internal(V vertex)
    {
        return vertices.get(vertex).getFirst().keySet();
    }

    protected Collection<V> getSuccs_internal(V vertex)
    {
        return vertices.get(vertex).getSecond().keySet();
    }

    public Collection<E> getInEdges(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(getIncoming_internal(vertex));
    }

    public Collection<E> getOutEdges(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(getOutgoing_internal(vertex));
    }

    public Collection<V> getPredecessors(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(getPreds_internal(vertex));
    }

    public Collection<V> getSuccessors(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(getSuccs_internal(vertex));
    }

    public Pair<V> getEndpoints(E edge)
    {
        if (!containsEdge(edge))
            return null;
        return edges.get(edge);
    }

    public V getSource(E directed_edge)
    {
        if (!containsEdge(directed_edge))
            return null;
        return edges.get(directed_edge).getFirst();
    }

    public V getDest(E directed_edge)
    {
        if (!containsEdge(directed_edge))
            return null;
        return edges.get(directed_edge).getSecond();
    }

    public boolean isSource(V vertex, E edge)
    {
        if (!containsEdge(edge) || !containsVertex(vertex))
            return false;
        return vertex.equals(this.getEndpoints(edge).getFirst());
    }

    public boolean isDest(V vertex, E edge)
    {
        if (!containsEdge(edge) || !containsVertex(vertex))
            return false;
        return vertex.equals(this.getEndpoints(edge).getSecond());
    }

    public Collection<E> getEdges()
    {
        return Collections.unmodifiableCollection(edges.keySet());
    }

    public Collection<V> getVertices()
    {
        return Collections.unmodifiableCollection(vertices.keySet());
    }

    public boolean containsVertex(V vertex)
    {
        return vertices.containsKey(vertex);
    }

    public boolean containsEdge(E edge)
    {
        return edges.containsKey(edge);
    }

    public int getEdgeCount()
    {
        return edges.size();
    }

    public int getVertexCount()
    {
        return vertices.size();
    }

    public Collection<V> getNeighbors(V vertex)
    {
        if (!containsVertex(vertex))
            return null;

        Collection<V> neighbors = new HashSet<V>();
        neighbors.addAll(getPreds_internal(vertex));
        neighbors.addAll(getSuccs_internal(vertex));
        return Collections.unmodifiableCollection(neighbors);
    }

    public Collection<E> getIncidentEdges(V vertex)
    {
        if (!containsVertex(vertex))
            return null;

        Collection<E> incident_edges = new HashSet<E>();
        incident_edges.addAll(getIncoming_internal(vertex));
        incident_edges.addAll(getOutgoing_internal(vertex));
        return Collections.unmodifiableCollection(incident_edges);
    }

    public boolean addVertex(V vertex)
    {
        if(vertex == null) {
            throw new IllegalArgumentException("vertex may not be null");
        }
        if (!containsVertex(vertex)) {
            vertices.put(vertex, new Pair<>(new HashMap<>(), new HashMap<>()));
            return true;
        } else {

            //final Pair<Map<V,E>> existing = vertices.get(vertex);
            //logger.debug("vertex allready exists: " + existing.toString() + "\nupdating with: " + vertex.toString());

            //vertices.put(vertex, existing);
            return false;
        }
    }

    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex))
            return false;

        // copy to avoid concurrent modification in removeEdge
        ArrayList<E> incident = new ArrayList<E>(getIncoming_internal(vertex));
        incident.addAll(getOutgoing_internal(vertex));

        for (E edge : incident)
            removeEdge(edge);

        vertices.remove(vertex);

        return true;
    }

    public boolean removeEdge(E edge) {
        if (!containsEdge(edge))
            return false;

        Pair<V> endpoints = this.getEndpoints(edge);
        V source = endpoints.getFirst();
        V dest = endpoints.getSecond();

        // remove vertices from each others' adjacency maps
        vertices.get(source).getSecond().remove(dest);
        vertices.get(dest).getFirst().remove(source);

        edges.remove(edge);
        return true;
    }
}
