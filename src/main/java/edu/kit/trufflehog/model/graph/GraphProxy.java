package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.Collection;

/**
 * <p>
 *     Graph deputy used to encapsulate the graph and thus make it interchangeable without having to update graph
 *     references of other objects.
 * </p>
 */
public class GraphProxy implements INetworkGraph, IGraphProxy {

    private INetworkGraph activeGraph;

    public GraphProxy(INetworkGraph initial) {

        activeGraph = initial;
    }

    @Override
    public INode getNetworkNodeByMACAddress(long macAddress) {
        return activeGraph.getNetworkNodeByMACAddress(macAddress);
    }

    @Override
    public INode getNetworkNodeByIPAddress(int ipAddress) {
        return activeGraph.getNetworkNodeByIPAddress(ipAddress);
    }

    @Override
    public INode getNetworkNodeByDeviceName(String deviceName) {
        return activeGraph.getNetworkNodeByDeviceName(deviceName);
    }

    @Override
    public INode getNetworkEdge(INode src, INode dest) {
        return activeGraph.getNetworkEdge(src, dest);
    }

    @Override
    public void addNetworkEdge(IConnection connection, INode from, INode to) {
        activeGraph.addNetworkEdge(connection, from, to);
    }

    @Override
    public void addNetworkNode(INode node) {

        activeGraph.addNetworkNode(node);
    }

    @Override
    public Collection<IConnection> getEdges() {
        return activeGraph.getEdges();
    }

    @Override
    public Collection<INode> getVertices() {
        return activeGraph.getVertices();
    }

    @Override
    public boolean containsVertex(INode vertex) {
        return activeGraph.containsVertex(vertex);
    }

    @Override
    public boolean containsEdge(IConnection edge) {
        return activeGraph.containsEdge(edge);
    }

    @Override
    public int getEdgeCount() {
        return activeGraph.getEdgeCount();
    }

    @Override
    public int getVertexCount() {
        return activeGraph.getVertexCount();
    }

    @Override
    public Collection<INode> getNeighbors(INode vertex) {
        return activeGraph.getNeighbors(vertex);
    }

    @Override
    public Collection<IConnection> getIncidentEdges(INode vertex) {
        return activeGraph.getIncidentEdges(vertex);
    }

    @Override
    public Collection<INode> getIncidentVertices(IConnection edge) {
        return activeGraph.getIncidentVertices(edge);
    }

    @Override
    public IConnection findEdge(INode v1, INode v2) {
        return activeGraph.findEdge(v1, v2);
    }

    @Override
    public Collection<IConnection> findEdgeSet(INode v1, INode v2) {
        return activeGraph.findEdgeSet(v1, v2);
    }

    @Override
    public boolean addVertex(INode vertex) {
        return activeGraph.addVertex(vertex);
    }

    @Override
    public boolean addEdge(IConnection edge, Collection<? extends INode> vertices) {
        return activeGraph.addEdge(edge, vertices);
    }

    @Override
    public boolean addEdge(IConnection edge, Collection<? extends INode> vertices, EdgeType edge_type) {
        return activeGraph.addEdge(edge, vertices, edge_type);
    }

    @Override
    public boolean removeVertex(INode vertex) {
        return activeGraph.removeVertex(vertex);
    }

    @Override
    public boolean removeEdge(IConnection edge) {
        return activeGraph.removeEdge(edge);
    }

    @Override
    public boolean isNeighbor(INode v1, INode v2) {
        return activeGraph.isNeighbor(v1, v2);
    }

    @Override
    public boolean isIncident(INode vertex, IConnection edge) {
        return activeGraph.isIncident(vertex, edge);
    }

    @Override
    public int degree(INode vertex) {
        return activeGraph.degree(vertex);
    }

    @Override
    public int getNeighborCount(INode vertex) {
        return activeGraph.getNeighborCount(vertex);
    }

    @Override
    public int getIncidentCount(IConnection edge) {
        return activeGraph.getIncidentCount(edge);
    }

    @Override
    public EdgeType getEdgeType(IConnection edge) {
        return activeGraph.getEdgeType(edge);
    }

    @Override
    public EdgeType getDefaultEdgeType() {
        return activeGraph.getDefaultEdgeType();
    }

    @Override
    public Collection<IConnection> getEdges(EdgeType edge_type) {
        return activeGraph.getEdges(edge_type);
    }

    @Override
    public int getEdgeCount(EdgeType edge_type) {
        return activeGraph.getEdgeCount(edge_type);
    }

    @Override
    public Collection<IConnection> getInEdges(INode vertex) {
        return activeGraph.getInEdges(vertex);
    }

    @Override
    public Collection<IConnection> getOutEdges(INode vertex) {
        return activeGraph.getOutEdges(vertex);
    }

    @Override
    public Collection<INode> getPredecessors(INode vertex) {
        return activeGraph.getPredecessors(vertex);
    }

    @Override
    public Collection<INode> getSuccessors(INode vertex) {
        return activeGraph.getSuccessors(vertex);
    }

    @Override
    public int inDegree(INode vertex) {
        return activeGraph.inDegree(vertex);
    }

    @Override
    public int outDegree(INode vertex) {
        return activeGraph.outDegree(vertex);
    }

    @Override
    public boolean isPredecessor(INode v1, INode v2) {
        return activeGraph.isPredecessor(v1, v2);
    }

    @Override
    public boolean isSuccessor(INode v1, INode v2) {
        return activeGraph.isSuccessor(v1, v2);
    }

    @Override
    public int getPredecessorCount(INode vertex) {
        return activeGraph.getPredecessorCount(vertex);
    }

    @Override
    public int getSuccessorCount(INode vertex) {
        return activeGraph.getSuccessorCount(vertex);
    }

    @Override
    public INode getSource(IConnection directed_edge) {
        return activeGraph.getSource(directed_edge);
    }

    @Override
    public INode getDest(IConnection directed_edge) {
        return activeGraph.getDest(directed_edge);
    }

    @Override
    public boolean isSource(INode vertex, IConnection edge) {
        return activeGraph.isSource(vertex, edge);
    }

    @Override
    public boolean isDest(INode vertex, IConnection edge) {
        return activeGraph.isDest(vertex, edge);
    }

    @Override
    public boolean addEdge(IConnection iConnection, INode v1, INode v2) {
        return activeGraph.addEdge(iConnection, v1, v2);
    }

    @Override
    public boolean addEdge(IConnection iConnection, INode v1, INode v2, EdgeType edgeType) {
        return activeGraph.addEdge(iConnection, v1, v2, edgeType);
    }

    @Override
    public Pair<INode> getEndpoints(IConnection edge) {
        return activeGraph.getEndpoints(edge);
    }

    @Override
    public INode getOpposite(INode vertex, IConnection edge) {
        return activeGraph.getOpposite(vertex, edge);
    }

    @Override
    public void setActive(INetworkGraph graph) {
        this.activeGraph = graph;
    }
}