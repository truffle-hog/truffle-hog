package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     The ReplayLogger manages the saving of the graph structure. It creates {@link ReplayLog} objects which are serialized
 *     and saved. From these ReplayLog objects, the entire graph can be recreated from a certain point in time.
 * </p>
 * <p>
 *     The ReplayLogger saves the state of the graph through two things. It takes snapshots of the graph every X seconds
 *     and then saves all commands that occurred from that point on until the next snapshot is taken through the
 *     {@link CommandLogger}. Together these snapshots with the list of commands form a ReplayLog. When blocks of
 *     DataLogs that were saved consecutively are loaded back into memory, they can be used to reconstruct the graph by
 *     taking the snapshot as the original graph and then applying all commands that occurred back on the graph, in the
 *     order and interval they occurred. This is how old graphs can be viewed.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class ReplayLogger {
    private static final Logger logger = LogManager.getLogger(ReplayLogger.class);

    /**
     * <p>
     *     Creates a new ReplayLogger object.
     * </p>
     */
    public ReplayLogger() {
    }

    /**
     * <p>
     *     Creates a new {@link ReplayLog} object based on the snapshot of a graph given and the list of commands that
     *     were executed for the next X seconds after the snapshot was taken.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to include in the replay log.
     * @param commands The list of commands to include in the replay log.
     * @return A new ReplayLog object that is serializable and that contains the graph snapshot and the list of commands
     *          passed to the method.
     */
    public ReplayLog createReplayLog(INetworkGraph snapshotGraph, LinkedMap<IReplayCommand, Long> commands) {
        return new ReplayLog(snapshotGraph, commands);
    }

    public boolean closeCaptureSession(File currentReplayLogFolder) {
        File[] filesArray = currentReplayLogFolder.listFiles();

        if (filesArray == null) {
            return false;
        }

        List<File> fileList = Arrays.asList(filesArray);
        final Pattern p = Pattern.compile("([0-9]+)-([0-9]+).replaylog");

        Optional<File> fileOptional = fileList.stream()
                .filter(fileTemp -> fileTemp.isFile()
                        && p.matcher(fileTemp.getName()).matches())
                .sorted((file1, file2) -> {
                    // Check if the file matches the replay log file name structure
                    Matcher m1 = p.matcher(file1.getName());
                    Matcher m2 = p.matcher(file2.getName());

                    // Get the middle time between the start and end time
                    long endTime1 = Long.parseLong(m1.group(2));
                    long endTime2 = Long.parseLong(m2.group(2));
                    return (int) (endTime1 - endTime2);
                })
                .findFirst();

        if (fileOptional.isPresent()) {
            File file = fileOptional.get();
            File parentFile = file.getParentFile();
            String startTime = currentReplayLogFolder.getName();
            // Check if the file matches the replay log file name structure
            Matcher m = p.matcher(file.getName());

            try {
                return currentReplayLogFolder.renameTo(new File(parentFile.getCanonicalPath() +
                        File.separator + startTime + "-" + m.group(2)));
            } catch (IOException e) {
                logger.error("Unable to rename capture session folder");
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * <p>
     *     Saves the {@link ReplayLog} object given on the hard drive so that it can be retrieved later.
     * </p>
     *
     * @param replayLog The replay log to save on the hard drive.
     * @param currentReplayLogFolder The folder where to save the replay logs to
     */
    public void saveReplayLog(ReplayLog replayLog, File currentReplayLogFolder) {
        if (replayLog == null || replayLog.getCommands().isEmpty() || Thread.currentThread().isInterrupted()) {
            return;
        }

        try {
            String fileName = replayLog.getStartInstant() + "-"
                    + replayLog.getEndInstant() + ".replaylog";
            String filePath = currentReplayLogFolder.getCanonicalPath() + File.separator + fileName;
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(replayLog);
            out.close();
            fileOut.close();
            logger.debug("Serialized replay log and saved at: " + filePath);
        } catch(NullPointerException | IOException e) {
            logger.error("Unable to serialize replay log: " + replayLog, e);
        }
    }
}