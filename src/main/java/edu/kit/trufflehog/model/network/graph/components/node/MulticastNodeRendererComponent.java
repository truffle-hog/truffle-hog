package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.*;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class MulticastNodeRendererComponent implements IComponent {


    public MulticastNodeRendererComponent() {

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
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        return null;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        return false;
    }
}
