package edu.kit.trufflehog.service;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.model.network.recording.copying.ComponentCopier;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Hoehler on 16.03.2016.
 */
public class NodeStatisticsUpdater implements Runnable {
    private final INetworkReadingPort readingPort;
    private final INetworkViewPort viewPort;
    private final double smooth = 0.3;

    private long initialTime = 0;
    private int interval = 1000;
    private HashMap<IAddress, NodeStatisticsComponent> lastNodes;

    public NodeStatisticsUpdater(final INetworkReadingPort readingPort, final INetworkViewPort viewPort) {
        if (readingPort == null) throw new NullPointerException("readingPort must not be null!");
        if (viewPort == null) throw new NullPointerException("viewPort must not be null!");

        this.readingPort = readingPort;
        this.viewPort = viewPort;
        this.interval = 1000;
    }

    public NodeStatisticsUpdater(final INetworkReadingPort readingPort, final INetworkViewPort viewPort, final int interval) {
        if (readingPort == null) throw new NullPointerException("readingPort must not be null!");
        if (viewPort == null) throw new NullPointerException("viewPort must not be null!");
        if (interval < 0) throw new IllegalArgumentException("interval must not be less than 0!");
        this.readingPort = readingPort;
        this.viewPort = viewPort;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                updateNodeStatistics();
                updateGraphStatistics();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     * <p>
     *     Updates the node component data using exponential smoothing
     * </p>
     */
    private void updateNodeStatistics() {
        final Collection<INode> nodes = readingPort.getNetworkNodes();

        if (nodes == null)
            return;

        if (lastNodes == null) {
            for (INode node : nodes) {
                lastNodes = new HashMap<>();
                NodeStatisticsComponent nsc = node.getComponent(NodeStatisticsComponent.class);

                IComponentVisitor<IComponent> copier = new ComponentCopier();
                NodeStatisticsComponent n2 = (NodeStatisticsComponent)nsc.accept(copier);
                lastNodes.put(node.getAddress(), n2);
            }
        }

        NodeStatisticsComponent nscNew, nscOld;

        for (INode node : nodes) {
            nscNew = node.getComponent(NodeStatisticsComponent.class);
            nscOld = lastNodes.get(node.getAddress());
            if (nscOld == null) {
                nscOld = nscNew;
            }

            if (nscNew != null) {
                double delta = (nscNew.getCommunicationCount() - nscOld.getCommunicationCount());
                double oldThroughput = nscOld.getThroughput();

                double nextThroughput = smooth*delta + (1.0d - smooth) * oldThroughput;

                nscNew.setThroughput(nextThroughput);
            }

            IComponentVisitor<IComponent> copier = new ComponentCopier();
            NodeStatisticsComponent n2 = (NodeStatisticsComponent) (nscNew != null ? nscNew.accept(copier) : null);
            if (lastNodes.get(node.getAddress()) != null) {
                lastNodes.replace(node.getAddress(), n2);
            } else {
                lastNodes.put(node.getAddress(), n2);
            }
        }
    }

    private void updateGraphStatistics() {
        Collection<INode> nodes = readingPort.getNetworkNodes();
        if (nodes != null) {
            double nextThroughput = 0.0;
            int multicastCount = 0;

            for (INode node : nodes) {
                NodeStatisticsComponent nsc = node.getComponent(NodeStatisticsComponent.class);
                if (nsc != null) {
                    nextThroughput += nsc.getThroughput();
                }

                if (node.getAddress().isMulticast()) {
                    multicastCount++;
                }
            }

            //because we count outgoing and incoming packages, so one new package affects 2 node communication counters
            nextThroughput = nextThroughput/2;

            readingPort.setThroughput(nextThroughput);
            readingPort.setPopulation(nodes.size() - multicastCount);

            viewPort.setThroughput(nextThroughput);
            viewPort.setPopulation(nodes.size() - multicastCount);

            if (initialTime == 0)
                initialTime = Instant.now().toEpochMilli();

            viewPort.setViewTime(Instant.now().toEpochMilli() - initialTime);
        }
    }
}