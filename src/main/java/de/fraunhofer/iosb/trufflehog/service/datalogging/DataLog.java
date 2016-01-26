package de.fraunhofer.iosb.trufflehog.service.datalogging;

import de.fraunhofer.iosb.trufflehog.command.ICommand;
import de.fraunhofer.iosb.trufflehog.model.graph.INetworkGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * <p>
 *     A DataLog object contains the snapshot of a given graph, a list of all commands that were executed for the next
 *     X seconds following that snapshot and a timestamp of when the DataLog starts to where it ends.
 * </p>
 * <p>
 *     DataLog objects are used to recreate a graph that already occurred. They capture all necessary information to
 *     display the full graph the way it was at any given point in time. This is done by applying the commands stored
 *     in it back on the snapshot until the end of the DataLog is reached, at which point the commands of the next
 *     data log are applied and so on.
 * </p>
 *
 * @author MrX
 */
public class DataLog implements Serializable {

    /**
     * <p>
     *     Creates a new DataLog object containing the given graph snapshot and the list of commands.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to include in the data log.
     * @param commands The list of commands to include in the data log.
     */
    public DataLog(INetworkGraph snapshotGraph, List<ICommand> commands) {
    }

    /**
     * <p>
     *     Gets the graph snapshot contained in this DataLog object.
     * </p>
     *
     * @return The graph snapshot contained in this DataLog object.
     */
    public INetworkGraph getGraphSnapshot() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Gets the command list contained in this DataLog object.
     * </p>
     *
     * @return The command list contained in this DataLog object.
     */
    public List<ICommand> getCommands() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Gets the point in time of when this DataLog object starts to contain graph data.
     * </p>
     *
     * @return The point in time of when this DataLog object starts to contain graph data.
     */
    public Instant getStartInstant() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Gets the point in time of when this DataLog object stops to contain graph data.
     * </p>
     *
     * @return The point in time of when this DataLog object stops to contain graph data.
     */
    public Instant getEndInstant() {
        throw new NotImplementedException();
    }
}
