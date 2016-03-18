package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent extends AbstractComponent implements IComponent {

    private static final Logger logger = LogManager.getLogger(NodeStatisticsComponent.class);
    
    private final IntegerProperty communicationCount;
    private final DoubleProperty throughput = new SimpleDoubleProperty(1);

    private final IntegerProperty ingoingCount;

    private final IntegerProperty outgoingCount;

    public NodeStatisticsComponent(int initialOutgoing, int initialIngoing) {

        ingoingCount = new SimpleIntegerProperty(initialIngoing);
        outgoingCount = new SimpleIntegerProperty(initialOutgoing);

        //communicationCountBinding = ingoingCount.add(outgoingCount);
        communicationCount = new SimpleIntegerProperty(0);
        communicationCount.bind(Bindings.add(ingoingCount, outgoingCount));
    }

    public int getOutgoingCount() {
        return outgoingCount.get();
    }

    public IntegerProperty outgoingCountProperty() {
        return outgoingCount;
    }

    public void setOutgoingCount(int outgoingCount) {
        this.outgoingCount.set(outgoingCount);
    }

    public int getIncomingCount() {
        return ingoingCount.get();
    }

    public IntegerProperty ingoingCountProperty() {
        return ingoingCount;
    }

    public void setIncomingCount(int ingoingCount) {
        this.ingoingCount.set(ingoingCount);
    }

    public IntegerProperty getCommunicationCountProperty() {

        return communicationCount;
    }

    public int getCommunicationCount() {
        return communicationCount.get();
    }

    //FIXME fix concurrency problem?
    /*
    Exception in thread "pool-5-thread-1" java.lang.NullPointerException
	at com.sun.javafx.binding.ExpressionHelper$Generic.fireValueChangedEvent(ExpressionHelper.java:349)
	at com.sun.javafx.binding.ExpressionHelper.fireValueChangedEvent(ExpressionHelper.java:81)
	at javafx.beans.property.IntegerPropertyBase.fireValueChangedEvent(IntegerPropertyBase.java:106)
	at javafx.beans.property.IntegerPropertyBase.markInvalid(IntegerPropertyBase.java:113)
	at javafx.beans.property.IntegerPropertyBase.set(IntegerPropertyBase.java:147)
	at edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent.setCommunicationCountProperty(NodeStatisticsComponent.java:36)
	at edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent.incrementThroughput(NodeStatisticsComponent.java:41)
	at edu.kit.trufflehog.model.network.graph.LiveUpdater.update(LiveUpdater.java:62)
	at edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent.update(NodeStatisticsComponent.java:70)
	at edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent.update(NodeStatisticsComponent.java:15)
	at edu.kit.trufflehog.model.network.graph.LiveUpdater.lambda$updateVertex$2(LiveUpdater.java:170)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:175)
	at java.util.Iterator.forEachRemaining(Iterator.java:116)
	at java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at edu.kit.trufflehog.model.network.graph.LiveUpdater.updateVertex(LiveUpdater.java:168)
	at edu.kit.trufflehog.model.network.graph.LiveUpdater.updateVertex(LiveUpdater.java:44)
	at edu.uci.ics.jung.graph.ObservableUpdatableGraph.updateVertex(ObservableUpdatableGraph.java:84)
	at edu.uci.ics.jung.graph.ObservableUpdatableGraph.addVertex(ObservableUpdatableGraph.java:171)
	at edu.kit.trufflehog.model.network.NetworkIOPort.writeNode(NetworkIOPort.java:65)
	at edu.kit.trufflehog.model.network.recording.NetworkWritingPortSwitch.writeNode(NetworkWritingPortSwitch.java:38)
	at edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand.execute(AddPacketDataCommand.java:95)
	at edu.kit.trufflehog.service.executor.CommandExecutor.run(CommandExecutor.java:45)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
     */

    public DoubleProperty getThroughputProperty() {
        return throughput;
    }

    public double getThroughput() {
        return throughput.getValue();
    }

    public void setThroughput(double value) {
        throughput.setValue(value);
    }

    @Override
    public String name() {
        return "Traffic info";
    }

    @Override
    public <T> T accept(IComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String toString() {

        return name() + ": " + "Throughput=" + getCommunicationCount();

    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NodeStatisticsComponent);
    }
}
