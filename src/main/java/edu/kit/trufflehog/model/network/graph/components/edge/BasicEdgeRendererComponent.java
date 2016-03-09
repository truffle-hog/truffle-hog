package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 *
 * TODO FIX GETTER!!!
 */
public class BasicEdgeRendererComponent implements IRendererComponent {


    Color colorUnpicked = new Color(0x7f7784);
    float[] hsbValsUnpicked = new float[3];


    Color colorPicked = new Color(0xf0caa3);
    float[] hsbValsPicked = new float[3];

    private Shape shape = new Ellipse2D.Float(-10, -10, 20, 20);

    //TODO change this!
    private Stroke stroke = new BasicStroke();

    private float stepSize = 0.1f;
    private float currentBrightness;

    public BasicEdgeRendererComponent() {

        Color.RGBtoHSB(colorUnpicked.getRed(), colorUnpicked.getGreen(), colorUnpicked.getBlue(), hsbValsUnpicked);
        Color.RGBtoHSB(colorPicked.getRed(), colorPicked.getGreen(), colorPicked.getBlue(), hsbValsPicked);

        currentBrightness = hsbValsUnpicked[2];
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public Color getColorUnpicked() {

        return new Color(Color.HSBtoRGB(hsbValsUnpicked[0], hsbValsUnpicked[1], currentBrightness));
    }

    @Override
    public Color getColorPicked() {
        // TODO implement
        return new Color(Color.HSBtoRGB(hsbValsPicked[0], hsbValsPicked[1], currentBrightness));
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.colorUnpicked = colorUnpicked;
        Color.RGBtoHSB(this.colorUnpicked.getRed(), this.colorUnpicked.getGreen(), this.colorUnpicked.getBlue(), hsbValsPicked);
    }

    @Override
    public void setShape(Shape shape) {
        if (shape == null) throw new NullPointerException("shape must not be null!");
        this.shape = shape;
    }

    @Override
    public void setStroke(Stroke stroke) {
        if (stroke == null) throw new NullPointerException("stroke must not be null!");
        this.stroke = stroke;
    }

    @Override
    public void updateState() {
        if (currentBrightness <= hsbValsPicked[2]) {
            currentBrightness = hsbValsPicked[2];
        } else {
            currentBrightness -= stepSize;
        }
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
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.colorPicked = colorPicked;

        Color.RGBtoHSB(this.colorPicked.getRed(), this.colorPicked.getGreen(), this.colorPicked.getBlue(), hsbValsPicked);
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BasicEdgeRendererComponent);
    }

    public float getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(float currentBrightness) {
        this.currentBrightness = currentBrightness;
    }
}
