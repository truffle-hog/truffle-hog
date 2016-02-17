package edu.kit.trufflehog.service.replaylogging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *     The CaptureSession is the abstraction of a folder in which replay logs are contained. It provides
 *     functionality that is shared among many classes of the replaylogging package. For instance, it can retrieve the
 *     session's start and end times, the contained replay logs as a sorted list, etc.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class CaptureSession implements ICaptureSession {
    private static final Logger logger = LogManager.getLogger(ReplayLogSaveService.class);

    private File replayLogsFolder;
    private Instant startInstant;
    private Instant endInstant;
    private Map<Long, File> replayLogs;

    /**
     * <p>
     *     Creates a new CaptureSession object. This constructor should ba called when a new session is created, and
     *     does not exists yet. (i.e. new replay logs will be saved into it, and not loaded from it).
     * </p>
     *
     * @param replayLogsFolder The parent folder of all replay logs in this capture session.
     * @param startInstant The instant the capture was started.
     */
    public CaptureSession(File replayLogsFolder, Instant startInstant) {
        this.replayLogsFolder = replayLogsFolder;
        this.startInstant = startInstant;
    }

    /**
     * <p>
     *     Creates a new CaptureSession object. This constructor should be called when an already existing session
     *     will be loaded into memory, and not when new replay logs will be saved into this session.
     * </p>
     *
     * @param replayLogsFolder The parent folder of all replay logs in this capture session.
     */
    public CaptureSession(File replayLogsFolder) {
        this(replayLogsFolder, null);
    }

    /**
     * <p>
     *     Loads all replay log files in the folder into a list and sorts them by their ending times.
     * </p>
     *
     * @throws NullPointerException thrown when no replay log files are found in the session folder
     */
    private TreeMap<Long, File> loadCaptureSessionSorted() throws NullPointerException {
        File[] filesArray = replayLogsFolder.listFiles();

        if (filesArray == null) {
            throw new NullPointerException("No replay log files found in session folder.");
        }

        List<File> fileList = Arrays.asList(filesArray);
        final Pattern p = Pattern.compile("([0-9]+)-([0-9]+).replaylog");

        // Map list to map with the replay log ending times as the key and sort this map
        return fileList.stream()
                .filter(fileTemp -> fileTemp.isFile()
                        && p.matcher(fileTemp.getName()).matches())
                .collect(Collectors.toMap(file -> {
                    Matcher m = p.matcher(file.getName());
                    m.matches();
                    return Long.parseLong(m.group(2));
                }, file -> file))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, TreeMap::new));
    }

    /**
     * <p>
     *     Creates a new capture session on the hard drive.
     * </p>
     *
     * @return True if the creation of a new capture session was successful, else false.
     */
    public boolean create() {
        return replayLogsFolder.mkdir();
    }

    /**
     * <p>
     *     Finishes a new capture session on the hard drive.
     * </p>
     *
     * @return True if the finishing of a new capture session was successful, else false.
     */
    public boolean finish() {
        TreeMap<Long, File> files;
        try {
            files = loadCaptureSessionSorted();
        } catch (NullPointerException e) {
            logger.error("Unable to load replay log files in session folder.", e);
            return false;
        }

        File lastFile = files.lastEntry().getValue();

        Pattern p = Pattern.compile("([0-9]+)-([0-9]+).replaylog");
        Matcher m = p.matcher(lastFile.getName());
        m.matches();
        endInstant = Instant.ofEpochMilli(startInstant.toEpochMilli() + Long.parseLong(m.group(2)));

        try {
            return replayLogsFolder.renameTo(new File(replayLogsFolder.getParentFile().getCanonicalPath() +
                    File.separator + replayLogsFolder.getName() + "-" + endInstant.toEpochMilli()));
        } catch (IOException e) {
            logger.error("Unable to rename capture session folder", e);
            return false;
        }
    }

    /**
     * <p>
     *     Loads a capture session with an existing folder containing replay logs.
     * </p>
     *
     * @return True if the loading operation was successful, else false.
     */
    public boolean load() {
        if (!replayLogsFolder.exists() || !replayLogsFolder.isDirectory()) {
            return false;
        }

        Pattern p = Pattern.compile("([0-9]+)-([0-9]+)");
        Matcher m = p.matcher(replayLogsFolder.getName());

        if (m.matches()) {
            startInstant = Instant.ofEpochMilli(Long.parseLong(m.group(1)));
            endInstant = Instant.ofEpochMilli(Long.parseLong(m.group(2)));

            try {
                replayLogs = loadCaptureSessionSorted();
                return true;
            } catch (NullPointerException e) {
                logger.error("Unable to load replay log files in session folder.", e);
                return false;
            }
        }

        logger.error("Folder name of session folder does not have required format.");
        return false;
    }

    @Override
    public File getSessionFolder() {
        return replayLogsFolder;
    }

    @Override
    public Instant getStartInstant() {
        return startInstant;
    }

    @Override
    public Instant getEndInstant() {
        return endInstant;
    }

    @Override
    public Map<Long, File> getSortedReplayLogs() {
        return replayLogs;
    }
}
