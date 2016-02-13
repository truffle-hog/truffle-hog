package edu.kit.trufflehog.presenter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * <p>
 *     A ScheduledThreadPoolExecutor that logs exceptions and does not just drop them.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class LoggedScheduledExecutor extends ScheduledThreadPoolExecutor {
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

    @Override
    public void execute(Runnable command) {
        super.execute(wrapRunnable(command));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrapRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return super.submit(wrapRunnable(task), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(wrapCallable(task));
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
     *      Wraps the callable in a LogOnExceptionCallable so that if it fails, the exception is logged.
     * </p>
     *
     * @param callable The callable to wrap.
     * @return The wrapped callable.
     */
    private <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new LogOnExceptionCallable<>(callable);
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
                logger.error("Error in executing: " + runnable + " - It will no longer be run", e);

                // Throw error again so that it can continue its expected behavior
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * <p>
     *     A wrapper class for a callable. This logs the crash of a callable if it does crash.
     * </p>
     */
    private class LogOnExceptionCallable<T> implements Callable<T> {
        private Callable<T> callable;

        /**
         * <p>
         *      Creates a new LogOnExceptionCallable object based on a previous {@link Callable} object.
         * </p>
         *
         * @param callable The {@link Callable} to create the LogOnExceptionCallable object from
         */
        public LogOnExceptionCallable(Callable<T> callable) {
            super();
            this.callable = callable;
        }

        @Override
        public T call() {
            try {
                return callable.call();
            } catch (Exception e) {
                // Log exception here
                logger.error("Error in executing: " + callable + " - It will no longer be run", e);

                // Throw error again so that it can continue its expected behavior
                throw new RuntimeException(e);
            }
        }
    }
}