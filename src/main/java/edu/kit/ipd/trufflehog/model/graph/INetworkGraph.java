package edu.kit.ipd.trufflehog.model.graph;

public interface INetworkGraph {

	public abstract void addNetworkNode(NetworkNode node);

	public abstract void addNetworkEdge(NetworkNode from, NetworkNode to);

}
