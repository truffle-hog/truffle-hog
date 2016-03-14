package edu.kit.trufflehog.view.elements;

import javafx.scene.text.Text;

import java.time.Instant;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     The TimerField is a stoo watch counting upwards wrapped in a text field that automatically updates itself.
 * </p>
 */
public class TimerField extends Text {
    private Instant start;
    private ScheduledExecutorService executorService;
    private Future<?> future;

    /**
     * <p>
     *     Creates a new TimerField
     * </p>
     *
     * @param executorService The executorService used to launch the time calculation thread.
     */
    public TimerField(ScheduledExecutorService executorService) {
        this.executorService = executorService;
        setText("00:00:00.00");
    }

    /**
     * <p>
     *     Sets the label to the HH:mm:ss.SS format based on the milliseconds that it was passed.
     * </p>
     *
     * @param millis The milliseconds as a long that should be displayed as HH:mm:ss.SS in the text field.
     */
    private void update(long millis) {
        // We always want the second and third digits from the right (tenth and hundredth of a second) thus we parse
        // the string here accordingly
        String millisString  = TimeUnit.MILLISECONDS.toMillis(millis) + "";
        if (millisString.length() >= 3) {
            millisString = millisString.substring(millisString.length() - 3, millisString.length() - 1);
        }
        long millisShort = Long.parseLong(millisString);

        // Actually do the conversion here
        String value = String.format("%02d:%02d:%02d.%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                TimeUnit.MILLISECONDS.toMillis(millisShort));
        setText(value);
    }

    /**
     * <p>
     *     Starts the timer.
     * </p>
     */
    public void startTimer() {
        start = Instant.now();
        future = executorService.scheduleAtFixedRate(() -> {
            update(Instant.now().toEpochMilli() - start.toEpochMilli());
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    /**
     * <p>
     *     Stops the timer.
     * </p>
     */
    public void stopTimer() {
        future.cancel(true);
    }
}