package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.model.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     The ReplayLogLoader loads {@link ReplayLog}s from the hard drive into memory, so that when the view needs them to
 *     display the old graph, it is at the view's disposal.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class ReplayLogLoader {
    private static final Logger logger = LogManager.getLogger(ReplayLogLoader.class);

    private final FileSystem fileSystem;
    private final ConcurrentSkipListSet<ReplayLog> replayLogs;

    private final int maxBufferSize;
    private final int maxBatchSize;
    private final int loadTimeLimit;

    /**
     * <p>
     *     Creates a new ReplayLogLoader object.
     * </p>
     *
     * @param fileSystem The fileSystem object that gives access to the locations where files are saved on the hard drive.
     *                   This is necessary to load ReplayLogs.
     */
    public ReplayLogLoader(FileSystem fileSystem, int maxBufferSize, int maxBatchSize, int loadTimeLimit) {
        this.fileSystem = fileSystem;
        this.replayLogs = new ConcurrentSkipListSet<>();

        this.maxBufferSize = maxBufferSize;
        this.maxBatchSize = maxBatchSize;
        this.loadTimeLimit = loadTimeLimit;
    }

    /**
     * <p>
     *     Gets the {@link ReplayLog} that comes after the given one in the set, if it is found. Otherwise returns null.
     * </p>
     *
     * @param replayLog The {@link ReplayLog} before the one to find.
     * @return Gets the {@link ReplayLog} that comes after the given one in the set, if it is found. Otherwise returns null.
     */
    public ReplayLog getNextReplayLog(ReplayLog replayLog) {
        // No need to synchronize because replayLogs is concurrent

        boolean returnNext = false;
        for (ReplayLog replayLogLocal : replayLogs) {
            if (replayLogLocal.equals(replayLog)) {
                returnNext = true;
            } else if (returnNext) {
                return replayLogLocal;
            }
        }

        return null;
    }

    /**
     * <p>
     *     Returns the number of milliseconds from the Java epoch of 1970-01-01T00:00:00Z to the end of the last buffered
     *     replay log.
     * </p>
     *
     * @return The number of milliseconds from the Java epoch of 1970-01-01T00:00:00Z to the end of the last buffered
     *     replay log
     */
    public long bufferedUntil() {
        // No need to synchronize because replayLogs is concurrent

        return replayLogs.last().getEndInstant();
    }

    /**
     * <p>
     *     Gets all capture sessions as files that are found on the hard drive
     * </p>
     *
     * @return All capture sessions as files that are found on the hard drive
     * @throws NullPointerException Thrown if no capture sessions were found
     */
    public synchronized File[] getAvailableSessions() throws NullPointerException {
        // This is synchronized because this method is called by the getAllCaptureSessions method in the
        // IReplayLogController, which could be called from outside this thread

        // Get all files in folder
        File replayLogFolder = fileSystem.getReplayLogFolder();
        File[] files = replayLogFolder.listFiles();
        if (files == null || files.length <= 0) {
            throw new NullPointerException("No replay logs found at all (no capture sessions found).");
        }

        return files;
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(long, boolean)}. {@link #getData(long, boolean)}
     *     gets the desired ReplayLog object from the already loaded ReplayLog objects and does not load any ReplayLog
     *     itself.
     * </p>
     *
     * @param selectedFolderOptional The {@link CaptureSession} folder wrapped in an {@link Optional} (i.e. the folder
     *                               where the replay logs are located that should be loaded).
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     * @return True if a replay log was found and loaded, else false
     */
    public boolean loadData(Optional<File> selectedFolderOptional, long instant) {
        // No need to synchronize because replayLogs is concurrent

        // Get the folder whose time interval includes the given instant
        File selectedFolder;
        if (!selectedFolderOptional.isPresent()) {
            logger.debug("No replay logs found for given instant.");
            return false;
        } else {
            selectedFolder = selectedFolderOptional.get();
        }

        // Get all replay log files found sorted in ascending order based on their ending playback time
        CaptureSession captureSession = new CaptureSession(selectedFolder);
        captureSession.load();
        Map<Long, File> validReplayLogFiles = captureSession.getSortedReplayLogs();

        // Get a list of all replay log files that should be deserialized.
        List<File> closestReplayLogs = getClosestReplayLog(instant, validReplayLogFiles);

        // Deserialize all replay logs in the list returned above
        return deserializeReplayLogs(closestReplayLogs);
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(long, boolean)}. {@link #getData(long, boolean)}
     *     gets the desired ReplayLog object from the already loaded ReplayLog objects and does not load any ReplayLog
     *     itself.
     * </p>
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     * @return True if a replay log was found and loaded, else false
     */
    public boolean loadData(long instant) {
        // No need to synchronize because replayLogs is concurrent

        logger.debug("Loading new batch of replay logs.");

        // Get all files in folder
        File[] files = getAvailableSessions();

        // Get the folder whose time interval includes the given instant (So get the folder that contains the relevant
        // replay logs
        Optional<File> selectedFolderOptional = getReplayLogFolder(files, instant);

        // Now we actually choose the batch of replay logs to deserialize and deserialize them
        return loadData(selectedFolderOptional, instant);
    }

    /**
     * <p>
     *     Deserializes all replay logs given to it and loads them into memory by putting them into the
     *     {@link ReplayLogLoader#replayLogs} priority queue.
     * </p>
     *
     * @param closestReplayLogs The replay logs to deserialize and load into memory
     * @return True if a replay log was found and loaded, else false
     */
    private boolean deserializeReplayLogs(List<File> closestReplayLogs) throws MissingResourceException {
        // Deserialize all replay logs in the list returned above
        if (!closestReplayLogs.isEmpty()) {
            closestReplayLogs.forEach(file -> {
                ReplayLog replayLog = null;
                try {
                    // Deserialize the replay log
                    FileInputStream inputFileStream = new FileInputStream(file.getCanonicalPath());
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
                    replayLog = (ReplayLog) objectInputStream.readObject();
                    objectInputStream.close();
                    inputFileStream.close();

                    // Add it to the priority queue
                    replayLogs.add(replayLog);

                    // Check if the priority queue is full, and if so remove the "oldest" replay log (the head of the
                    // queue since the queue is sorted in ascending order). The queue is considered full it contains
                    // more elements than is defined in maxBufferSize
                    if (replayLogs.size() > maxBufferSize) {
                        replayLogs.remove(replayLogs.first());
                    }
                } catch(IOException | ClassNotFoundException e) {
                    logger.error("Unable to deserialize a replay log", e);
                }
            });

            logger.debug("New batch of replay logs loaded.");
            return true;
        } else {
            logger.debug("No replay logs to load found!");
            return false;
        }
    }

    /**
     * <p>
     *     Get the replay log that matches most closely the given time instant and returns a whole batch of replay
     *     logs that lie around this chosen replay log. This way replay logs are buffered for future use and don't have
     *     to be constantly loaded.
     * </p>
     * <p>
     *     This method finds the {@link File} object of the replay log that is closest to in time, and if possible
     *     contains the graph the way it was at the given instant.
     * </p>
     *
     * @param instant The instant used to choose the right replay log file
     * @param validReplayLogs All replay log files that were found in the folder which covers the time interval of
     *                        interest.
     * @return The batch of replay logs that should be deserialized.
     */
    private List<File> getClosestReplayLog(long instant, Map<Long, File> validReplayLogs) {
        List<File> fileList = new LinkedList<>();

        // Check if we can just directly return the received list because it is smaller than the batch size
        if (validReplayLogs.size() <= maxBatchSize) {
            fileList.addAll(validReplayLogs.values());
            return fileList;
        }

        // Find the replay log around which to base the batch
        long closestInstant = -1;
        long closestReplayLogFileIndex = 0;
        int j = 0;
        for (Long time : validReplayLogs.keySet()) {
            if (closestInstant == -1) {
                closestInstant = time;
                closestReplayLogFileIndex = j;
            } else {
                if (Math.abs(time - instant) < closestInstant) {
                    closestInstant = time;
                    closestReplayLogFileIndex = j;
                }
            }

            j++;
        }

        // Collect + maxBatchSize replay logs which form the batch of replay logs that will be deserialized
        j = 0;
        for (File file : validReplayLogs.values()) {
            long index = closestReplayLogFileIndex + j;
            if (index >= j && index < validReplayLogs.size()) {
                if (j >= maxBatchSize) {
                    return fileList;
                }

                fileList.add(file);
                j++;
            }
        }

        return fileList;
    }

    /**
     * <p>
     *     Gets the folder which should contain the replay logs that contain the graph at the given interval.
     * </p>
     *
     * @param files All replay log folders found
     * @param instant The time instant for which to find a replay log
     * @return An optional containing the wanted folder if it was found.
     */
    private Optional<File> getReplayLogFolder(File[] files, final long instant) {
        List<File> fileList = Arrays.asList(files);
        final Pattern p = Pattern.compile("([0-9]+)-([0-9]+)");
        return fileList.stream()
                .filter(fileTemp -> {
                    Matcher m = p.matcher(fileTemp.getName());
                    if (fileTemp.isDirectory() && m.matches()) {
                        long startTime = Long.parseLong(m.group(1));
                        long endTime = Long.parseLong(m.group(2));

                        return startTime <= instant && endTime >= instant;
                    } else {
                        return false;
                    }
                })
                .findFirst();
    }

    /**
     * <p>
     *     Gets the ReplayLog object closest to the given time instant.
     * </p>
     * <p>
     *     If strict is true, a replay log containing the given time instant must be returned, else the
     *     MissingResourceException is thrown. If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else a MissingResourceException is thrown.
     * </p>
     * <p>
     *     This is done because there is a small gap in time between different replay logs, usually between 25-50 milliseconds
     *     because the logger needs time to execute as well. If the user happens to be unlucky to select such a gap as
     *     the next replay log that should be played, than the system does not crash. However since this does deviate
     *     from the expected behaviour it is made optional.
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object. The ReplayLog object closest to
     *                the time instant will be returned.
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     * @return The ReplayLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no appropriate replay log is found in memory.
     */
    public ReplayLog getData(long instant, boolean strict) throws MissingResourceException {
        if (strict) {
            return getData(instant, 0);
        } else {
            return getData(instant, loadTimeLimit);
        }
    }

    /**
     * <p>
     *     Gets a ReplayLog object that contains the given instant in its interval (there should be only one - but this
     *     cannot be guaranteed).
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object.
     * @return The ReplayLog object that contains the given instant in its interval.
     * @throws MissingResourceException Thrown when no appropriate replay log is found in memory.
     */
    private ReplayLog getData(final long instant, final long loadTimeLimit) throws MissingResourceException {
        // Get all replay logs who contain the instant in their covered time interval
        return replayLogs.stream()
                .filter(replayLog -> replayLog.getStartInstant() - loadTimeLimit <= instant
                        && replayLog.getEndInstant() + loadTimeLimit >= instant)
                .reduce((replaylog1, replaylog2) -> {
                    // We make a sorted map here to easily find the replay log with the smallest offset
                    TreeMap<Long, ReplayLog> map = new TreeMap<>();
                    map.put(Math.abs(replaylog1.getEndInstant() - instant), replaylog1);
                    map.put(Math.abs(instant - replaylog1.getStartInstant()), replaylog1);
                    map.put(Math.abs(replaylog2.getEndInstant() - instant), replaylog2);
                    map.put(Math.abs(instant - replaylog2.getStartInstant()), replaylog2);
                    return map.firstEntry().getValue();
                })
                .orElseThrow(() -> new MissingResourceException("ReplayLog that matches time instant not found",
                "ReplayLog", instant + ""));
    }
}
