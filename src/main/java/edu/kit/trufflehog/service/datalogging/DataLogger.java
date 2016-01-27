package edu.kit.trufflehog.service.datalogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * <p>
 *     The DataLogger manages the saving of the graph structure. It creates {@link DataLog} objects which are serialized
 *     and saved. From these DataLog objects, the entire graph can be recreated from a certain point in time.
 * </p>
 * <p>
 *     The DataLogger saves the state of the graph through two things. It takes snapshots of the graph every X seconds
 *     through the {@link SnapshotLogger} and then saves all commands that occurred from that point on until the next
 *     snapshot is taken through the {@link CommandLogger}. Together these snapshots with the list of commands form a
 *     DataLog. When blocks of DataLogs that were saved consecutively are loaded back into memory, they can be used to
 *     reconstruct the graph by taking the snapshot as the original graph and then applying all commands that occurred
 *     back on the graph, in the order and interval they occurred. This is how old graphs can be viewed.
 * </p>
 */
public class DataLogger {

    /**
     * <p>
     *     Creates a new DataLogger object.
     * </p>
     */
    public DataLogger() {
    }

    /**
     * <p>
     *      Creates a new {@link DataLog} object based on the snapshot of a graph given and the list of commands that
     *      were executed for the next X seconds after the snapshot was taken.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to include in the data log.
     * @param commands The list of commands to include in the data log.
     * @return A new DataLog object that is serializable and that contains the graph snapshot and the list of commands
     *          passed to the method.
     */
    public DataLog createDataLog(INetworkGraph snapshotGraph, List<ICommand> commands) {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Saves the {@link DataLog} object given on the hard drive so that it can be retrieved later.
     * </p>
     *
     * @param log The data log to save on the hard drive.
     */
    public void saveDataLog(DataLog log) {
    }
}