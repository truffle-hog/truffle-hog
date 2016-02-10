package edu.kit.trufflehog.service.replaylogging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     A ScheduledThreadPoolExecutor that logs exceptions and does not just drop them.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class LoggedScheduledExecutor extends ScheduledThreadPoolExecutor {
    private static final Logger logger = LogManager.getLogger(LoggedScheduledExecutor.class);

    /**
     * <p>
     *     Creates a new LoggedScheduledExecutor object.
     * </p>
     *
     * @param corePoolSize The core pool size of the executor, for more info see the oracle documentation for
     *                     ScheduledThreadPoolExecutor
     */
    public LoggedScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    @Override
    public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }

    /**
     * <p>
     *      Wraps the runnable in a LogOnExceptionRunnable so that if it fails, the exception is logged.
     * </p>
     *
     * @param runnable The runnable to wrap.
     * @return The wrapped runnable.
     */
    private Runnable wrapRunnable(Runnable runnable) {
        return new LogOnExceptionRunnable(runnable);
    }

    /**
     * <p>
     *     A wrapper class for a runnable. This logs the crash of a runnable if it does crash.
     * </p>
     */
    private class LogOnExceptionRunnable implements Runnable {
        private Runnable runnable;

        /**
         * <p>
         *      Creates a new LogOnExceptionRunnable object based on a previous {@link Runnable} object.
         * </p>
         *
         * @param runnable The {@link Runnable} to create the LogOnExceptionRunnable object from
         */
        public LogOnExceptionRunnable(Runnable runnable) {
            super();
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                // Log exception here
                logger.error("Error in executing: " + runnable + " - It will no longer be run!", e);

                // Throw error again so that it can continue its expected behavior
                throw new RuntimeException(e);
            }
        }
    }
}