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

/**
 * <p>
 *     The ReplayLogLoader loads {@link ReplayLog}s from the hard drive into memory, so that when the view needs them to
 *     display the old graph, it is at the view's disposal.
 * </p>
 */
public class ReplayLogLoader {
    private static final Logger logger = LogManager.getLogger(ReplayLogLoader.class);

    private FileSystem fileSystem;
    private int MAX_BUFFER_SIZE;                  //
    private int REPLAY_LOG_BATCH_LOAD_SIZE;       //
    PriorityQueue<ReplayLog> replayLogs;

    /**
     * <p>
     *     Creates a new ReplayLogLoader object.
     * </p>
     *
     * @param fileSystem The fileSystem object that gives access to the locations where files are saved on the hard drive.
     *                   This is necessary to load ReplayLogs.
     */
    public ReplayLogLoader(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        replayLogs = new PriorityQueue<>();

        MAX_BUFFER_SIZE = 200;
        REPLAY_LOG_BATCH_LOAD_SIZE = 20;
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(Instant)}. {@link #getData(Instant)} gets
     *     the desired ReplayLog object from the already loaded ReplayLog objects and does not load any ReplayLog itself.
     * </p>
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     */
    public void loadData(Instant instant) {
        // Get all files in folder
        File replayLogFolder = fileSystem.getReplayLogFolder();
        File[] replayLogFiles = replayLogFolder.listFiles();

        // Make sure there are files
        if (replayLogFiles == null || replayLogFiles.length <= 0) {
            return;
        }

        // Get all replay log files found sorted in ascending order based on their middle playback time (end playback
        // time - start playback time)
        TreeMap<Long, File> validReplayLogFiles = getReplayLogMap(replayLogFiles);

        // Get a list of all replay log files that should be deserialized. This is a number
        List<File> closestReplayLogs = getClosestReplayLog(instant, validReplayLogFiles);

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
                    // more elements than is defined in MAX_BUFFER_SIZE
                    if (replayLogs.size() > MAX_BUFFER_SIZE) {
                        replayLogs.poll();
                    }
                } catch(IOException | ClassNotFoundException e) {
                    logger.error("Unable to deserialize a replay log", e);
                }
            });
        }
    }

    /**
     *
     * @param instant
     * @param validReplayLogs
     * @return
     */
    private List<File> getClosestReplayLog(Instant instant, TreeMap<Long, File> validReplayLogs) {
        List<File> fileList = new LinkedList<>();

        //
        if (validReplayLogs.size() <= REPLAY_LOG_BATCH_LOAD_SIZE) {
            fileList.addAll(validReplayLogs.values());
            return fileList;
        }

        //
        long closestInstant = -1;
        long closestReplayLogFileIndex = 0;
        int j = 0;
        for (Long time : validReplayLogs.keySet()) {
            if (closestInstant == -1) {
                closestInstant = time;
                closestReplayLogFileIndex = j;
            } else {
                if (Math.abs(time - instant.getEpochSecond()) < closestInstant) {
                    closestInstant = time;
                    closestReplayLogFileIndex = j;
                }
            }

            j++;
        }

        //
        j = 0;
        for (Long time : validReplayLogs.keySet()) {
            try {
                if (j >= 20) {
                    return fileList;
                }

                fileList.add(validReplayLogs.get(closestReplayLogFileIndex + j - 9));
                j++;
            } catch (IndexOutOfBoundsException e) {
                // Do nothing because eventually we will
            }
        }

        return fileList;
    }

    /**
     *
     * @param replayLogs
     * @return
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
     *      Gets the ReplayLog object closest to the given time instant.
     * </p>
     * <p>
     *      Condition: A ReplayLog reasonably close in time to the given instant must be in memory, else a
     *      MissingResourceException is thrown.
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object. The ReplayLog object closest to
     *                the time instant will be returned.
     * @return The ReplayLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no ReplayLog reasonably close in time to the given instant is found
     *                                  in memory.
     */
    public ReplayLog getData(Instant instant) throws MissingResourceException {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
