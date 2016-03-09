package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.*;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class BasicEdgeRendererComponent implements IRendererComponent {


    Color colorUnpicked = new Color(0x7f7784);
    float[] hsbValsUnpicked = new float[3];


    Color colorPicked = new Color(0xf0caa3);
    float[] hsbValsPicked = new float[3];


    private float stepSize = 0.1f;
    private float currentBrightness;

    public BasicEdgeRendererComponent() {

        Color.RGBtoHSB(colorUnpicked.getRed(), colorUnpicked.getGreen(), colorUnpicked.getBlue(), hsbValsUnpicked);
        Color.RGBtoHSB(colorPicked.getRed(), colorPicked.getGreen(), colorPicked.getBlue(), hsbValsPicked);

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
    public void setColorUnpicked(Color colorUnpicked) {
        this.colorUnpicked = colorUnpicked;
        Color.RGBtoHSB(this.colorUnpicked.getRed(), this.colorUnpicked.getGreen(), this.colorUnpicked.getBlue(), hsbValsPicked);
    }

    @Override
    public void setShape(Shape shape) {

    }

    @Override
    public void setStroke(Stroke stroke) {

    }


    @Override
    public String name() {
        return "Node Renderer";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    public void setColorPicked(Color colorPicked) {
        this.colorPicked = colorPicked;


        Color.RGBtoHSB(this.colorPicked.getRed(), this.colorPicked.getGreen(), this.colorPicked.getBlue(), hsbValsPicked);
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        return updater.update(this, instance);
    }

    public float getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(float currentBrightness) {
        this.currentBrightness = currentBrightness;
    }
}
