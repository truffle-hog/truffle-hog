package edu.kit.ipd.trufflehog.service.datalogging;

import edu.kit.ipd.trufflehog.commands.ICommand;
import edu.kit.ipd.trufflehog.model.graph.INetworkGraph;
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
     *     Creates a new DataLogger.
     * </p>
     */
    public DataLogger() {
    }

    /**
     * <p>
     *      Creates a new DataLog object based on the snapshot of a graph given and the list of commands that occured
     *      for the next X seconds after the snaphot was taken.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to use
     * @param commands
     * @return
     */
    public DataLog createDataLog(INetworkGraph snapshotGraph, List<ICommand> commands) {
        throw new NotImplementedException();
    }

    /**
     *
     * @param log
     */
    public void saveDataLog(DataLog log) {

    }

    /**
     *
     * @param index
     */
    public void loadDataLog(int index) {

    }

    /**
     *
     * @param log
     */
    public void outputDataLog(DataLog log) {

    }
}
