package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraphTestClassDummy;
import edu.kit.trufflehog.model.graph.NetworkGraph;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketDataTestClassDummy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 */
public class AddPacketDataCommandTest {

    private AddPacketDataCommand apdc;
    private AbstractNetworkGraphTestClassDummy graph;

    @Before
    public void setup() {
        IPacketData truffle = mock(IPacketData.class);
     //   truffle.setAttribute(String.class, "macAddress", "source");
       // truffle.setAttribute(String.class, "macAddress", "dest");

        Filter filter = new Filter();
        List<Filter> filterList = new LinkedList<Filter>();
        filterList.add(filter);

        graph = new AbstractNetworkGraphTestClassDummy();

        apdc = new AddPacketDataCommand(graph, truffle, filterList);
    }

    @After
    public void teardown() {
        apdc = null;
    }

    @Test
    public void addPacketDataCommandTest1() {
        apdc.execute();
        assert(graph.hasConnection() == true);
    }
}