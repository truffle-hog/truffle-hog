package edu.kit.trufflehog.model.network.graph.components.edge;

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
public class BasicEdgeRendererComponent implements IRendererComponent {


    Color baseUnpicked = new Color(0x7f7784);
    float[] hsbValsUnpicked = new float[3];


    Color basePicked = new Color(0xf0caa3);
    float[] hsbValsPicked = new float[3];


    private float stepSize = 0.1f;
    private float currentBrightness;

    public BasicEdgeRendererComponent() {

        Color.RGBtoHSB(baseUnpicked.getRed(), baseUnpicked.getGreen(), baseUnpicked.getBlue(), hsbValsUnpicked);
        Color.RGBtoHSB(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), hsbValsPicked);

        currentBrightness = hsbValsUnpicked[2];
    }

    @Override
    public Shape getShape() {

        //TODO implement
        return null;
    }

    @Override
    public Color getColorUnpicked() {

        if (currentBrightness <= hsbValsUnpicked[2]) {
            currentBrightness = hsbValsUnpicked[2];
        } else {
            currentBrightness -= stepSize;
        }

        return new Color(Color.HSBtoRGB(hsbValsUnpicked[0], hsbValsUnpicked[1], currentBrightness));
    }

    @Override
    public Color getColorPicked() {

        if (currentBrightness <= hsbValsPicked[2]) {
            currentBrightness = hsbValsPicked[2];
        } else {
            currentBrightness -= stepSize;
        }
        // TODO implement
        return new Color(Color.HSBtoRGB(hsbValsPicked[0], hsbValsPicked[1], currentBrightness));
    }

    @Override
    public Stroke getStroke() {
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

        if (currentBrightness > 0.7) {
            return true;
        }
        // TODO implement more
        currentBrightness = 1;
        return true;
    }
}
