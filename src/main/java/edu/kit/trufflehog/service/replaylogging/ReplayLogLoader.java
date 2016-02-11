package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.model.FileSystem;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
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
    private LinkedList<ReplayLog> replayLogs;

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
        replayLogs = new LinkedList<>();
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
        File[] replayLogs = replayLogFolder.listFiles();

        // Make sure there are files
        if (replayLogs == null || replayLogs.length <= 0) {
            return;
        }

        //
        LinkedMap<File, Long> validReplayLogs = getReplayLogMap(replayLogs);

        //
        File closestReplayLogFile = getClosestReplayLog(instant, validReplayLogs);

        if (closestReplayLogFile != null) {
            ReplayLog replayLog = null;
            try {
                FileInputStream inputFileStream = new FileInputStream(closestReplayLogFile.getCanonicalPath());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
                replayLog = (ReplayLog) objectInputStream.readObject();
                objectInputStream.close();
                inputFileStream.close();
            } catch(IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param instant
     * @param validReplayLogs
     * @return
     */
    private List<File> getClosestReplayLog(Instant instant, LinkedMap<File, Long> validReplayLogs) {
        List<File> fileList = new LinkedList<>();

        if (validReplayLogs.size() <= 20) {
            fileList.addAll(validReplayLogs.keySet());
            return fileList;
        }

        File closestReplayLogFile = null;
        long closestInstant = -1;
        int closestReplayLogFileIndex = 0;
        int j = 0;
        for (File replayLog : validReplayLogs.keySet()) {
            if (closestInstant == -1) {
                closestReplayLogFile = replayLog;
                closestInstant = validReplayLogs.get(replayLog);
                closestReplayLogFileIndex = j;
            } else {
                long currentInstant = validReplayLogs.get(replayLog);
                if (Math.abs(currentInstant - instant.getEpochSecond()) < closestInstant) {
                    closestReplayLogFile = replayLog;
                    closestInstant = validReplayLogs.get(replayLog);
                    closestReplayLogFileIndex = j;
                }
            }

            j++;
        }

        j = 0;
        for (File replayLog : validReplayLogs.keySet()) {
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
    private LinkedMap<File, Long> getReplayLogMap(File[] replayLogs) {
        LinkedMap<File, Long> validReplayLogs = new LinkedMap<>();
        for (File replayLog : replayLogs) {

            // Check if the file matches the replay log file name structure
            Pattern p = Pattern.compile("([0-9]+)-([0-9]+).replaylog");
            Matcher m = p.matcher(replayLog.getName());
            if (m.matches()) {

                // Get the middle time between the start and end time
                long startTime = Long.parseLong(m.group(1));
                long endTime = Long.parseLong(m.group(2));
                long middleTime = (startTime + endTime) / 2;

                validReplayLogs.put(replayLog, middleTime);
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
