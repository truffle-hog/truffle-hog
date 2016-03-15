package edu.kit.trufflehog.view.elements;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

/***
 * <p>
 *     The GlowImageButton is an extension of the {@link ImageButton} that has to ability to glow.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class GlowImageButton extends ImageButton {
    private final Timeline timeline;
    private final Glow glow;


    /**
     * <p>
     *     Creates a new GlowImageButton.
     * </p>
     *
     * @param image The path to the image to display as the button.
     */
    public GlowImageButton(final String image) {
        super(image);

        glow = new Glow();
        glow.setLevel(0.0);
        setEffect(glow);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(glow.levelProperty(), 1);
        final KeyFrame kf = new KeyFrame(Duration.millis(700), kv);
        timeline.getKeyFrames().add(kf);
    }

    /**
     * <p>
     *     Makes the button glow until {@link #stopGlow} is called.
     * </p>
     */
    public void startGlow() {
        timeline.play();
    }

    /**
     * <p>
     *     Stops the button from glowing.
     * </p>
     */
    public void stopGlow() {
        timeline.stop();
        glow.setLevel(0.0);
    }
}
