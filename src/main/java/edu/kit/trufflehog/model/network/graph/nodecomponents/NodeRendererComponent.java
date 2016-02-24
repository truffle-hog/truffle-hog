package edu.kit.trufflehog.model.network.graph.nodecomponents;

import edu.kit.trufflehog.model.network.graph.IComponent;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class NodeRendererComponent implements IComponent {


    public NodeRendererComponent() {

        // TODO implement
    }


    @Override
    public String name() {
        return "Node Renderer";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public IComponent createDeepCopy() {
        return null;
    }

    @Override
    public boolean update(IComponent update) {
        return false;
    }
}