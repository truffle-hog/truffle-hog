package edu.kit.trufflehog.service;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.recording.copying.ComponentCopier;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Hoehler on 16.03.2016.
 */
public class NodeStatisticsUpdater implements Runnable {
    private final INetworkReadingPort readingPort;
    private final double smooth = 0.3;
    private int interval = 1000;
    private HashMap<IAddress, NodeStatisticsComponent> last;

    public NodeStatisticsUpdater(INetwork network) {
        if (network == null) throw new NullPointerException("network must not be null!");
        this.readingPort = network.getReadingPort();
        this.interval = 1000;
    }

    public NodeStatisticsUpdater(INetwork network, int interval) {
        if (network == null) throw new NullPointerException("network must not be null!");
        if (interval < 0) throw new IllegalArgumentException("interval must not be less than 0!");
        this.readingPort = network.getReadingPort();
        this.interval = interval;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                update();
                Thread.sleep(interval);
            } catch (InterruptedException e) {

            }
        }
    }


    /**
     * <p>
     *     Updates the node component data using exponential smoothing
     * </p>
     */
    private void update() {
        final Collection<INode> nodes = readingPort.getNetworkNodes();
        if (nodes == null) return;
        if (last == null) {
            for (INode i:nodes) {
                last = new HashMap<>();
                NodeStatisticsComponent nsc = i.getComponent(NodeStatisticsComponent.class);

                IComponentVisitor<IComponent> copier = new ComponentCopier();
                NodeStatisticsComponent n2 = (NodeStatisticsComponent)nsc.accept(copier);
                last.put(i.getAddress(), n2);
            }
        }

        NodeStatisticsComponent cNew, cOld;

        for (INode i:nodes) {
            cNew = i.getComponent(NodeStatisticsComponent.class);
            cOld = last.get(i.getAddress());
            if (cOld == null) {
                cOld = cNew;
            }

            if (cNew != null && cOld != null) {
                int delta = cNew.getCommunicationCount() - cOld.getCommunicationCount();
                double tOld = cOld.getThroughput();

                double tn = smooth*delta + (1.0d-smooth)*tOld;

                cNew.setThroughput(tn);
            }

            IComponentVisitor<IComponent> copier = new ComponentCopier();
            NodeStatisticsComponent n2 = (NodeStatisticsComponent)cNew.accept(copier);
            if (last.get(i.getAddress()) != null) {
                last.replace(i.getAddress(), n2);
            } else {
                last.put(i.getAddress(), n2);
            }
        }

    }
}