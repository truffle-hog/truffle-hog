package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Valentin Kiechle on 10.04.2016.
 */
public class SelectionCommandTest {

    private StatisticsViewModel svm;
    private Pair<Set<INode>, Set<IConnection>> selection;
    private SelectionCommand sc;
    private Set<INode> nodes;
    private Set<IConnection> edges;
    private TreeItem infos;


    @Before
    public void setup() {
        svm = mock(StatisticsViewModel.class);
        sc = new SelectionCommand(svm);
        infos = new TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>();
    }

    @After
    public void teardown() {
        svm = null;
        sc = null;
        infos = null;
    }

    @Test
    public void SelectionCommandTest_nothingSelectedSoStatisticsGetCleared() {
        sc.execute();
        verify(svm, times(1)).clearStatistics();
    }

    @Test
    public void SelectionCommandTest_onlyConnectionsSelected() {
        edges = new HashSet<>();
        IConnection testEdge = mock(IConnection.class);
        edges.add(testEdge);
        nodes = new HashSet<>();

        Stream stream = mock(Stream.class);
        when(testEdge.stream()).thenReturn(stream);
        when(testEdge.stream().collect(any(Collector.class))).thenReturn(infos);

        selection = new ImmutablePair<>(nodes, edges);
        sc.setSelection(selection);
        sc.execute();//TODO not running yet
        verify(infos, times(1)).setExpanded(true);
        //TODO test platform runlater
    }

}