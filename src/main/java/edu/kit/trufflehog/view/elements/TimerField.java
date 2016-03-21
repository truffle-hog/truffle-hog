package edu.kit.trufflehog.view.elements;

import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     The TimerField is a stop watch counting upwards wrapped in a text field that automatically updates itself.
 * </p>
 */
public class TimerField extends Text {
    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Instant startTime;
    private Instant endTime;
    private final StringProperty timeString;
    private final ScheduledExecutorService executorService;
    private Future<?> future;

    /**
     * <p>
     *     Creates a new TimerField.
     * </p>
     */
    public TimerField() {
        timeString = new SimpleStringProperty("00:00:00");
        textProperty().bind(timeString);
        executorService = LoggedScheduledExecutor.getInstance();
    }

    /**
     * <p>
     *     Starts the timer.
     * </p>
     */
    public void startTimer() {
        startTime = Instant.now();
        endTime = null;

        future = executorService.scheduleAtFixedRate(() -> timeString.setValue(LocalTime.now()
                .minus(Duration.ofMillis(startTime.toEpochMilli())).minus(Duration.ofHours(1))
                .format(SHORT_TIME_FORMATTER)), 0, 1, TimeUnit.SECONDS);
    }

    /**
     * <p>
     *     Stops the timer.
     * </p>
     */
    public void stopTimer() {
        endTime = Instant.now();
        future.cancel(true);
    }

    /**
     * <p>
     *     Gets the duration of the last recording in milliseconds. If a recording is currently in action, or if none
     *     has happened yet, -1 is returned instead.
     * </p>
     *
     * @return The duration of the last recording in milliseconds, or -1 if a recording is currently in action or has
     *         not occurred yet.
     */
    public long getRecordDuration() {
        if (endTime != null) {
            return endTime.toEpochMilli() - startTime.toEpochMilli();
        }

        return  -1;
    }
}