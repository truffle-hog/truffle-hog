package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;

import java.awt.*;

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

    public Shape getShape() {

        //TODO implement
        return null;
    }

    public Color getColor() {

        // TODO implement
        return null;
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
    public boolean update(INode update) {
        return false;
    }
}
