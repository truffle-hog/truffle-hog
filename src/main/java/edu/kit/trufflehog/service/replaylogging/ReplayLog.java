package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraph;
import org.apache.commons.collections4.map.LinkedMap;

import java.io.Serializable;
import java.time.Instant;

/**
 * <p>
 *     A ReplayLog object contains the snapshot of a given graph, a list of all commands that were executed for the next
 *     X seconds following that snapshot and a timestamp of when the ReplayLog starts to where it ends.
 * </p>
 * <p>
 *     ReplayLog objects are used to recreate a graph that already occurred. They capture all necessary information to
 *     display the full graph the way it was at any given point in time. This is done by applying the commands stored
 *     in it back on the snapshot until the end of the ReplayLog is reached, at which point the commands of the next
 *     replay log are applied and so on.
 * </p>
 *
 * @author Julian Brendl
 */
public class ReplayLog implements Serializable, Comparable<ReplayLog> {
    private AbstractNetworkGraph snapshotGraph;
    private LinkedMap<ICommand, Instant> commands;
    private Instant startInstant;
    private Instant endInstant;

    /**
     * <p>
     *     Creates a new ReplayLog object containing the given graph snapshot and the list of commands.
     * </p>
     *
     * @param snapshotGraph The snapshot of the graph to include in the replay log.
     * @param commands The list of commands to include in the replay log.
     */
    public ReplayLog(AbstractNetworkGraph snapshotGraph, LinkedMap<ICommand, Instant> commands) {
        this.snapshotGraph = snapshotGraph;
        this.commands = commands;

        if (!commands.isEmpty()) {
            startInstant = commands.get(commands.firstKey());
            endInstant = commands.get(commands.lastKey());
        }
    }

    /**
     * <p>
     *     Gets the graph snapshot contained in this ReplayLog object.
     * </p>
     *
     * @return The graph snapshot contained in this ReplayLog object.
     */
    public AbstractNetworkGraph getGraphSnapshot() {
        return snapshotGraph;
    }

    /**
     * <p>
     *     Gets the command list contained in this ReplayLog object.
     * </p>
     *
     * @return The command list contained in this ReplayLog object.
     */
    public LinkedMap<ICommand, Instant> getCommands() {
        return commands;
    }

    /**
     * <p>
     *     Gets the point in time of when this ReplayLog object starts to contain graph data.
     * </p>
     *
     * @return The point in time of when this ReplayLog object starts to contain graph data.
     */
    public Instant getStartInstant() {
        return startInstant;
    }

    /**
     * <p>
     *     Gets the point in time of when this ReplayLog object stops to contain graph data.
     * </p>
     *
     * @return The point in time of when this ReplayLog object stops to contain graph data.
     */
    public Instant getEndInstant() {
        return endInstant;
    }

    @Override
    public int compareTo(ReplayLog o) {
        long thisMedianTime = (startInstant.toEpochMilli() + endInstant.toEpochMilli()) / 2;
        long otherMedianTime = (o.getStartInstant().toEpochMilli() + o.getEndInstant().toEpochMilli()) / 2;

        return (int)(thisMedianTime - otherMedianTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplayLog)) return false;

        ReplayLog replayLog = (ReplayLog) o;

        return !(snapshotGraph != null ? !snapshotGraph.equals(replayLog.snapshotGraph) : replayLog.snapshotGraph != null)
                && !(commands != null ? !commands.equals(replayLog.commands) : replayLog.commands != null)
                && !(startInstant != null ? !startInstant.equals(replayLog.startInstant) : replayLog.startInstant != null)
                && !(endInstant != null ? !endInstant.equals(replayLog.endInstant) : replayLog.endInstant != null);

    }

    @Override
    public int hashCode() {
        int result = snapshotGraph != null ? snapshotGraph.hashCode() : 0;
        result = 31 * result + (commands != null ? commands.hashCode() : 0);
        result = 31 * result + (startInstant != null ? startInstant.hashCode() : 0);
        result = 31 * result + (endInstant != null ? endInstant.hashCode() : 0);
        return result;
    }
}
