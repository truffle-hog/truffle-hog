package trufflehog.testing.jung;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * Created by julianbrendl on 12/9/15.
 */
public class Jung {
    public static void main(String[] args) {
        // Graph<V, E> where V is the type of the vertices
        // and E is the type of the edges
        Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex((Integer)1);
        g.addVertex((Integer)2);
        g.addVertex((Integer)3);
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", 2, 3);
        // Let's see what we have. Note the nice output from the
        // SparseMultigraph<V,E> toString() method
        System.out.println("The graph g = " + g.toString());
        // Note that we can use the same nodes and edges in two different graphs.
        Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
        g2.addVertex((Integer)1);
        g2.addVertex((Integer)2);
        g2.addVertex((Integer)3);
        g2.addEdge("Edge-A", 1,3);
        g2.addEdge("Edge-B", 2,3, EdgeType.DIRECTED);
        g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
        g2.addEdge("Edge-P", 2,3); // A parallel edge
        System.out.println("The graph g2 = " + g2.toString());
    }
}
