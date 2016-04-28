package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Hoehler on 30.03.2016.
 */
public class NameRegexFilterTest {
    @Test
    public void check() throws Exception{
        NetworkIOPort port = Mockito.mock(NetworkIOPort.class);
        INode node = new NetworkNode(new MacAddress(123));
        NodeInfoComponent infoComponent = new NodeInfoComponent(new MacAddress(123));
        infoComponent.setDeviceName("asddddbladdddsa");
        FilterPropertiesComponent propertiesComponent = new FilterPropertiesComponent();

        node.addComponent(infoComponent);
        node.addComponent(propertiesComponent);

        List<String> rules = new LinkedList<>();
        rules.add(".*bla.*");
        FilterInput input = new FilterInput("test", SelectionModel.SELECTION, FilterType.NAME, rules, Color.CYAN, false, 0);

        boolean testPassed = true;

        NameRegexFilter filter = new NameRegexFilter(port, input);
        if (propertiesComponent.getFilterColor() != null) testPassed = false;
        filter.check(node);
        if (!propertiesComponent.getFilterColor().equals(Color.CYAN)) testPassed = false;

        assertTrue(testPassed);
    }


    @Test
    public void clear() throws Exception{
        NetworkIOPort port = Mockito.mock(NetworkIOPort.class);
        INode node = new NetworkNode(new MacAddress(123));
        List<INode> nodeList = new LinkedList<>();
        nodeList.add(node);
        when(port.getNetworkNodes()).thenReturn(nodeList);


        NodeInfoComponent infoComponent = new NodeInfoComponent(new MacAddress(123));
        infoComponent.setDeviceName("asddddbladdddsa");
        FilterPropertiesComponent propertiesComponent = new FilterPropertiesComponent();

        node.addComponent(infoComponent);
        node.addComponent(propertiesComponent);

        List<String> rules = new LinkedList<>();
        rules.add(".*bla.*");
        FilterInput input = new FilterInput("test", SelectionModel.SELECTION, FilterType.NAME, rules, Color.CYAN, false, 0);

        NameRegexFilter filter = new NameRegexFilter(port, input);

        filter.check(node);
        filter.clear();

        assertNull(propertiesComponent.getFilterColor());
    }
}