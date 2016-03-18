package edu.kit.trufflehog.view.elements;

import javafx.scene.effect.Glow;

/***
 * <p>
 *     The GlowImageButton is an extension of the {@link ImageButton} that has to ability to glow.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class GlowImageButton extends ImageButton {
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
    }

    /**
     * <p>
     *     Makes the button glow until {@link #stopGlow} is called.
     * </p>
     */
    public void startGlow() {
        glow.setLevel(10.0);
    }

    /**
     * <p>
     *     Stops the button from glowing.
     * </p>
     */
    public void stopGlow() {
        glow.setLevel(0.0);
    }
}
