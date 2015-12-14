package trufflehog.testing.graphstream;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Created by julianbrendl on 12/9/15.
 */
public class GraphStream {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CA", "C", "A");

        graph.setAutoCreate( true );
        graph.addEdge( "AB", "A", "B" );
        graph.addEdge( "BC", "B", "C" );
        graph.addEdge( "CA", "C", "A" );

        graph.display();

        for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }

    }
}

