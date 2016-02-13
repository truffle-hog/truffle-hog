package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.model.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *     The ReplayLogLoader loads {@link ReplayLog}s from the hard drive into memory, so that when the view needs them to
 *     display the old graph, it is at the view's disposal.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLogLoader {
    private static final Logger logger = LogManager.getLogger(ReplayLogLoader.class);

    private FileSystem fileSystem;
    private SortedSet<ReplayLog> replayLogs;

    private int maxBufferSize;
    private int maxBatchSize;
    private int loadTimeLimit;

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
        this.replayLogs = new TreeSet<>();

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
    public synchronized ReplayLog getNextReplayLog(ReplayLog replayLog) {
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
    public synchronized long bufferedUntil() {
        return replayLogs.last().getEndInstant().toEpochMilli();
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(Instant, boolean)}. {@link #getData(Instant, boolean)}
     *     gets the desired ReplayLog object from the already loaded ReplayLog objects and does not load any ReplayLog
     *     itself.
     * </p>
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     * @return True if a replay log was found and loaded, else false
     */
    public boolean loadData(Instant instant) {
        logger.debug("Loading new batch of replay logs.");

        // Get all files in folder
        File replayLogFolder = fileSystem.getReplayLogFolder();
        File[] files = replayLogFolder.listFiles();
        if (files == null || files.length <= 0) {
            logger.debug("No replay logs found at all.");
            return false;
        }

        // Get the folder whose time interval includes the given instant (So get the folder that contains the relevant
        // replay logs
        File selectedFolder = getReplayLogFolder(files, instant);
        if (selectedFolder == null) {
            logger.debug("No replay logs found for given instant.");
            return false;
        }

        // Get all replayLogs in folder
        File[] replayLogFiles = selectedFolder.listFiles();
        if (replayLogFiles == null || replayLogFiles.length == 0) {
            logger.debug("No replay logs found in folder for given instant.");
            return false;
        }

        // Get all replay log files found sorted in ascending order based on their middle playback time (end playback
        // time - start playback time)
        TreeMap<Long, File> validReplayLogFiles = getReplayLogMap(replayLogFiles);

        // Get a list of all replay log files that should be deserialized.
        List<File> closestReplayLogs = getClosestReplayLog(instant, validReplayLogFiles);

        // Deserialize all replay logs in the list returned above
        if (!closestReplayLogs.isEmpty()) {
            deserializeReplayLogs(closestReplayLogs);

            logger.debug("New batch of replay logs loaded.");
            return true;
        } else {
            logger.debug("No replay logs to load found!");
            return false;
        }
    }

    /**
     * <p>
     *     Deserializes all replay logs given to it and loads them into memory by putting them into the
     *     {@link ReplayLogLoader#replayLogs} priority queue.
     * </p>
     *
     * @param closestReplayLogs The replay logs to deserialize and load into memory
     */
    private void deserializeReplayLogs(List<File> closestReplayLogs) {
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
    private List<File> getClosestReplayLog(Instant instant, TreeMap<Long, File> validReplayLogs) {
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
                if (Math.abs(time - instant.toEpochMilli()) < closestInstant) {
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
     *     Gets all replay log files in the folder mapped to the median of their time interval that they represent.
     *     That way it is easy later on to find the replay log of interest. These replay logs are sorted based
     * </p>
     *
     * @param replayLogs All files in the replay log folder that was chosen in {@link #getReplayLogFolder(File[], Instant)}
     * @return All files that are actual replay logs mapped to their median time interval value in a sorted map
     */
    private TreeMap<Long, File> getReplayLogMap(File[] replayLogs) {
        TreeMap<Long, File> validReplayLogs = new TreeMap<>();
        for (File replayLog : replayLogs) {

            // Check if the file matches the replay log file name structure
            Pattern p = Pattern.compile("([0-9]+)-([0-9]+).replaylog");
            Matcher m = p.matcher(replayLog.getName());
            if (m.matches()) {

                // Get the middle time between the start and end time
                long startTime = Long.parseLong(m.group(1));
                long endTime = Long.parseLong(m.group(2));
                long middleTime = (startTime + endTime) / 2;

                validReplayLogs.put(middleTime, replayLog);
            }
        }

        return validReplayLogs;
    }

    /**
     * <p>
     *     Gets the folder which contains the replay logs that contain the graph at the given interval.
     * </p>
     *
     * @param files All replay log folders found
     * @param instant The time instant for which to find a replay log
     * @return The folder
     */
    private File getReplayLogFolder(File[] files, Instant instant) {
        for (File file : files) {
            if (file.isDirectory()) {

                // Check if the folder matches the replay log folder name structure
                Pattern p = Pattern.compile("([0-9]+)");
                Matcher m = p.matcher(file.getName());

                if (m.matches()) {
                    // Find folder that contains replay logs for the given instant
                    long startTime = Long.parseLong(file.getName());
                    long instantTime = instant.toEpochMilli();

                    if (instantTime >= startTime) {
                        return file;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     *     Gets the ReplayLog object closest to the given time instant.
     * </p>
     * <p>
     *     If strict is set, a replay log containing the given time instant must be returned, else the
     *     MissingResourceException is thrown. If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else a MissingResourceException is thrown.
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object. The ReplayLog object closest to
     *                the time instant will be returned.
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in + {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered interval.
     * @return The ReplayLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no appropriate replay log is found in memory.
     */
    public ReplayLog getData(Instant instant, boolean strict) throws MissingResourceException {
        if (strict) {
            return getDataStrict(instant);
        } else {
            return getDataNotStrict(instant);
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
    private ReplayLog getDataNotStrict(Instant instant) throws MissingResourceException {
        ReplayLog foundReplayLog = null;
        int timeOffset = loadTimeLimit + 1;
        for (ReplayLog replayLog : replayLogs) {
            long endOffset = replayLog.getEndInstant().toEpochMilli() - instant.toEpochMilli();
            long startOffset = instant.toEpochMilli() - replayLog.getStartInstant().toEpochMilli();

            if (endOffset > 0 && startOffset > 0) {
                // Replay log contained time instant
                return replayLog;
            } else {
                endOffset = Math.abs(endOffset);
                startOffset = Math.abs(startOffset);

                // Find replay log whose covered interval lies closest to the desired time instant
                if (endOffset <= loadTimeLimit || startOffset <= loadTimeLimit) {
                    if (endOffset < startOffset && endOffset < timeOffset) {
                        foundReplayLog = replayLog;
                        timeOffset = (int) endOffset;
                    } else if (startOffset < timeOffset) {
                        foundReplayLog = replayLog;
                        timeOffset = (int) startOffset;
                    }
                }
            }
        }

        if (foundReplayLog == null) {
            throw new MissingResourceException("ReplayLog that matches time instant not found", "ReplayLog",
                    instant.toEpochMilli() + "");
        }

        return foundReplayLog;
    }

    /**
     * <p>
     *     Gets the ReplayLog object closest to the given time instant.
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object. The ReplayLog object closest to
     *                the time instant will be returned.
     * @return The ReplayLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no appropriate replay log is found in memory.
     */
    private ReplayLog getDataStrict(Instant instant) throws MissingResourceException {
        // Get all replay logs who contain the instant in their covered time interval
        List<ReplayLog> foundReplayLogs = replayLogs.stream()
                .filter(replayLog -> replayLog.getStartInstant().toEpochMilli() <= instant.toEpochMilli()
                        && replayLog.getEndInstant().toEpochMilli() >= instant.toEpochMilli())
                .collect(Collectors.toList());

        if (foundReplayLogs.size() > 1) {
            // More than 1 replay log has been found
            logger.debug("Found more than 1 replay log that matches time interval, returning 1st");
            return foundReplayLogs.get(0);
        } else if (foundReplayLogs.isEmpty()) {
            // No replay log has been found
            throw new MissingResourceException("ReplayLog that matches time instant not found", "ReplayLog",
                    instant.toEpochMilli() + "");
        } else {
            // Exactly 1 replay log has been found
            return foundReplayLogs.get(0);
        }
    }
}
